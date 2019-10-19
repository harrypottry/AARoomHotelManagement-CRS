package com.shangmei.persistence;

import com.aaroom.beans.Chkinaccounts;
import com.aaroom.beans.HotelBase;
import com.shangmei.beans.Hotel;
import com.shangmei.beans.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by sosoda on 2018/10/23.
 */
@Repository
public interface ShangmeiMapper {
    //根据酒店id获取一个酒店
    Hotel getHotel(@Param("id") String id);

    User getUserByPhone(@Param("memberId") String memberId);

    //玩周边，根据酒店id获取周边信息


    List<Chkinaccounts> getChkinAccountsList(@Param("allHotelBase") List<HotelBase> allHotelBase,
                                             @Param("AA_BOOKSOURCE") String AA_BOOKSOURCE,
                                             @Param("fromDateTime")Date fromDateTime,
                                             @Param("toDateTime") Date toDateTime);
}
