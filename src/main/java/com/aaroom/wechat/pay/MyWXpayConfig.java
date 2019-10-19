package com.aaroom.wechat.pay;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by sosoda on 2019/2/22.
 */
@Component
public class MyWXpayConfig extends WXPayConfig {

    private byte[] certData;

    @Value("${weixin.client.appId}")
    @Getter
    private String appID;

    @Value("${weixin.pay.mchID}")
    @Getter
    private String mchID;

    @Value("${weixin.pay.key}")
    @Getter
    private String key;

    //Todo：自动切换不同的渠道
    public MyWXpayConfig() throws Exception {
        Resource resource = new ClassPathResource("apiclient_cert.p12");
        InputStream certStream = resource.getInputStream();
        this.certData = new byte[(int) resource.contentLength()];
        certStream.read(this.certData);
        certStream.close();
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            //Todo：报告问题，暂时无需实现
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}