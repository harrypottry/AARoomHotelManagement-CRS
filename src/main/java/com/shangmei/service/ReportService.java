package com.shangmei.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    //统一处理报表数据
    public Map<String, Object> insertDetail(Double totalpercentage, Double totaljianye, String indatetime, String overtime, BigDecimal disprc, String rtname, String rTCode, String atime){
        Map<String, Object> temp_Map = new LinkedHashMap<>();
        List temp_List = new LinkedList();
        Map<String, Object> detailMap = new LinkedHashMap();

        detailMap.put("indatetime",indatetime);
        detailMap.put("overtime",overtime);
        detailMap.put("disprc",disprc);
        detailMap.put("rtname",rtname);
        detailMap.put("rTCode",rTCode);

        if (totalpercentage == null|| totaljianye == null|| atime ==null){
            return detailMap;
        }else {
            temp_Map.put("totalpercentage",totalpercentage+"%");
            temp_Map.put("totaljianye", totaljianye);


            temp_List.add(detailMap);
            temp_Map.put("atime",atime);
            temp_Map.put("dataList",temp_List);
            return temp_Map;
        }
    }
}
