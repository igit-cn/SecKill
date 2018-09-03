package com.cloudSeckill.net.http;

public interface NetApi {
    
    //测试环境
//    String HOST_URL = "http://test.user.toutime.com.cn/";
    //正式环境2.0.0以前的
//    String HOST_URL = "http://www.toutime.com.cn/coffeeInterface/";
    //正式环境 2.1.0以后的
    String HOST_URL = "http://user.toutime.com.cn/";
    
    /** 登录 */
    String userLogin = HOST_URL + "user/loginApp";
    /** 查找用户信息 */
    String findUserInfo = HOST_URL + "user/findUserInfo";
    /** 获取验证码 */
    String verifyCode = HOST_URL + "user/obtainVerificationCode";
    /** 注册 */
    String regist = HOST_URL + "user/registerUser";
    /** 初始化密码 */
    String resetPwd = HOST_URL + "user/forgetUserPassword";
    /** 第三方登录 */
    String threeLogin = HOST_URL + "user/findUserInfoByThree";
    /** 微信获取Token */
    String getWechatToken = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /** 修改用户注册手机 */
    String bangUserPhone = HOST_URL + "user/bangUserPhone";
    /** 重置支付密码 */
    String forgetUserPayPassword = HOST_URL + "user/forgetUserPayPassword";
    /** 通过旧密码修改新密码 */
    String updateUserPasswordNew = HOST_URL + "user/updateUserPasswordNew";
    /** 查询用户未读消息数量 */
    String findUserMsgUnReadCount = HOST_URL + "user/findUserMsgUnReadCount";
    /** 获取用户消息列表 */
    String findUserMsgList = HOST_URL + "user/findUserMsgList";
    /** 获取收货地址列表 */
    String findUserReceiveInfoList = HOST_URL + "user/findUserReceiveInfoList";
    /** 新增编辑收货地址 */
    String addUserReceiveInfo = HOST_URL + "user/addUserReceiveInfo";
    /** 删除收货地址列表 */
    String delUserReceiveInfo = HOST_URL + "user/delUserReceiveInfo";
    /** 获取用户等级信息 */
    String findUserGradeInfo = HOST_URL + "user/findUserGradeInfo";
    /** 用户当月签到列表 */
    String userSighDayList = HOST_URL + "user/userSighDayList";
    /** 用户签到 */
    String userSign = HOST_URL + "user/userSign";
    /** 第三方登录绑定手机号 */
    String threeLoginBindingPhone = HOST_URL + "user/loginThree";
    /** 系统开关（充值开关，邀请好友开关）*/
    String findInviteSwitch = HOST_URL + "user/findInviteSwitch";
    /** 我的小时积分列表 */
    String findUserIntegralLogList = HOST_URL + "user/findUserIntegralLogList";
    /** 获取首页商铺列表 */
    String shopList = HOST_URL + "shop/findShopList";
    /** 收藏关注 */
    String addCollectionInfo = HOST_URL + "user/addCollectionInfo";
    /** 取消收藏  */
    String delCollectionInfo = HOST_URL + "user/delCollectionInfo";
    /** 关注收藏列表（大咖 和商店点赞数 ）*/
    String myCollectionList = HOST_URL + "user/myCollectionList";
    /** 获取新的朋友列表 */
    String findNewFriendList = HOST_URL + "user/findUserApplyFriendList";
    /** 通过关键字查询朋友列表 */
    String searchFriendList = HOST_URL + "user/searchUserByPhoneName";
    /** 申请添加好友 */
    String addUserMsg = HOST_URL + "user/addUserMsg";
    /** 同意或者拒绝添加好友 */
    String acceptUserFriend = HOST_URL + "user/acceptUserFriend";
    /** 查询我的评论列表 */
    String findClientUserCommentList = HOST_URL + "product/findClientUserCommentList";
    /** 配置极光推送是否打开 */
    String configIsPush = HOST_URL + "user/configIsPush";
    /** 偷时分享点击接口地址 */
    String inviteFriendShareUrl = HOST_URL + "user/inviteFriendShareUrl";//线上的分享地址要测
    /** 删除好友 */
    String delUserFriend = HOST_URL + "user/delUserFriend";
    /** 偷时首页 */
    String findWhenStealingInfo = HOST_URL + "user/findWhenStealingInfo";
    /** 可能认识的人 */
    String getPossibleFriends = HOST_URL + "user/getPossibleFriends";
    /** 我的优惠券 */
    String myCouponList = HOST_URL + "user/myCouponList";
    /** 删除购物车商品 */
    String deleteShoppingCart = HOST_URL + "order/deleteShoppingCart";
    /** 积分商品列表 */
    String findIntegralProductList = HOST_URL + "user/findIntegralProductList";
    /** 秒杀优惠券 */
    String userMiaosha = HOST_URL + "user/userMiaosha";
    /** 获取整百充值的优惠减免列表 */
    String findChargeActList = HOST_URL + "news/findChargeActList";
    /** 获取用户积分数量 和 秒杀剩余数量 */
    String findUserIntegral = HOST_URL + "user/findUserIntegral";
    /** 兑换记录 */
    String myExchangeRecords = HOST_URL + "product/myExchangeRecords";
    /** 偷好友小时 */
    String userTouTime = HOST_URL + "user/userTouTime";
    /** 商家评论列表 */
    String findClientShopCommentList = HOST_URL + "product/findClientShopCommentList";
    /** 删除订单 */
    String deleteOrderMsg = HOST_URL + "order/deleteOrder";
    // 获取融云token
    String findRongyunToken = HOST_URL + "user/findRongyunToken";
    /** 订单课程订单列表 */
    String findOrderCourseList = HOST_URL + "order/findOrderCourseList";
    /** 取消订单原因 */
    String saveCancelOrderReason = HOST_URL + "order/saveCancelOrderReason";
    /** 加入购物车 */
    String addShoppingCartInfo = HOST_URL + "order/addShoppingCartInfo";
    /** 增加转发数 */
    String addNewsZhuafaCount = HOST_URL + "user/addNewsZhuafaCount";
    /** 支付密码验证 */
    String checkPayPassword = HOST_URL + "user/checkPayPassword";
    /** 争做榜首列表 */
    String getUserTouIntegralList = HOST_URL + "user/getUserTouIntegralList";
    /** 课程下单 */
    String addOrderCourse = HOST_URL + "order/addOrderCourse";
    /** 获取课程列表 */
    String findCourseList = HOST_URL + "news/findCourseList";
    /** 查找支付页面购物车支付商品列表 */
    String findShopCartPayProductList = HOST_URL + "order/findShopCartPayProductList";
    /** 配送员位置信息 */
    String findOrderSenderPosition = HOST_URL + "order/findOrderSenderPosition";
    /** 首页Banner图 */
    String homeBannerList = HOST_URL + "activity/homeBannerList";
    /** 根据type 获取banner图片 */
    String findOtherBannerList = HOST_URL + "activity/findOtherBannerList";
    /** 购物车列表*/
    String findShoppingCartList = HOST_URL + "order/findShoppingCartList";
    /** 我的折扣卡 */
    String myZhekouCardList = HOST_URL + "user/myZhekouCardList";
    /** 积分商品兑换*/
    String integralProductExchange = HOST_URL + "product/integralProductExchange";
    /** 申请入住商家 */
    String userApplyShop = HOST_URL + "user/userApplyShop";
    /** 咖啡豆充值 - 微信参数获取 */
    String getWechatPayParam = HOST_URL + "order/findWXPayAppParam";
    /** 咖啡豆充值 - 支付宝参数获取 */
    String getAlipayParam = HOST_URL + "order/findAlipayParam";
    /** 充值码兑换咖啡豆 */
    String exchUserCoinCoupon = HOST_URL + "order/exchUserCoinCoupon";
    /** 生成订单 */
    String findAddOrder = HOST_URL + "order/addOrder";
    /** 创建充值下单 */
    String createOrderWallet = HOST_URL  + "order/createOrderWallet";
    /** 商品列表 */
    String findProductList = HOST_URL + "product/findProductList";
    /** 用户随享列表 */
    String listFeedback = HOST_URL + "issues/listFeedback";
    /** 添加一条意见反馈 --> 用户随享 */
    String addFeedback = HOST_URL + "issues/addFeedback";
    /** 大咖分享 --> 大咖列表 */
    String masterList = HOST_URL + "master/list";
    /** 获取博文等内容的列表 (好店分享 偷时问答) */
    String listNews = HOST_URL + "news/listNews";
    /** 热门商区位置过滤 */
    String cityHotList = HOST_URL + "activity/cityHotList";
    /** 充值50返50活动 */
    String findChargeMoney50 = HOST_URL + "activity/findReturnBackPhoto";
    /** 订单商品订单列表 */
    String findOrderList = HOST_URL + "order/findOrderList";
    /** 文章内容+作者信息 */
    String newsDetailApp = HOST_URL + "news/findNewsDetail";
    /** 评论列表 */
    String findCommentList = HOST_URL + "activity/findCommentList";
    /** 大咖文章图片列表 */
    String findNewsPhotoList = HOST_URL + "news/findNewsPhotoList";//没有测试全面
    /** 大咖详情列表 */
    String masterDetail = HOST_URL + "master/detail";
    /** 获取大咖列表 */
    String findBigPersonList = HOST_URL + "news/findBigPersonList";
    /** 好友等级排行 */
    String findUserFriendsRankList = HOST_URL + "user/findUserFriendsRankList";
    /** 查找商铺信息 */
    String findShopMsg = HOST_URL + "shop/findShopMsg";
    /** 图文教程、器具原料、海量资讯，大咖文章列表列表 */
    String findNewsList = HOST_URL + "news/findNewsList";
    /** 视频列表 */
    String findNewsVideoList = HOST_URL + "news/findNewsVideoList";
    /** 获取Web或者课程的标题名字 */
    String findNewsPart = HOST_URL + "news/findNewsPart";
    /** 课程的时间地点 */
    String findCourseTimeList = HOST_URL + "news/findCourseTimeList";
    /** 课程详情4个课程 */
    String findCourseAllDetail = HOST_URL + "news/findCourseAllDetail";
    /** 商家列表含有商家产品分类所有元素 */
    String findShopByProduct = HOST_URL + "shop/findShopByProduct";
    /** 添加文章评论 */
    String addNewsCommentMsg = HOST_URL + "activity/addNewsCommentMsg";
    /** 确认收货 */
    String confirmOrderReceived = HOST_URL + "order/confirmOrderReceived";
    /** 取消订单 */
    String cancelOrder = HOST_URL + "order/cancelOrder";
    /** 图片上传 */
    String uploadBase64image = HOST_URL + "/common/upload/base64";
    /** 修改用户信息 */
    String updateUserBaseInfo = HOST_URL + "user/updateUserBaseInfo";
    /** 添加订单评论 */
    String addCommentMsg = HOST_URL + "activity/addCommentMsg";
    /** 检查版本更新 */
    String findVersionInfo = HOST_URL + "user/findVersionInfo";
    
    // 获取课程码 赵
    String findOrderCourseCode = HOST_URL + "order/findOrderCourseCode";
    // 开启防偷盾
    String openPreventTou = HOST_URL + "user/openPreventTou.htm";
    /** 订单消息 */
    String findOrderLogsList = HOST_URL + "order/findOrderLogsList";
    
    // 删除购物车全部失效商品
    String deleteShoppingCartAllNoValid = HOST_URL + "order/deleteShoppingCartAllNoValid.htm"; //废弃
    /** 用户点赞 */
    String addZanUserInfo = HOST_URL + "user/addZanUserInfo"; // 废弃
    
    interface H5{
        /**下载分享地址 */
        String appdown = HOST_URL + "phone/addFriend.jsp?token=";
        /** 收支明细 */
        String userDouConsume = HOST_URL + "phone/userDouConsume.jsp?token=";
        /** 消费详情列表 */
        String userConsume = HOST_URL + "phone/userConsume.jsp?token=";
        
        
        /** 文章内容webView地址 */
        String newsDetail = HOST_URL + "phone/newsDetail.jsp?newsId=";
        /** 充值50送50活动 */
        String recharge_50 = HOST_URL + "phone/returnBackMoney.jsp";
        /** 活动规则 */
        String active_rules = HOST_URL + "phone/bannerInfo.html?type=5";
        /** 关于偷时 */
        String abboutTouTime = HOST_URL + "phone/about.html";
        /** 获取所有博文详情web页面 */
        String newsDetails = HOST_URL + "phone/newsDetails.jsp?newsId=";
        /** 店铺分享 */
        String findShopShare = HOST_URL + "phone/shopShare.jsp?shopId=";
        /** 偷时规则 */
        String touTimeRule = HOST_URL + "phone/activeRule.html?type=1";
        /** 签到活动规则 */
        String activeRule = HOST_URL + "phone/activeRule.html?type=2";
        /** 课程活动规则 */
        String courseRule = HOST_URL + "phone/activeRule.html?type=4";
        //周排行榜规则
        String weekOrder = HOST_URL + "phone/weekOrder.jsp";

        // 常见问题
        String problem0 = HOST_URL + "phone/problem0.html";
        String problem1 = HOST_URL + "phone/problem1.html";
        String problem2 = HOST_URL + "phone/problem2.html";
        String problem3 = HOST_URL + "phone/problem3.html";
        String problem4 = HOST_URL + "phone/problem4.html";
        String problem5 = HOST_URL + "phone/problem5.html";
        String problem6 = HOST_URL + "phone/problem6.html";
    }
}
