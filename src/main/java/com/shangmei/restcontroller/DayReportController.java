package com.shangmei.restcontroller;

import com.aaroom.exception.RestError;
import com.shangmei.service.DayReportService;
import com.shangmei.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tips:
 * 除非登录人是AA业务人员以及root，其他角色需要关联AA酒店ID
 * others,AA酒店与尚美酒店若是同一个酒店，则需要在hotel_base进行1=1关联
 */
@RestController
public class DayReportController {

    @Autowired
    private DayReportService dayReportService;

    @PostMapping("/wx/console/api/getDayReportListByEmpId")
    public Object getDayReportListByEmpId(@RequestParam(required = false,value = "select_time") String select_time,
                                      @RequestParam(value = "SM_HotelId") String SM_HotelId)throws Exception {

            //处理日期
            String real_time = StringUtils.isNotBlank(select_time) ?
                    select_time : DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), 0), "yyyy-MM-dd");
            String yes_time = DateUtil.format(DateUtil.addDateDays(real_time, -1), "yyyy-MM-dd");
            String onemonth = DateUtil.format(DateUtil.addDateDays(real_time, -40), "yyyy-MM-dd");
            String thismonthAtimeStart = DateUtil.getCurrentMonthFirstDay(DateUtil.convertStringToDate(real_time));
            String thismonthAtimeEnd = DateUtil.getCurrentMonthLastDay(DateUtil.convertStringToDate(real_time));


            Map<String, Object> tempMap = new LinkedHashMap();
            //dataSet
            Map<String,Object> paramsThisM = new HashMap<>();
            paramsThisM.put("thismonthAtimeStart", thismonthAtimeStart);
            paramsThisM.put("thismonthAtimeEnd", thismonthAtimeEnd);
            paramsThisM.put("SM_HotelId", SM_HotelId);
            Map<String,Object> thismList = dayReportService.getBusnessAnalysisReportThisM(paramsThisM);
            if (thismList!=null){
                Double totalAmount = (Double)thismList.get("totalAmount");  //本月总营收
                Double totaljianye = (Double)thismList.get("totaljianye"); //本月间夜数
                tempMap.put("totalAmount",totalAmount);
                tempMap.put("totaljianye",totaljianye);
            }

            Map<String, Object> yesday = new LinkedHashMap<>();
            yesday.put("atime", yes_time);
            yesday.put("shopID", SM_HotelId);
            Map<String, Object> yesterdayList = dayReportService.getBusnessAnalysisReportDay(yesday); //获取昨天数据
            if (yesterdayList!=null){
                Double avgRoomPriceYes = (Double)yesterdayList.get("avgRoomPrice");//昨日平均房价
                Double paymoneyTotal = (Double)yesterdayList.get("paymoneyTotal");//昨日营业额
                Double totalpercentage = (Double)yesterdayList.get("totalpercentage");  //昨日日入住率
                Double avgRoomPrice = avgRoomPriceYes * totalpercentage*0.01;//昨日平均有效房价
                //String format = df.format(avgRoomPrice);
                tempMap.put("avgRoomPriceYes",avgRoomPriceYes);
                tempMap.put("paymoneyTotal",paymoneyTotal);
                tempMap.put("totalpercentage",totalpercentage);
                tempMap.put("avgRoomPrice",String.format("%.1f",avgRoomPrice));
            }

            LinkedHashMap<String, Object> onemonthday = new LinkedHashMap<>();
            onemonthday.put("shopID", SM_HotelId);
            onemonthday.put("onemonth", onemonth);
            onemonthday.put("real_time", real_time);
            List<LinkedHashMap<String, Object>> onemonthList = dayReportService.getBusnessAnalysisReportOneMonth(onemonthday);//获取前一个月的数据

            List<LinkedHashMap<String, Object>> onemonthList_order= dayReportService.getDeatilMapByData(onemonthList);

            //封装数据
            Map<String, Object> result = new LinkedHashMap();
            result.put("yesterDayData", tempMap);
            result.put("onemonthData", onemonthList_order);
            //返回结果
            return new RestError(result);
    }
}
