package com.aaroom.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @className HotelBase
 * @Description 这个类主要是干
 * @Author 张赢
 * @Date 2018/12/24 下午 23:50
 * @Version 1.0
 **/
@Data
public class HotelBase implements Serializable {

    //
    private Integer id;//
    //
    private String hotel_name;//
    //
    private String create_date;//
    //区域主键ID
    private String area_id;//
    //
    private String province_id;//
    //
    private String city_id;//
    //
    private String county_id;//
    //
    private String street_address;//
    //分店类型（直营、加盟）
    private String hotel_type;//
    //
    private String status;//
    //
    private String brand_id;//
    //
    private Integer flag;//
    //
    private String audit_status;//
    //
    private String create_by;//
    //
    private Date create_time;//
    //
    private String last_repair;//
    //
    private Date last_time;//
    //
    private String customer_id;//
    //
    private Integer system;//
    //
    private String start_date;//
    //
    private String qy_date;//
    //
    private String jy_date;//
    //
    private String run_date;//
    //
    private String stop_date;//
    //
    private Integer power_num;//
    //
    private String authorize_date;//
    //
    private Integer is_using;//
    //
    private Integer opt_flag;
    //线下信用住门店
    private Integer offline_credit;//
    //尚美酒店ID
    private String smhotel_id;
    //房间数
    private Integer room_num;

}
