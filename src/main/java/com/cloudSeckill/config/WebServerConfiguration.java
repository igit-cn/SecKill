package com.cloudSeckill.config;

import com.cloudSeckill.utils.LogUtils;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebServerConfiguration {
    @Bean
    public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
        LogUtils.info("Tomcat服务器自定义配置");
        TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatFactory.setPort(80);
        tomcatFactory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
        tomcatFactory.setSessionTimeout(30, TimeUnit.MINUTES);
        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcatFactory;
    }
}


class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
    public void customize(Connector connector) {
        Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
        //设置最大连接数
        protocol.setMaxConnections(10000);
        protocol.setConnectionTimeout(20000);
        //设置最大线程数
        protocol.setMaxThreads(10000);
        protocol.setAcceptCount(1000);
        //缓存
        protocol.setCompression("on");
//        protocol.setCompressionMinSize(2048);
//        protocol.setCompressibleMimeType("text/html,text/xml,text/plain,text/css,text/javascript,application/javascript");
        protocol.setMaxSavePostSize(10485760);
        protocol.setMaxHttpHeaderSize(8192);
        protocol.setDisableUploadTimeout(true);
        protocol.setAcceptorThreadCount(4);
    }
}
