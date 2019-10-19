package com.aaroom.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RestError {

    public static final RestError E_OK = new RestError(0, "success");
    // Auth Errors
    public static final RestError E_AUTH_NEEDED = new RestError(20101, "使用此功能需要先登录");
    public static final RestError E_AUTH_FAILED = new RestError(20102, "账号或密码错误");
    public static final RestError E_AUTH_REPEAT = new RestError(20103, "此手机号已经注册");
    public static final RestError E_AUTH_LOGOUT = new RestError(20104, "该账号已经退出");
    public static final RestError E_AUTH_SENDMESSAGE = new RestError(20105, "发送验证码失败");

    //Data Errors
    public static final RestError E_DATA_INVALID = new RestError(30101, "无效的数据");
    public static final RestError E_DATA_EXIST = new RestError(30102, "已存在的数据");
    public static final RestError E_CLOTHID_EXIST = new RestError(30201, "布草初始化时id已存在");
    public static final RestError E_RFTID_EXIST = new RestError(30202, "布草初始化时id已存在");

    // Start Errors
    public static final RestError E_DATA_FAIL = new RestError(50100,"暂无数据");
    public static final RestError E_START_NOTIN = new RestError(50101,"当前房间不是您任务列表中的房间！");
    public static final RestError E_START_FAIL = new RestError(50102,"请完成已接受任务！");
    public static final RestError E_START_WORK = new RestError(50103,"您当前扫码脏布草不属于本房间！");
    public static final RestError E_START_DirCloth = new RestError(50104,"您当前扫码为本房间脏布草！");
    public static final RestError E_START_UNCloth = new RestError(50105,"当前扫码布草类型不匹配！");
    public static final RestError E_START_SAME = new RestError(50106,"您已经配置过此布草了！");
    public static final RestError E_START_FINISH = new RestError(50107,"此房间任务配置布草数量不匹配！");
    public static final RestError E_START_FINAL = new RestError(50108,"您当前扫码房间不正确，请检查！");
    public static final RestError E_START_OVERFLOWING = new RestError(50109,"您当前数量超过上限，请检查！");
    public static final RestError E_START_HOTELWRONG = new RestError(50110,"您当前扫码非本酒店！");
    public static final RestError E_MISSION_ASSIONFAIL = new RestError(50111, "任务无法重新派发");
    public static final RestError E_CLOTHS_OTHERS = new RestError(50112, "当前布草不属于你");
    public static final RestError E_DAYREPORT_OVERTIME = new RestError(50113, "当前选择时间超出限定范围！");
    public static final RestError E_DAYREPORT_TIMEOUT = new RestError(50114, "开始时间小于结束时间！");

    //睡得香 二维码
    public static final RestError E_CODE_EXIST = new RestError(40102, "此二维码已经消费");

    //睡得香 登录
    public static final RestError E_USERCODE_SUCCEED = new RestError(40202, "用户名可以注册");

    //睡得香 注册
    public static final RestError E_REGISTERCODE_FAIL = new RestError(40302, "注册失败");

    //睡得香 校验验证码
    public static final RestError E_COMPARE_FAIL = new RestError(40402, "验证码错误");

    //睡得香 重置密码失败
    public static final RestError E_PASSWORD_FAIL = new RestError(50401, "修改密码失败");

    //睡得香 验证状态
    public static final RestError E_CONFIRMSTATUS_FAIL = new RestError(40502, "用户未登录");

    //clothlog 扫描验证状态
    public static final RestError E_TRANSACTIONAL_FAIL = new RestError(50201, "扫描失败");

    //clothlog 获取新旧布草信息失败
    public static final RestError E_CLOTHMESSAGE_FAIL = new RestError(50202, "获取布草信息失败");
    //clothlog 获取布草瞬时状态
    public static final RestError E_CLOTHPOSSESSORTYPE_FAIL = new RestError(50302, "该类型下暂无数据");
    //clothlog 修改上下班失败
    public static final RestError E_WORKING_FAIL = new RestError(50402, "操作失败,请重新操作");

    public static final RestError E_WORKING_OTHER = new RestError(50403, "请将布草归还库管!");

    public static final RestError E_WORKING_MISSION = new RestError(50404, "您有未完成的任务!");

    public static final RestError E_CLOTHERROR_FAIL = new RestError(50405, "报损失败请重新操作!");

    public static final RestError E_PICTURE_FAIL = new RestError(50406, "上传图片失败!");

    public static final RestError E_RETURN_FAIL = new RestError(50407, "您的交回的布草当前状态不在保洁员名下!");

    public static final RestError E_WASHFACTORY_FAIL = new RestError(50407, "您的交回的布草当前状态不在洗衣厂名下!");

    public static final RestError E_STOREHOUSE_FAIL = new RestError(50408, "您要送洗的布草当前状态不在库房名下!");

    public static final RestError E_CLOTHIDMISS_FAIL = new RestError(50409, "您扫描的布草id不存在!");

    public static final RestError E_CLOTHIERRORTYPE_FAIL = new RestError(50410, "您在进行报损操作，请选择报损类型!");



    //c端小程序
    public static final RestError E_WECHAT_COLLECTED = new RestError(70000, "您已经收藏过了！");
    public static final RestError E_WECHAT_UNCOLLECT = new RestError(70001, "您尚未收藏过此酒店！");


    protected int errorCode;
    protected String errorDesc;
    protected Map<String, String> detail;
    public Object data;

    public RestError(){

    }

    public RestError(int c, String d) {
        this.errorCode = c;
        this.errorDesc = d;
    }

    public RestError detail(String p, String e) {
        if (detail == null)
            detail = new HashMap<String,String>();
        detail.put(p, e);
        return this;
    }

    public RestError clone() {
        RestError t = new RestError();
        t.errorCode = this.errorCode;
        t.errorDesc = this.errorDesc;
        return t;
    }

    //为了成功方便返回，做了一个统一的构造
    public RestError(Object obj){
        this.errorCode = 0;
        this.errorDesc = "success";
        this.data = obj;
    }

    //为了成功方便返回，做了一个统一的构造(含错误码 描述)
    public RestError(Object obj,int errorCode,String errorDesc){
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.data = obj;
    }



}
