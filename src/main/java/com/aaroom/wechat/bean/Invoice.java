package com.aaroom.wechat.bean;

import lombok.Data;

/**
 * Created by liyp on 2019/3/14 0014.
 */
@Data
public class Invoice {
    private Integer id;

    private String member_id; //会员编号

    private String invoice_rise ;//发票抬头类型

    private String company_name;//公司名称

    private String company_number;//公司税号

}
