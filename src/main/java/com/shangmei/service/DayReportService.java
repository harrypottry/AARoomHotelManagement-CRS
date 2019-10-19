package com.shangmei.service;

import com.shangmei.beans.DayReport;
import com.shangmei.persistence.DayReportMapper;
import com.shangmei.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DayReportService {

    @Resource
    private DayReportMapper dayReportMapper;

    public Map<String,Object> getBusnessAnalysisReportDay(Map<String, Object> paramMap){
        return dayReportMapper.getBusnessAnalysisReportDay(paramMap);
    }

    public List<LinkedHashMap<String,Object>> getBusnessAnalysisReportOneWeek(LinkedHashMap<String, Object> paramMap){
        return dayReportMapper.getBusnessAnalysisReportOneWeek(paramMap);
    }
    public List<LinkedHashMap<String,Object>> getBusnessAnalysisReportTwoWeek(LinkedHashMap<String, Object> paramMap){
        return dayReportMapper.getBusnessAnalysisReportTwoWeek(paramMap);
    }
    public List<LinkedHashMap<String,Object>> getBusnessAnalysisReportOneMonth(LinkedHashMap<String, Object> paramMap){
        return dayReportMapper.getBusnessAnalysisReportOneMonth(paramMap);
    }
    public DayReport selectByPrimaryKey(String id){

        return dayReportMapper.selectByPrimaryKey(id);

    }
    public Map<String,Object> getBusnessAnalysisReportThisM(Map<String, Object> paramMap){
        return dayReportMapper.getBusnessAnalysisReportThisM(paramMap);
    }



    public List<LinkedHashMap<String, Object>> getDeatilMapByData(List<LinkedHashMap<String, Object>> dataMap){

        for (Map<String, Object> map:dataMap){
            Date atime = (Date) map.get("atime");
            //算出当前时间是星期几
            String s = DateUtil.WeekOfDate(atime,false);
            String s1 = DateUtil.WeekOfDate(atime, true);
            map.put("weekDay",s);
            map.put("weekDays",s1);
        }
        return dataMap;
    }
}
