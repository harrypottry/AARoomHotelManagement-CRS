package com.shangmei.persistence;

import com.shangmei.beans.DayReport;

import java.util.*;

public interface DayReportMapper {
    int deleteByPrimaryKey(String id);

    int insert(DayReport record);

    int insertSelective(DayReport record);

    DayReport selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DayReport record);

    int updateByPrimaryKey(DayReport record);

    Map<String,Object> getBusnessAnalysisReportDay(Map<String, Object> paramMap);

    List<LinkedHashMap<String,Object>> getBusnessAnalysisReportOneWeek(LinkedHashMap<String, Object> paramMap);

    List<LinkedHashMap<String,Object>> getBusnessAnalysisReportTwoWeek(LinkedHashMap<String, Object> paramMap);

    List<LinkedHashMap<String,Object>> getBusnessAnalysisReportOneMonth(LinkedHashMap<String, Object> paramMap);

    Map<String,Object> getBusnessAnalysisReportThisM(Map<String, Object> paramMap);


}