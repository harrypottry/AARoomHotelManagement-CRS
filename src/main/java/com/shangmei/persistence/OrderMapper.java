package com.shangmei.persistence;

import com.shangmei.model.OrderInfoView;
import com.shangmei.model.OrderStatusView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyp on 2019/1/11 0011.
 */
@Repository
public interface OrderMapper {
    List<OrderStatusView> getAllOrderStatusList();

    List<OrderInfoView> getAllOrderList(@Param("shopID") String shopID,
                                        @Param("flag")Integer flag,
                                        @Param("orderStatus")Integer orderStatus);
}
