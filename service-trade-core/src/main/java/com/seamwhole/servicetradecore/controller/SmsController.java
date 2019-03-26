package com.seamwhole.servicetradecore.controller;

import com.seamwhole.except.CheckException;
import com.seamwhole.servicetradecore.model.SysSmsLog;
import com.seamwhole.servicetradecore.service.SysSmsLogService;
import com.seamwhole.servicetradecore.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 发送短信接口Controller
 *
 * @author liepngjun
 * @email 939961241@qq.com
 * @date 2018-06-05 13:58:47
 */
@RestController
@RequestMapping("api")
public class SmsController {
    @Autowired
    private SysSmsLogService sysSmsLogService;

    /**
     * 发送短信
     *
     * @param request request
     * @param params 请求参数{mobile：电话号码字符串，中间用英文逗号间隔,content：内容字符串,stime：追加发送时间，可为空，为空为及时发送}
     * @return R
     */
    @RequestMapping("/sendSms")
    public ResponseObject sendSms(HttpServletRequest request, @RequestParam Map<String, String> params) {
        SysSmsLog smsLog = new SysSmsLog();
        String validIP = RequestUtil.getIpAddrByRequest(request);
        if (ResourceUtil.getConfigByName("sms.validIp").indexOf(validIP) < 0) {
            throw new CheckException("非法IP请求！");
        }
        smsLog.setMobile(params.get("mobile"));
        smsLog.setContent(params.get("content"));
        String stime = params.get("stime");
        if (StringUtils.isNotEmpty(stime)) {
            smsLog.setStime(DateUtils.convertStringToDate(stime));
        }
        SysSmsLogEntity sysSmsLogEntity = smsLogService.sendSms(smsLog);
        return ResponseObject.ok().put("result", sysSmsLogEntity);
    }
}
