package com.proxy.dao;

public interface FeedbackDaoMapper {

    void addFeedback(String proxyName, String content, long time);
}
