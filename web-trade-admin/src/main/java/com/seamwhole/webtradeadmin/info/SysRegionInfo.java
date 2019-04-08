package com.seamwhole.webtradeadmin.info;

import java.io.Serializable;

public class SysRegionInfo extends SysRegionDO implements Serializable {

    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
