package com.seamwhole.serviceerpcore.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.serializer.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jishenghua qq752718920  2018-10-7 15:26:27
 */
public class ExtJsonUtils {
    private static class NPFloatCodec extends FloatCodec {
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
            SerializeWriter out = serializer.getWriter();

            if (object == null) {
                if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                    out.write('0');
                } else {
                    out.writeNull();
                }
                return;
            }

            float floatValue = (Float) object;

            if (Float.isNaN(floatValue)) {
                out.writeNull();
            } else if (Float.isInfinite(floatValue)) {
                out.writeNull();
            } else {
                String floatText = Float.toString(floatValue);
                out.write(floatText);

                if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                    out.write('F');
                }
            }
        }
    }

    private static class NPDoubleSerializer extends DoubleSerializer {
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
            SerializeWriter out = serializer.getWriter();

            if (object == null) {
                if (!serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                    out.writeNull();
                } else {
                    out.write('0');
                }
                return;
            }

            double doubleValue = (Double) object;

            if (Double.isNaN(doubleValue)) {
                out.writeNull();
            } else if (Double.isInfinite(doubleValue)) {
                out.writeNull();
            } else {
                String doubleText;
                doubleText = Double.toString(doubleValue);
                out.append(doubleText);

                if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                    out.write('D');
                }
            }
        }
    }

    private static final String EXT_NAME = "ext";

    static class ExtFilter extends AfterFilter implements PropertyFilter {
        static {
            SerializeConfig.getGlobalInstance().put(Float.class, new NPFloatCodec());
            SerializeConfig.getGlobalInstance().put(float.class, new NPFloatCodec());
            SerializeConfig.getGlobalInstance().put(Double.class, new NPDoubleSerializer());
            SerializeConfig.getGlobalInstance().put(double.class, new NPDoubleSerializer());
        }

        private Map<Object, JSONObject> map = new HashMap<>();

        private Map<Object, Set<String>> ignoredKey = new HashMap<>();

        @Override
        public boolean apply(Object object, String name, Object value) {
            if (name.equals(EXT_NAME) && value instanceof String) {
                map.put(object, JSON.parseObject((String) value));
                return false;
            }
            if (!map.containsKey(object)) {
                ignoredKey.put(object, new HashSet<String>());
            }
            ignoredKey.get(object).add(name);
//            if (value instanceof Float || value instanceof Double) {
//                if (!floatMap.containsKey(object)) {
//                    floatMap.put(object, new HashMap<String, Object>());
//                }
//                floatMap.get(object).put(name, value);
//                return false;
//            }
            return true;
        }

        @Override
        public void writeAfter(Object object) {
            if (map.containsKey(object)) {
                Set<String> ignoredKeys;
                if (ignoredKey.containsKey(object)) {
                    ignoredKeys = ignoredKey.get(object);
                } else {
                    ignoredKeys = new HashSet<>();
                }
                for (Map.Entry<String, Object> entry : map.get(object).entrySet()) {
                    if (!ignoredKeys.contains(entry.getKey())) {
                        writeKeyValue(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, new ExtFilter());
    }

    public interface ExtExtractor {
        String getExt(Object bean);
    }

    private static class MetaInfo {
        private final static ParserConfig INSTANCE = ParserConfig.getGlobalInstance();

        private final Object object;
        private final Map<String, FieldDeserializer> map;
        private final JSONObject ext = new JSONObject();

        private MetaInfo(Object object) {
            this.object = object;
            this.map = INSTANCE.getFieldDeserializers(object.getClass());
        }

        void gather(String key, Object value) {
            if (!map.containsKey(key)) {
                ext.put(key, value);
            }
        }

        public void update(ExtExtractor extractor) {
            JSONObject old = JSON.parseObject(extractor.getExt(object));
            if (old == null) {
                old = new JSONObject();
            }
            old.putAll(ext);
            map.get(EXT_NAME).setValue(object, old.toJSONString());
        }

        static boolean hasExt(Class<?> clazz) {
            return INSTANCE.getFieldDeserializers(clazz).containsKey(EXT_NAME);
        }
    }

    public static <T> T parseObject(String text, final Class<T> clazz, ExtExtractor extractor) {
        final Map<Object, MetaInfo> map = new HashMap<>();

        T object = JSON.parseObject(text, clazz, new ExtraProcessor() {
            @Override
            public void processExtra(Object object, String key, Object value) {
                if (!map.containsKey(object) && MetaInfo.hasExt(object.getClass())) {
                    map.put(object, new MetaInfo(object));
                }
                if (map.containsKey(object)) {
                    map.get(object).gather(key, value);
                }
            }
        });

        for (Map.Entry<Object, MetaInfo> entry : map.entrySet()) {
            entry.getValue().update(extractor);
        }

        return object;
    }
}
