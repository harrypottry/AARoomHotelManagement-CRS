package com.shangmei.persistence;

import com.shangmei.model.RoomView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2019/1/9 0009.
 */
@Repository
public interface RoomInfoMapper {
    List<RoomView> getRoomList(@Param("shopID") String shopID,
                               @Param("roomType")String roomType,
                               @Param("roomStatus")String roomStatus);

    List<RoomView> getRoomStatusByShopID(@Param("shopID") String shopID);

    List<RoomView> getRoomTypeByShopID(@Param("shopID")String shopID);
}
