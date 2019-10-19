package com.aaroom.utils;

/**
 * @className Page
 * @Description   分页查询--常量
 * @Author 张赢
 * @Date 2018/11/5 19:27
 * @Version 1.0
 **/

public class Constants {
    //默认显示第一页
    public static final int DEFAULT_PAGE_NO = 1;
    //默认一页30条
    public static final int DEFAULT_PAGE_SIZE = 30;

    public static final String CLOTH = "C:";

    public static final String CLOTHRFID = "CRFID:";

    public static final String CLOTHTYPEIDS = "CTIDS";

    public static final String ALLCLOTHTYPEIDS = "ACTIDS";

    public static final String CLOTHTYPE = "CT:";

    public static final String MISSION = "M:";

    public static final String HOTEL = "H:";

    public static final String TEMPIDS = "TEMPIDS:";

    public static final String RESOURCE = "RES:";

    public static final String SHANGMEIOTATOKEN = "SMT";

/*    public static final String PROVINCES ="http://120.92.148.25:8099/rest/Geo/Provinces";//尚美-省份信息接口

    public static final String CITIES ="http://120.92.148.25:8099/rest/Geo/Cities";//尚美-城市信息接口

    public static final  String HOTEL_LIST = "http://120.92.148.25:8099/rest/static/hotel/list"; //尚美-酒店列表接口

    public static final String HOTEL_INFO = "http://120.92.148.25:8099/rest/static/hotel/";

    public static final  String AVAIL = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/avail"; //查询房态

    public static final  String ORDER_SAVE = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/order/save"; //尚美-酒店下单接口

    public static final  String ORDER_QUERY = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/order/query"; //尚美-酒店查看订单详情

    public static final  String ORDER_CANCEL = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/order/cancel"; //尚美-酒店取消订单

    public static final  String HOTEL_ROOMTYPE = "http://120.92.148.25:8099/rest/static/hotel/roomtype/";

    public static final  String GET_TOKEN = "http://120.92.148.25:8099/rest/oauth/authorize"*/

    public static final  String AVAIL = "http://openapi.pms.ethank.com.cn/rest/PMSGateway/AAWECHAT/avail"; //查询房态

    public static final  String ORDER_SAVE = "http://openapi.pms.ethank.com.cn/rest/PMSGateway/AAWECHAT/order/save"; //尚美-酒店下单接口

    public static final  String ORDER_QUERY = "http://openapi.pms.ethank.com.cn/rest/PMSGateway/AAWECHAT/order/query"; //尚美-酒店查看订单详情

    public static final  String ORDER_CANCEL = "http://openapi.pms.ethank.com.cn/rest/PMSGateway/AAWECHAT/order/cancel"; //尚美-酒店取消订单

    public static final String HOTEL_INFO = "http://openapi.pms.ethank.com.cn/rest/static/hotel/";

    public static final  String HOTEL_ROOMTYPE = "http://openapi.pms.ethank.com.cn/rest/static/hotel/roomtype/";

    public static final  String GET_TOKEN = "http://openapi.pms.ethank.com.cn/rest/oauth/authorize";

    public static final  String WX_CHANNEL_NAME ="AA酒店(微信)";

    public static final String HOTELURL = "http://openapi.pms.ethank.com.cn/rest/static/hotel/";
    public enum ClothKind{

        //名称
        Type,

        //规格
        Size,

        //品牌
        Brand,

        //材质
        Material,

        //工艺
        Craft,

        //织纱
        Woven,

        //密度
        Density
    }


    public enum ClothErrorType{

        //丢失
        Lost,

        //破损
        Broken,

        //二维码破损
        QRBroken

    }

    public interface Session {

        String key = "employee_id";

    }

    public interface WechatSession {

        String key = "member_id";

    }
}
