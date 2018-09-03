package com.cloudSeckill.dao.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitLength = -1;

    public UserExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitLength(int limitLength) {
        this.limitLength=limitLength;
    }

    public int getLimitLength() {
        return limitLength;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andFromUserNameIsNull() {
            addCriterion("from_user_name is null");
            return (Criteria) this;
        }

        public Criteria andFromUserNameIsNotNull() {
            addCriterion("from_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andFromUserNameEqualTo(String value) {
            addCriterion("from_user_name =", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameNotEqualTo(String value) {
            addCriterion("from_user_name <>", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameGreaterThan(String value) {
            addCriterion("from_user_name >", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("from_user_name >=", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameLessThan(String value) {
            addCriterion("from_user_name <", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameLessThanOrEqualTo(String value) {
            addCriterion("from_user_name <=", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameLike(String value) {
            addCriterion("from_user_name like", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameNotLike(String value) {
            addCriterion("from_user_name not like", value, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameIn(List<String> values) {
            addCriterion("from_user_name in", values, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameNotIn(List<String> values) {
            addCriterion("from_user_name not in", values, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameBetween(String value1, String value2) {
            addCriterion("from_user_name between", value1, value2, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andFromUserNameNotBetween(String value1, String value2) {
            addCriterion("from_user_name not between", value1, value2, "fromUserName");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andHeadImgIsNull() {
            addCriterion("head_img is null");
            return (Criteria) this;
        }

        public Criteria andHeadImgIsNotNull() {
            addCriterion("head_img is not null");
            return (Criteria) this;
        }

        public Criteria andHeadImgEqualTo(String value) {
            addCriterion("head_img =", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotEqualTo(String value) {
            addCriterion("head_img <>", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgGreaterThan(String value) {
            addCriterion("head_img >", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgGreaterThanOrEqualTo(String value) {
            addCriterion("head_img >=", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLessThan(String value) {
            addCriterion("head_img <", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLessThanOrEqualTo(String value) {
            addCriterion("head_img <=", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLike(String value) {
            addCriterion("head_img like", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotLike(String value) {
            addCriterion("head_img not like", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgIn(List<String> values) {
            addCriterion("head_img in", values, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotIn(List<String> values) {
            addCriterion("head_img not in", values, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgBetween(String value1, String value2) {
            addCriterion("head_img between", value1, value2, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotBetween(String value1, String value2) {
            addCriterion("head_img not between", value1, value2, "headImg");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNull() {
            addCriterion("income is null");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNotNull() {
            addCriterion("income is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeEqualTo(Integer value) {
            addCriterion("income =", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotEqualTo(Integer value) {
            addCriterion("income <>", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThan(Integer value) {
            addCriterion("income >", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThanOrEqualTo(Integer value) {
            addCriterion("income >=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThan(Integer value) {
            addCriterion("income <", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThanOrEqualTo(Integer value) {
            addCriterion("income <=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeIn(List<Integer> values) {
            addCriterion("income in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotIn(List<Integer> values) {
            addCriterion("income not in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeBetween(Integer value1, Integer value2) {
            addCriterion("income between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotBetween(Integer value1, Integer value2) {
            addCriterion("income not between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andWechatIdIsNull() {
            addCriterion("wechat_id is null");
            return (Criteria) this;
        }

        public Criteria andWechatIdIsNotNull() {
            addCriterion("wechat_id is not null");
            return (Criteria) this;
        }

        public Criteria andWechatIdEqualTo(String value) {
            addCriterion("wechat_id =", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdNotEqualTo(String value) {
            addCriterion("wechat_id <>", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdGreaterThan(String value) {
            addCriterion("wechat_id >", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdGreaterThanOrEqualTo(String value) {
            addCriterion("wechat_id >=", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdLessThan(String value) {
            addCriterion("wechat_id <", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdLessThanOrEqualTo(String value) {
            addCriterion("wechat_id <=", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdLike(String value) {
            addCriterion("wechat_id like", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdNotLike(String value) {
            addCriterion("wechat_id not like", value, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdIn(List<String> values) {
            addCriterion("wechat_id in", values, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdNotIn(List<String> values) {
            addCriterion("wechat_id not in", values, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdBetween(String value1, String value2) {
            addCriterion("wechat_id between", value1, value2, "wechatId");
            return (Criteria) this;
        }

        public Criteria andWechatIdNotBetween(String value1, String value2) {
            addCriterion("wechat_id not between", value1, value2, "wechatId");
            return (Criteria) this;
        }

        public Criteria andExpirTimeIsNull() {
            addCriterion("expir_time is null");
            return (Criteria) this;
        }

        public Criteria andExpirTimeIsNotNull() {
            addCriterion("expir_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpirTimeEqualTo(Date value) {
            addCriterionForJDBCDate("expir_time =", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("expir_time <>", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("expir_time >", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expir_time >=", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeLessThan(Date value) {
            addCriterionForJDBCDate("expir_time <", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("expir_time <=", value, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeIn(List<Date> values) {
            addCriterionForJDBCDate("expir_time in", values, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("expir_time not in", values, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expir_time between", value1, value2, "expirTime");
            return (Criteria) this;
        }

        public Criteria andExpirTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("expir_time not between", value1, value2, "expirTime");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIsNull() {
            addCriterion("online_status is null");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIsNotNull() {
            addCriterion("online_status is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusEqualTo(Integer value) {
            addCriterion("online_status =", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotEqualTo(Integer value) {
            addCriterion("online_status <>", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusGreaterThan(Integer value) {
            addCriterion("online_status >", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("online_status >=", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusLessThan(Integer value) {
            addCriterion("online_status <", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusLessThanOrEqualTo(Integer value) {
            addCriterion("online_status <=", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIn(List<Integer> values) {
            addCriterion("online_status in", values, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotIn(List<Integer> values) {
            addCriterion("online_status not in", values, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusBetween(Integer value1, Integer value2) {
            addCriterion("online_status between", value1, value2, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("online_status not between", value1, value2, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andPickTypeIsNull() {
            addCriterion("pick_type is null");
            return (Criteria) this;
        }

        public Criteria andPickTypeIsNotNull() {
            addCriterion("pick_type is not null");
            return (Criteria) this;
        }

        public Criteria andPickTypeEqualTo(Integer value) {
            addCriterion("pick_type =", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeNotEqualTo(Integer value) {
            addCriterion("pick_type <>", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeGreaterThan(Integer value) {
            addCriterion("pick_type >", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pick_type >=", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeLessThan(Integer value) {
            addCriterion("pick_type <", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeLessThanOrEqualTo(Integer value) {
            addCriterion("pick_type <=", value, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeIn(List<Integer> values) {
            addCriterion("pick_type in", values, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeNotIn(List<Integer> values) {
            addCriterion("pick_type not in", values, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeBetween(Integer value1, Integer value2) {
            addCriterion("pick_type between", value1, value2, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("pick_type not between", value1, value2, "pickType");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeIsNull() {
            addCriterion("pick_delay_time is null");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeIsNotNull() {
            addCriterion("pick_delay_time is not null");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeEqualTo(Integer value) {
            addCriterion("pick_delay_time =", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeNotEqualTo(Integer value) {
            addCriterion("pick_delay_time <>", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeGreaterThan(Integer value) {
            addCriterion("pick_delay_time >", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pick_delay_time >=", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeLessThan(Integer value) {
            addCriterion("pick_delay_time <", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeLessThanOrEqualTo(Integer value) {
            addCriterion("pick_delay_time <=", value, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeIn(List<Integer> values) {
            addCriterion("pick_delay_time in", values, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeNotIn(List<Integer> values) {
            addCriterion("pick_delay_time not in", values, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeBetween(Integer value1, Integer value2) {
            addCriterion("pick_delay_time between", value1, value2, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("pick_delay_time not between", value1, value2, "pickDelayTime");
            return (Criteria) this;
        }

        public Criteria andPickDelayIsNull() {
            addCriterion("pick_delay is null");
            return (Criteria) this;
        }

        public Criteria andPickDelayIsNotNull() {
            addCriterion("pick_delay is not null");
            return (Criteria) this;
        }

        public Criteria andPickDelayEqualTo(Integer value) {
            addCriterion("pick_delay =", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayNotEqualTo(Integer value) {
            addCriterion("pick_delay <>", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayGreaterThan(Integer value) {
            addCriterion("pick_delay >", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayGreaterThanOrEqualTo(Integer value) {
            addCriterion("pick_delay >=", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayLessThan(Integer value) {
            addCriterion("pick_delay <", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayLessThanOrEqualTo(Integer value) {
            addCriterion("pick_delay <=", value, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayIn(List<Integer> values) {
            addCriterion("pick_delay in", values, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayNotIn(List<Integer> values) {
            addCriterion("pick_delay not in", values, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayBetween(Integer value1, Integer value2) {
            addCriterion("pick_delay between", value1, value2, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickDelayNotBetween(Integer value1, Integer value2) {
            addCriterion("pick_delay not between", value1, value2, "pickDelay");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonIsNull() {
            addCriterion("pick_group_list_json is null");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonIsNotNull() {
            addCriterion("pick_group_list_json is not null");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonEqualTo(String value) {
            addCriterion("pick_group_list_json =", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonNotEqualTo(String value) {
            addCriterion("pick_group_list_json <>", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonGreaterThan(String value) {
            addCriterion("pick_group_list_json >", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonGreaterThanOrEqualTo(String value) {
            addCriterion("pick_group_list_json >=", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonLessThan(String value) {
            addCriterion("pick_group_list_json <", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonLessThanOrEqualTo(String value) {
            addCriterion("pick_group_list_json <=", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonLike(String value) {
            addCriterion("pick_group_list_json like", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonNotLike(String value) {
            addCriterion("pick_group_list_json not like", value, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonIn(List<String> values) {
            addCriterion("pick_group_list_json in", values, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonNotIn(List<String> values) {
            addCriterion("pick_group_list_json not in", values, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonBetween(String value1, String value2) {
            addCriterion("pick_group_list_json between", value1, value2, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andPickGroupListJsonNotBetween(String value1, String value2) {
            addCriterion("pick_group_list_json not between", value1, value2, "pickGroupListJson");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalIsNull() {
            addCriterion("auto_pick_personal is null");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalIsNotNull() {
            addCriterion("auto_pick_personal is not null");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalEqualTo(Integer value) {
            addCriterion("auto_pick_personal =", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalNotEqualTo(Integer value) {
            addCriterion("auto_pick_personal <>", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalGreaterThan(Integer value) {
            addCriterion("auto_pick_personal >", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalGreaterThanOrEqualTo(Integer value) {
            addCriterion("auto_pick_personal >=", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalLessThan(Integer value) {
            addCriterion("auto_pick_personal <", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalLessThanOrEqualTo(Integer value) {
            addCriterion("auto_pick_personal <=", value, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalIn(List<Integer> values) {
            addCriterion("auto_pick_personal in", values, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalNotIn(List<Integer> values) {
            addCriterion("auto_pick_personal not in", values, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalBetween(Integer value1, Integer value2) {
            addCriterion("auto_pick_personal between", value1, value2, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoPickPersonalNotBetween(Integer value1, Integer value2) {
            addCriterion("auto_pick_personal not between", value1, value2, "autoPickPersonal");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferIsNull() {
            addCriterion("auto_receive_transfer is null");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferIsNotNull() {
            addCriterion("auto_receive_transfer is not null");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferEqualTo(Integer value) {
            addCriterion("auto_receive_transfer =", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferNotEqualTo(Integer value) {
            addCriterion("auto_receive_transfer <>", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferGreaterThan(Integer value) {
            addCriterion("auto_receive_transfer >", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferGreaterThanOrEqualTo(Integer value) {
            addCriterion("auto_receive_transfer >=", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferLessThan(Integer value) {
            addCriterion("auto_receive_transfer <", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferLessThanOrEqualTo(Integer value) {
            addCriterion("auto_receive_transfer <=", value, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferIn(List<Integer> values) {
            addCriterion("auto_receive_transfer in", values, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferNotIn(List<Integer> values) {
            addCriterion("auto_receive_transfer not in", values, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferBetween(Integer value1, Integer value2) {
            addCriterion("auto_receive_transfer between", value1, value2, "autoReceiveTransfer");
            return (Criteria) this;
        }

        public Criteria andAutoReceiveTransferNotBetween(Integer value1, Integer value2) {
            addCriterion("auto_receive_transfer not between", value1, value2, "autoReceiveTransfer");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}