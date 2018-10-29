package com.cloudSeckill.utils;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 日志工具类
 */
public class LogUtils {
    private static LogUtils logUtils = new LogUtils();
    private static Logger log = Logger.getLogger("wechatProj-cursor_fei:");
    private static org.apache.commons.logging.Log errorlog = LogFactory.getLog("errorlog");
    private static org.apache.commons.logging.Log infolog = LogFactory.getLog("infolog");

    //不允许外界实例化
    private LogUtils() {
    }


    //打印info信息
    public static void info(Object obj) {
        /*StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement sta = stackTrace[2];
        log = Logger.getLogger(sta.getClassName());
        String str = "方法:" + sta.getMethodName() + ",行:" + sta.getLineNumber() + "; - " + obj.toString();
        log.info(str);*/
        //log.info(obj.toString());
        infoLogout(obj);

    }

    //打印warn信息
    public static void warn(Object obj) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement sta = stackTrace[2];
        log = Logger.getLogger(sta.getClassName());
        String str = "方法:" + sta.getMethodName() + ",行:" + sta.getLineNumber() + "; - " + obj.toString();
        log.warn(str);
    }

    //打印error信息
    public static void error(Object obj) {
        /*StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement sta = stackTrace[2];
        log = Logger.getLogger(sta.getClassName());
        String str = "方法:" + sta.getMethodName() + ",行:" + sta.getLineNumber() + "; - " + obj.toString();
        log.error(str);*/
        //log.error(obj.toString());
        errorLogout(obj);
    }

    //打印fatal信息
    public static void fatal(Object obj) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement sta = stackTrace[2];
        log = Logger.getLogger(sta.getClassName());
        String str = "方法:" + sta.getMethodName() + ",行:" + sta.getLineNumber() + "; - " + obj.toString();
        log.fatal(str);
    }

    public static void errorLogout(Object obj) {
        try {
            errorlog.error(new String(obj.toString().getBytes("GBK"), Charset.defaultCharset()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void infoLogout(Object obj) {
        try {
            infolog.info(new String(obj.toString().getBytes("GBK"), Charset.defaultCharset()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
