package com.aaroom.restcontroller;

import com.aaroom.exception.RestError;
import com.aaroom.model.StrListView;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.bean.MemberInvoice;
import com.aaroom.wechat.service.MemberInvoiceService;
import com.shangmei.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InvoiceController {

   @Autowired
    private MemberInvoiceService memberInvoiceService;

   //获取发票信息列表
   @PostMapping(value = "/console/api/getMemberListByParams")
   public Object getMemberListByParams(@RequestParam(value = "province",required = false)String province,
                                       @RequestParam(value = "city",required = false)String city,
                                       @RequestParam(value = "hotel_id",required = false)String hotel_id,
                                       @RequestParam(value = "begin_time",required = false)String begin_time,
                                       @RequestParam(value = "end_time",required = false)String end_time,
                                       @RequestParam(value = "is_active",required = false)String is_active,
                                       @RequestParam(value = "pageIndex",defaultValue = "1",required = false)Integer pageIndex,
                                       @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize){
       Map params = new HashMap();
       params.put("province",province);
       params.put("city",city);
       params.put("begin_time",begin_time);
       params.put("end_time",end_time);
       if ("0".equals(hotel_id)){
           params.put("hotel_id",null);
       }else {
           params.put("hotel_id",hotel_id);
       }
       if ("2".equals(is_active)){ //查询所有
           params.put("is_active",null);
       }else {
           params.put("is_active",is_active);
       }
       List<Map<String,Object>> memberInvoiceListByParams = memberInvoiceService.getMemberInvoiceListByParams(params);

       for (Map<String,Object> map:memberInvoiceListByParams){
           int id = (int)map.get("id");//mc的id
           MemberInvoice memberInvoice = memberInvoiceService.selectByPrimaryKey(id);
           map.put("create_time",DateUtil.format(memberInvoice.getCreate_time()));
       }
       Page pageList = PageUtils.getPageList(memberInvoiceListByParams, pageIndex, pageSize);
       return pageList;
   }


    //批量修改发票通知信息
    @PostMapping(value = "/console/api/updateInvoiceByIds")
    public Object updateInvoiceByIds(@RequestBody StrListView strListView){
        List<String> ids = strListView.getIds();
        if (ids.size()>0){
            for (String id:ids){
                Map params = new HashMap();
                params.put("id",id);
                params.put("is_active",strListView.getIs_active());
                memberInvoiceService.updateByPrimaryKeyParams(params);
            }
            return RestError.E_OK;
        }
        return RestError.E_DATA_FAIL;
    }
}
