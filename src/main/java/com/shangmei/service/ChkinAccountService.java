package com.shangmei.service;

import com.shangmei.persistence.ChkinAccountMapper;
import com.shangmei.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ChkinAccountService {

    @Resource
    private ChkinAccountMapper chkinAccountMapper;

    @Resource
    private DayReportService dayReportService;

    @Resource
    private ReportService reportService;

    public List<Map<String, Object>> getChkOutRefund(Map<String, Object> paramMap){
        return chkinAccountMapper.getChkOutRefund(paramMap);
    }

    public List<Map<String, Object>> getReportByTime(List<Map<String, Object>> checkMap,String SM_HotelId){
        //temp
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Map<String, Object> dataMap:checkMap){
            //data
            Double totalpercentage;
            Double totaljianye;
            String indatetime = DateUtil.format((Date) dataMap.get("indatetime"),"MM.dd");//开始时间
            String overtime = DateUtil.format((Date) dataMap.get("overtime"),"MM.dd");//结束时间
            BigDecimal disprc = (BigDecimal) dataMap.get("disprc");//房间平均价
            String rtname = (String) dataMap.get("rtname");//房间类型名
            String rTCode = (String) dataMap.get("rTCode");//房间号
            String atime = DateUtil.format((Date)dataMap.get("atime"), "yyyy年MM月dd日");//时间
            Map<String, Object> paramsday = new LinkedHashMap<>();
            paramsday.put("atime", DateUtil.format(DateUtil.addDays(DateUtil.currentDate(), 0), "yyyy-MM-dd"));
            paramsday.put("shopID", SM_HotelId);
            Map<String, Object> dayList = dayReportService.getBusnessAnalysisReportDay(paramsday);
            //判空处理
            if (dayList==null){
                totalpercentage = 0.00;
                totaljianye = 0.00;
            }else {
                totalpercentage = (Double)dayList.get("totalpercentage");  //今日入住率
                totaljianye = (Double) dayList.get("totaljianye");//今日间夜数
            }
            if(0 != ret.size()){
                //先获取当前集合中存储的所有数据
                int flag = 0 ; // 0为新增数据，1为update
                for (int i= 0;i< ret.size();i++) {
                    Map<String, Object> currentMap = ret.get(i);
                    if (String.valueOf(currentMap.get("atime")).equals(atime)){
                        List dataList = (List)currentMap.get("dataList");
                        //将剩下的dataMap中的值放入到tempMap的"dataList"中
                        Map<String, Object> detailMap = reportService.insertDetail(null,null,indatetime, overtime, disprc, rtname, rTCode,null);
                        dataList.add(detailMap);
                        flag =1;
                        continue;
                    }
                }
                if(flag == 0){//新增操作
                    Map<String, Object> tempMap = reportService.insertDetail(totalpercentage, totaljianye, indatetime, overtime, disprc, rtname, rTCode, atime);
                    ret.add(tempMap);
                    continue;
                }
            }else { //第一次将一个完整的数据放入
                Map<String, Object> tempMap = reportService.insertDetail(totalpercentage, totaljianye, indatetime, overtime, disprc, rtname, rTCode, atime);
                ret.add(tempMap);
            }
        }
        return ret;
    }
}
