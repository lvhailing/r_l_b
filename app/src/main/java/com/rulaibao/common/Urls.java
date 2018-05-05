package com.rulaibao.common;


public class Urls {
    //测试环境
    public static String URL_DEBUG = "http://192.168.1.82:93/";

    // 外网测试环境
    public static String URL_OUTER_NET_DEBUG = "http://123.126.102.219:30093/";

    // 正式环境IP
//    public static String URL_OFFICIAL = "https://hwapp.cf360.com/";
    public static String URL_OFFICIAL = "https://app.jdjufu.com/";  //( 再发版时用这个新的正式地址)

    //代树理
    public static final String URL_DSL = "http://192.168.1.106:9999/rulaibao-app/";

    //张亚磊
    public static final String URL_ZYL = "http://192.168.1.193:9999/rulaibao-app/";

    // 邢玉洁
    public static final String URL_XYJ = "http://192.168.1.125:9999/rulaibao-app/";

    // 冯艳敏
    public static final String URL_FYM = "http://192.168.1.164:8887/rulaibao-app/";

    // 沈楠
    public static final String URL_SN = "http://192.168.1.138:9999/rulaibao-app/";

    // 调试，上线时只需改此处环境即可
    private static String EC_HOST = URL_SN;


    /**
     * 注册
     */
    public static final String URL_SIGN_UP = EC_HOST + "android/register";
    /**
     * 登出
     */
    public static final String URL_LOGINOFF = EC_HOST + "android/logoff";
    /**
     * 找回密码
     */
    public static final String URL_FINDPASSWORD = EC_HOST + "password/find";
    /**
     * 发送验证码
     */
    public static final String URL_SENDSMS = EC_HOST + "user/mobile/send/verifycode";

    /**
     * url
     */
    public static final String URL = EC_HOST + "";


    /**
     * 登陆
     */

    public static final String URL_LOGIN = EC_HOST + "android/login";

    // icon_mine_setting--服务协议
    public static final String URL_SERVICE_AGREEMENT = EC_HOST + "register/agreement";


    /**
     * 短信类型
     */
    public static final String REGISTER = "register";       //  用户注册

    public static final String LOGINRET = "loginRet";       //  登录密码找回

    public static final String MOBILEBIND = "mobileBind";       //  绑定手机

    public static final String MOBILEEDIT = "mobileEdit";       //  修改手机

    public static final String ADDBANKCARD = "bankCardBind";       //  添加银行卡

    public static final String WITHDRAW = "withdrawCashNum";       //  提现


    // */ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态

    public static final String ACTIVITY_SPLASH = "activity_splash";
    public static final String ACTIVITY_GESEDIT = "activity_gesedit";
    public static final String ACTIVITY_GESVERIFY = "activity_gesverify";
    public static final String ACTIVITY_ACCOUNTSET = "activity_accountset";
    public static final String ACTIVITY_ACCOUNT = "activity_account";
    public static final String ACTIVITY_CHANGE_GESTURE = "activity_change_gesture";

    /************************************************* 研修模块start *****************************************************************/

    // 研修--首页 课程展示
    public static final String URL_TRAINING_RESEARCH = EC_HOST + "research/index";

    // 研修首页 热门问答
    public static final String URL_TRAINING_HOT_ASK = EC_HOST + "appQuestion/hot/list";

    // 研修首页 - 换一换
    public static final String URL_TRAINING_REFRESH = EC_HOST + "research/change";

    // 研修--课程首页
    public static final String URL_TRAINING_CLASS_LIST = EC_HOST + "appCourse/index";

    // 研修圈子首页
    public static final String URL_TRAINING_CIRCLE_INDEX = EC_HOST + "appCircle/index";

    // 研修圈子详情
    public static final String URL_TRAINING_CIRCLE_DETAILS = EC_HOST + "appCircle/detail";

    // 研修圈子详情话题列表
    public static final String URL_TRAINING_CIRCLE_DETAILS_TOPIC = EC_HOST + "appTopic/list";

    // 研修圈子详情话题详情
    public static final String URL_TRAINING_CIRCLE_DETAILS_TOPIC_DETAILS = EC_HOST + "appTopic/detail";

    // 研修圈子详情话题详情评论列表
    public static final String URL_TRAINING_CIRCLE_COMMENT_LIST = EC_HOST + "appTopic/comment/list";

    // 研修圈子详情话题详情回复评论
    public static final String URL_TRAINING_CIRCLE_REPLY = EC_HOST + "appTopic/comment";

    // 研修圈子详情话题详情点赞
    public static final String URL_TRAINING_CIRCLE_LIKE = EC_HOST + "appTopic/like";

    // 研修圈子发布话题
    public static final String URL_TRAINING_CIRCLE_ADD_TOPIC = EC_HOST + "appTopic/add";

    // 研修圈子设置权限
    public static final String URL_TRAINING_CIRCLE_SET_AUTHORITY = EC_HOST + "appCircle/set";

    // 研修圈子话题置顶或者取消
    public static final String URL_TRAINING_CIRCLE_SET_TOP = EC_HOST + "appTopic/top";

    // 加入圈子
    public static final String URL_TRAINING_CIRCLE_JOIN = EC_HOST + "appCircle/apply/join";

    // 退出圈子
    public static final String URL_TRAINING_CIRCLE_OUT = EC_HOST + "appCircle/apply/out";

    // 问答类型
    public static final String URL_TRAINING_ASK_TYPE = EC_HOST + "appQuestion/type";

    // 问答首页
    public static final String URL_TRAINING_ASK_INDEX = EC_HOST + "appQuestion/list";


    // 问答详情
    public static final String URL_TRAINING_ASK_DETAILS = EC_HOST + "appQuestion/detail";

    // 问答详情回答列表
    public static final String URL_TRAINING_ASK_DETAILS_ANSWER = EC_HOST + "appQuestion/appAnswer/list";

    // 回答详情
    public static final String URL_TRAINING_ANSWER_DETAILS = EC_HOST + "appQuestion/appAnswer/detail";

    // 问答详情评论列表
    public static final String URL_TRAINING_ASK_DETAILS_ANSWER_COMMENT = EC_HOST + "appQuestion/appAnswer/comment/list";

    // 我要提问
    public static final String URL_TRAINING_TOASK = EC_HOST + "appQuestion/question";

    // 我要回答
    public static final String URL_TRAINING_TOANSWER = EC_HOST + "appQuestion/answer";

    // 研修问题回复
    public static final String URL_TRAINING_ANSWER_REPLY = EC_HOST + "appQuestion/answer/comment";

    // 研修回答详情点赞
    public static final String URL_TRAINING_ANSWER_LIKE = EC_HOST + "appQuestion/answer/like";

    // 研修课程详情--简介
    public static final String URL_TRAINING_CLASS_DETAILS_DESC = EC_HOST + "appCourse/detail/content";

    // 研修课程详情--目录
    public static final String URL_TRAINING_CLASS_DETAILS_CATALOG = EC_HOST + "appCourse/detail/outline";

    // 研修课程详情--研讨
    public static final String URL_TRAINING_CLASS_DETAILS_DISCUSS = EC_HOST + "appCourse/detail/comment";

    // 研修课程详情--研讨 回复
    public static final String URL_TRAINING_CLASS_DETAILS_DISCUSS_REPLY = EC_HOST + "appCourse/detail/comment/add";

    // 研修课程详情--PPT
    public static final String URL_TRAINING_CLASS_DETAILS_PPT = EC_HOST + "appCourse/detail/ppt";


    /************************************************* 研修模块end *****************************************************************/
    //首页
    public static final String URL_INDEX = EC_HOST + "product/index/list";

    // 首页--轮播图
    public static final String URL_HOME_ADVERTISE = EC_HOST + "home/advertise";

    // 保险产品列表
    public static final String URL_INSURANCE_PRODUCT = EC_HOST + "product/list";

    // 保险产品列表--搜索
    public static final String URL_INSURANCE_PRODUCT_SEARCH = EC_HOST + "product/nonsort/list";

    // 计划书列表
    public static final String URL_PLAN = EC_HOST + "product/prospectus/list";

    // 计划书--搜索
    public static final String URL_PLAN_SEARCH = EC_HOST + "product/prospectus/nonsort/list";

    // 首页--保单规划列表
    public static final String URL_POLICY_PLAN = EC_HOST + "account/order/plan/list";

    // 保险详情
    public static final String URL_INSURANCE_DETAILS = EC_HOST + "product/detail";

    // 保险详情---收藏取消收藏
    public static final String URL_COLLECTION = EC_HOST + "collection/update";

    // 保险详情---预约
    public static final String URL_APPOINTMENT_ADD = EC_HOST + "appointment/add";


//==================================================== 我的模块接口 =======================================================
    //获取我的信息
    public static final String URL_MINE_DATA = EC_HOST + "account/index";

    //获取个人信息/销售认证等两个页面数据
    public static final String URL_APP_USER_INFO = EC_HOST + "account/appUserInfo";

    //(销售认证页)提交认证
    public static final String URL_APP_USER_INFO_SUBMIT = EC_HOST + "account/appUserInfo/submit";

    // 我的模块 未读消息数
    public static final String URL_MESSAGE_TYPE_COUNT = EC_HOST + "message/type/count";

    // 我的模块 消息列表
    public static final String URL_MESSAGE_LIST = EC_HOST + "message/list";

    //上传图片
    public static final String URL_SUBMIT_PHOTO = EC_HOST + "android/account/photo/upload";

   //获取推荐主页信息
    public static final String URL_ACCOUNT_SET_RECOMMENDAppTo = EC_HOST + "account/set/recommendAppTo";

   //推荐记录
    public static final String URL_ACCOUNT_SET_RECOMMEND_LIST = EC_HOST + "account/set/recommendList";

    //(累计佣金)交易记录
    public static final String URL_ACCOUNT_TRADE_RECORD = EC_HOST + "account/appTradeRecord/list";

    // 交易记录-明细
    public static final String URL_ACCOUNT_TRADE_RECORD_DETAIL = EC_HOST + "account/appTradeRecord/detail";

    // 保单列表
    public static final String URL_ACCOUNT_ORDER_LIST = EC_HOST + "account/order/list";

    // 保单详情
    public static final String URL_ACCOUNT_ORDER_DETAIL = EC_HOST + "account/order/detail";

    // 续保提醒列表
    public static final String URL_ACCOUNT_ORDER_RENEWAL_LIST = EC_HOST + "account/order/renewal/list";

    // 预约列表
    public static final String URL_ACCOUNT_APPOINTMENT_LIST = EC_HOST + "appointment/list";

    // 我的提问列表
    public static final String URL_MY_ASK_LIST = EC_HOST + "appQuestion/my/list";

    // 我参与的（提问列表）
    public static final String URL_APPQUESTION_MYJOIN_LIST = EC_HOST + "appQuestion/myJoin/list";

    // 我的话题列表
    public static final String URL_MY_TOPIC_LIST = EC_HOST + "appTopic/my/list";

    // 平台公告 列表
    public static final String URL_BULLETIN_LIST = EC_HOST + "bulletin/list";

    // 联系客服
    public static final String URL_FEEDBACK_ADD = EC_HOST + "feedback/add";










//==================================================== 我的模块接口 =======================================================


}