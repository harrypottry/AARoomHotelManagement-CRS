package com.shangmei.restcontroller;

import com.aaroom.exception.RestError;
import com.shangmei.service.ChkinAccountService;
import com.shangmei.service.DayReportService;
import com.shangmei.service.RoomUseService;
import com.shangmei.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


@RestController
public class ReportController {

    @Autowired
    private RoomUseService roomUseService;

    @Autowired
    private ChkinAccountService chkinAccountService;

    @Autowired
    private DayReportService dayReportService;

    private final static Integer REALDAY = 50;

    @PostMapping("/wx/console/api/getDayReportListByRoom")
    public Object getDayReportListByRoom(@RequestParam(required = false,value = "begin_time") String begin_time,
                                         @RequestParam(required = false,value = "end_time") String end_time,
                                         @RequestParam(value = "SM_HotelId") String SM_HotelId) throws Exception{


        Map<String,Object> params = new HashMap<>();
        String real_time =  DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), 0), "yyyy-MM-dd");
        params.put("atime", real_time);
        params.put("shopID", SM_HotelId);
        params.put("thismonthAtimeStart", DateUtil.getCurrentMonthFirstDay(DateUtil.convertStringToDate(real_time)));
        params.put("thismonthAtimeEnd", DateUtil.getCurrentMonthLastDay(DateUtil.convertStringToDate(real_time)));
        params.put("lastmonthAtimeStart", DateUtil.getLastMonthFirstDay(DateUtil.convertStringToDate(real_time)));
        params.put("lastmonthAtimeEnd", DateUtil.getLastMonthLastDay(DateUtil.convertStringToDate(real_time)));
        params.put("thisyearAtimeStart", DateUtil.getCurrentYearFirstDay(real_time));
        params.put("thisyearAtimeEnd", DateUtil.getCurrentYearLastDay(real_time));
        params.put("lastyearAtimeStart", DateUtil.getLastYearFirstDay(DateUtil.convertStringToDate(real_time)));
        params.put("lastyearAtimeEnd", DateUtil.getLastYearLastDay(DateUtil.convertStringToDate(real_time)));


        //传参判断
        if(StringUtils.isNotEmpty(begin_time) || StringUtils.isNotEmpty(end_time)){
            params.put("myStart", DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), -1), "yyyy-MM-dd"));
            params.put("myEnd", DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), -60), "yyyy-MM-dd"));
            Long aLong = DateUtil.dateDiff(begin_time, end_time);
            if (aLong<0){
                return RestError.E_DAYREPORT_TIMEOUT;
            }
            if (aLong>60){
                return RestError.E_DAYREPORT_OVERTIME;
            }
        }else {
            params.put("myStart", begin_time);
            params.put("myEnd", end_time);
        }

        Map<String,Object> roomList = roomUseService.getRoomTypeAnalysisCountData(params);
        //Map<String, Object> roomListNotNull = roomUseService.getRoomListNotNull(roomList);



        //获取表单数据
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("shopid", SM_HotelId);
        if (StringUtils.isEmpty(begin_time)){
            paramMap.put("begin_time",DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), -61), "yyyy-MM-dd"));
            paramMap.put("end_time",DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), -1), "yyyy-MM-dd"));
        } else {
            paramMap.put("begin_time", begin_time);
            paramMap.put("end_time", end_time);
        }//不指定日期，默认给当前天最近60天数据

        //
        Map<String, Object> yesday = new LinkedHashMap<>();
        yesday.put("atime", begin_time);
        yesday.put("shopID", SM_HotelId);
        Map<String, Object> yesterdayList = dayReportService.getBusnessAnalysisReportDay(yesday); //
        //totalcount
        Double totalcount = 20.00;  //查询每日发生客房总数
        if (yesterdayList!=null){
            totalcount = (Double)yesterdayList.get("totalcount");
        }

        List<Map<String, Object>> checkMap = chkinAccountService.getChkOutRefund(paramMap);   //被解析的数据集
        List<Map<String, Object>> reportByTime = chkinAccountService.getReportByTime(checkMap,SM_HotelId);

        Map<String,Object> numMap = new HashMap<>();
        if (reportByTime.size()>0 &&checkMap.size()>0){
            //解析数据
            int sum_totaljianye =0;
            double sum_totalpercentage=0.00;
            double sum_disprc =0.00;
            DecimalFormat df = new DecimalFormat("#.0");
            for (Map<String, Object> map:reportByTime){
                List dataList =(List) map.get("dataList");
                double totaljianye = dataList.size();  //每天间夜数
                sum_totaljianye+=totaljianye;//总间夜数
                double avg=totaljianye/totalcount;  //入住率
                sum_totalpercentage+=avg; //总入住率
                map.put("totaljianye",totaljianye);
                double s = Double.valueOf(String.format("%.2f", avg)) * 100 > 100 ? 100   : Double.valueOf(String.format("%.2f", avg)) * 100 ;
                map.put("totalpercentage",String.format("%.1f", s)+"%");  //double小数点后保留两位  ??
            }

            for (Map<String, Object> priceMap:checkMap){
                BigDecimal disprc = (BigDecimal)priceMap.get("disprc");
                sum_disprc+=disprc.doubleValue(); //总房价
            }
            //

            numMap.put("thisyearrentalNum",sum_totaljianye);

            numMap.put("roomrate", Double.valueOf(String.format("%.1f", sum_totalpercentage / (double) reportByTime.size())) *100);//double小数点后保留两位 ??

            numMap.put("roomavgfee",df.format((sum_disprc/reportByTime.size())));//double小数点后保留两位
            numMap.put("rentalNum",df.format(sum_disprc/(double) reportByTime.size()*sum_totalpercentage/(double) reportByTime.size()));

        }else {
            numMap.put("thisyearrentalNum",0);
            numMap.put("roomrate",0);
            numMap.put("roomavgfee",0);
            numMap.put("rentalNum",0);
        }



        //返回结果
        Map result = new LinkedHashMap();
        result.put("roomList",numMap);  //表头数据
        result.put("checkMap",reportByTime);  //表单数据
        return new RestError(result);
    }
}
