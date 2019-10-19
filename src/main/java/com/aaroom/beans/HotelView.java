package com.aaroom.beans;

import lombok.Data;

import java.util.List;

@Data
public class HotelView {

    private ShopBase shopBase;

    private List<ShopContacts> shopContactsList;

    private List<ShopTraffic> shopTrafficList;

    private List<ShopService> shopWifiList;

    private List<Integer> shop_service;

    private List<Integer> pay_method;

    private List<Integer> channel;

}
