package com.aaroom.wechat.controller;

import com.aaroom.beans.ShopBase;
import com.aaroom.exception.RestError;
import com.aaroom.service.MemberCrsService;
import com.aaroom.service.ShopBaseService;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.bean.*;
import com.aaroom.wechat.bean.model.MemberInfoView;
import com.aaroom.wechat.service.*;
import com.aaroom.wechat.service.MemberAddressService;
import com.aaroom.wechat.service.MemberCollectionService;
import com.aaroom.wechat.service.MemberInfoService;
import com.aaroom.wechat.service.MemberInvoiceService;
import com.aaroom.wechat.service.MemberPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * C端小程序后台接口命名规则：
 * /WeChat/console/api/+XXX
 */
@RestController
public class MemberController {

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private MemberInvoiceService memberInvoiceService;

    @Autowired
    private MemberPassengerService memberPassengerService;

    @Autowired
    private MemberCollectionService memberCollectionService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberCommentService memberCommentService;

    @Autowired
    private WechatOrderService wechatOrderService;

    @Autowired
    private MemberCrsService memberCrsService;

    @Autowired
    private ShopBaseService shopBaseService;


    /**
     * 添加地址（只加一个）
     * @param memberAddress
     * @return
     */
    @PostMapping("/WeChat/console/api/insertMemberAddress")
    public Object insertMemberAddress(@RequestBody MemberAddress memberAddress){
        memberAddressService.insertMemberAddress(memberAddress);
        //同时增加会员信息-省，市
        Map params = new HashMap();
        params.put("id",memberAddress.getMember_id());
        params.put("province",memberAddress.getProvince());
        params.put("city",memberAddress.getCity());
        memberInfoService.updateMemberCityAndProvince(params);
        return RestError.E_OK;
    }

    /**
     * 获取地址（只有一个）
     * @return
     */
    @PostMapping("/WeChat/console/api/getAllMemberAddress")
    public Object getAllMemberAddress(@RequestParam(value = "member_id")String member_id){
        List<MemberAddress> allMemberAddress = memberAddressService.getAllMemberAddress(member_id);
        if (allMemberAddress.size()>0){
            return allMemberAddress;
        }
        return RestError.E_DATA_FAIL;
    }

    //修改地址
    @PostMapping("/WeChat/console/api/updateMemberAddressById")
    public Object updateMemberAddressById(@RequestBody MemberAddress memberAddress){
        MemberAddress ma = memberAddressService.selectByPrimaryKey(memberAddress.getId());
        if (ma==null){
            return RestError.E_DATA_INVALID;
        }
        ma.setProvince(memberAddress.getProvince());
        ma.setCity(memberAddress.getCity());
        ma.setArea(memberAddress.getArea());
        ma.setAddress_detail(memberAddress.getAddress_detail());
        ma.setName(memberAddress.getName());
        ma.setPhone(memberAddress.getPhone());
        ma.setPostal_code(memberAddress.getPostal_code());
        memberAddressService.updateByPrimaryKeySelective(ma);
        return RestError.E_OK;

    }

    //删除地址
    @PostMapping("/WeChat/console/api/deleteMemberAddressById")
    public Object deleteMemberAddressById(@RequestParam(value = "id")Integer id){
        if (memberAddressService.selectByPrimaryKey(id)==null){
            return RestError.E_DATA_INVALID;
        }
        memberAddressService.deleteByPrimaryKey(id);
        return RestError.E_OK;
    }

    /**
     *添加发票（只加一个）
     * @param memberInvoice
     * @return
     */
    @PostMapping("/WeChat/console/api/insertMemberInvoice")
    public Object insertMemberInvoice(@RequestBody MemberInvoice memberInvoice){
        memberInvoiceService.insertMemberInvoice(memberInvoice);
        return RestError.E_OK;
    }

    /**
     * 获取发票（只有一个）
     * @return
     */
    @PostMapping("/WeChat/console/api/getAllMemberInvoice")
    public Object getAllMemberInvoice(@RequestParam(value = "member_id")String member_id){
        List<MemberInvoice> allMemberInvoice = memberInvoiceService.getAllMemberInvoice(member_id);
        if (allMemberInvoice.size()>0){
            return allMemberInvoice;
        }
        return RestError.E_DATA_FAIL;
    }

    //修改发票
    @PostMapping("/WeChat/console/api/updateMemberInvoiceById")
    public Object updateMemberInvoiceById(@RequestBody MemberInvoice memberInvoice){
        MemberInvoice mi = memberInvoiceService.selectByPrimaryKey(memberInvoice.getId());
        if (mi == null) {
            return RestError.E_DATA_INVALID;
        }
        mi.setCompany_name(memberInvoice.getCompany_name());
        mi.setCompany_number(memberInvoice.getCompany_number());
        mi.setInvoice_rise(memberInvoice.getInvoice_rise());
        memberInvoiceService.updateByPrimaryKeySelective(mi);
        return RestError.E_OK;
    }

    //删除发票
    @PostMapping("/WeChat/console/api/deleteMemberInvoiceById")
    public Object deleteMemberInvoiceById(@RequestParam(value = "id")Integer id){
        MemberInvoice mi = memberInvoiceService.selectByPrimaryKey(id);
        if (mi == null) {
            return RestError.E_DATA_INVALID;
        }
        memberInvoiceService.deleteByPrimaryKey(id);
        return RestError.E_OK;
    }


    /**
     * 添加常用住客
     * @param memberPassenger
     * @return
     */
    @PostMapping("/WeChat/console/api/insertMemberPassenger")
    public Object insertMemberPassenger(@RequestBody MemberPassenger memberPassenger){
        memberPassengerService.insertMemberPassenger(memberPassenger);
        return RestError.E_OK;
    }

    /**
     *获取常用住客
     * @return
     */
    @PostMapping("/WeChat/console/api/getAllMemberPassenger")
    public Object getAllMemberPassenger(@RequestParam(value = "member_id")String member_id){
        List<MemberPassenger> allMemberPassenger = memberPassengerService.getAllMemberPassenger(member_id);
        if (allMemberPassenger.size()>0){
            return allMemberPassenger;
        }
        return RestError.E_DATA_FAIL;
    }

    //修改常用住客
    @PostMapping("/WeChat/console/api/updateMemberPassengerById")
    public Object updateMemberPassengerById(@RequestBody MemberPassenger memberPassenger){
        MemberPassenger mp = memberPassengerService.selectByPrimaryKey(memberPassenger.getId());
        if (mp == null){
            return RestError.E_DATA_INVALID;
        }
        mp.setCard_number(memberPassenger.getCard_number());
        mp.setCard_type(memberPassenger.getCard_type());
        mp.setEmail(memberPassenger.getEmail());
        mp.setName(memberPassenger.getName());
        mp.setPhone(memberPassenger.getPhone());
        memberPassengerService.updateByPrimaryKeySelective(mp);
        return RestError.E_OK;
    }

    //删除常用住客
    @PostMapping("/WeChat/console/api/deleteMemberPassengerById")
    public Object deleteMemberPassengerById(@RequestParam(value = "id")Integer id){
        MemberPassenger mp = memberPassengerService.selectByPrimaryKey(id);
        if (mp == null){
            return RestError.E_DATA_INVALID;
        }
        memberPassengerService.deleteByPrimaryKey(id);
        return RestError.E_OK;
    }

    /**
     * 收藏酒店
     * @param hotel_id
     * @return
     */
    @PostMapping("/WeChat/console/api/collectHotel")
    public Object collectHotel(@RequestParam(value = "member_id")String member_id,
                               @RequestParam(value = "hotel_id")String hotel_id){
        //判断是否已经收藏过了
        Map<String,String> params = new HashMap();
        params.put("hotel_id",hotel_id);
        params.put("member_id",member_id);

        List<MemberCollection> memberCollectionByEntity = memberCollectionService.getMemberCollectionByEntity(params);
        if (memberCollectionByEntity.size()>0){
            return RestError.E_WECHAT_COLLECTED;
        }
        memberCollectionService.insert(new MemberCollection(hotel_id,member_id));
        return RestError.E_OK;
    }

    /**
     * 取消收藏酒店
     * @param hotel_id
     * @return
     */
    @PostMapping("/WeChat/console/api/collectHotelDel")
    public Object collectHotelDel(@RequestParam(value = "member_id")String member_id,
                                  @RequestParam(value = "hotel_id")String hotel_id){
       //判断是否处于收藏状态
       Map params = new HashMap();
       params.put("member_id",member_id);
       params.put("hotel_id",hotel_id);

        List<MemberCollection> mc = memberCollectionService.getMemberCollectionByEntity(params);
        if (mc == null){
           return RestError.E_WECHAT_UNCOLLECT;
       }
        for (MemberCollection memberCollection:mc){
            memberCollectionService.deleteByEntity(memberCollection);
        }
       return RestError.E_OK;
    }

    //获取收藏列表
    @PostMapping("/WeChat/console/api/getCollectedHotelList")
    public Object getCollectedHotelList(@RequestParam(value = "member_id")String member_id,
                                        @RequestParam(value = "lon",required = false)String lon,
                                        @RequestParam(value = "lat",required = false)String lat) {
        Map params = new HashMap();
        params.put("member_id", member_id);
        List<MemberCollection> list = memberCollectionService.getMemberCollectionByEntity(params);

        if (list.size()>0){
            List<String> l = new ArrayList<>();
            for (MemberCollection memberCollection:list){
                l.add(memberCollection.getHotel_id());  //所有收藏的酒店id
            }
            List my_list = (List)memberCollectionService.getHotelInfoByHotelId(l, lon, lat);
            return my_list;
        }
        return RestError.E_DATA_INVALID;
    }

    //判断酒店状态是否收藏
    @PostMapping("/WeChat/console/api/isCollectedHotel")
    public Object isCollectedHotel(@RequestParam(value = "member_id")String member_id,
                                  @RequestParam(value = "hotel_id")String hotel_id){
        Map params = new HashMap();
        params.put("hotel_id",hotel_id);
        params.put("member_id",member_id);
        List memberCollectionByEntity = memberCollectionService.getMemberCollectionByEntity(params);
        if (memberCollectionByEntity.size()<=0){
            return RestError.E_WECHAT_UNCOLLECT;  //尚未收藏
        }
        return RestError.E_WECHAT_COLLECTED; //收藏状态
    }

    /**
     *常住酒店（取常住的酒店前十名数据）
     */
    @PostMapping("/WeChat/console/api/getUsuallyHotelInfo")
    public Object getUsuallyHotelInfo(@RequestParam(value = "member_id")String member_id,
                                      @RequestParam(value = "lon",required = false)String lon,
                                      @RequestParam(value = "lat",required = false)String lat){
        Map params = new HashMap();
        params.put("member_id",member_id);
        params.put("status","已入住");
        List<Order> orderList = wechatOrderService.getUsuallyHotelInfo(params);//获取订单信息

        if (orderList.size()>0){
            Map<String, Integer> ret = wechatOrderService.getUsuallyHotelInfoByEmpAndStatus(orderList);//<酒店id，数量>前十名

            List<String> list = new ArrayList<>();
            for (String hotel_id:ret.keySet()){
                list.add(hotel_id);
            }
            return memberCollectionService.getHotelInfoByHotelId(list, lon, lat);
        }
        return RestError.E_DATA_FAIL;
    }

    /**
     * 酒店评论
     */
    //插入评论
    @PostMapping("/WeChat/console/api/insertMemberComment")
    public Object insertMemberComment(@RequestBody MemberComment memberComment){
        memberComment.setIs_active((byte)0);
        memberCommentService.insertSelective(memberComment);
        wechatOrderService.updateOrderStatusFinally(memberComment.getOrder_id());
        return RestError.E_OK;
    }
    //删除评论
    @PostMapping("/WeChat/console/api/deleteMemberComment")
    public Object deleteMemberComment(Integer id){
        memberCommentService.deleteByPrimaryKey(id);
        return RestError.E_OK;
    }

    //查看本酒店的所有评论
    @PostMapping("/WeChat/console/api/selectMemberCommentList")
    public Object selectMemberCommentList(@RequestParam(value = "hotel_id") String hotel_id,
                                          @RequestParam(value = "star_level",required = false,defaultValue = "0") String star_level,
                                          @RequestParam(value = "pageIndex",defaultValue = "1",required = false)Integer pageIndex,
                                          @RequestParam(value = "pageSize",defaultValue = "10",required = false)Integer pageSize){
        //0 传所有， 1 传好评 2 传中评 3 传差评
        Map<String,Object> finalMap = new LinkedHashMap();
        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> tempMap ;


        Map params = new HashMap();
        params.put("hotel_id",hotel_id);
        params.put("is_active",(byte)1);
        List<MemberComment> mcList = memberCommentService.getMemberCommentByParams(params);

        if (mcList.size()>0){
            int count_star = 0;
            for (MemberComment mc:mcList){
                String utility = mc.getUtility();  //1 2 3 4 5
                try {
                    int a = Integer.parseInt(utility);
                    count_star+=a;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String commentByUtility = memberCommentService.getCommentByUtility(utility);//评分
                if (star_level.equals("0")){  //所有
                    tempMap = new HashMap();
                    tempMap.put("star_level",commentByUtility);
                    tempMap.put("star",utility);
                    tempMap.put("memberCommentDetail",mc);
                    tempMap.put("memberCardNum",memberInfoService.getMemberById(mc.getMember_id()).getCardNum());//会员卡号
                    result.add(tempMap);
                }else if (star_level.equals("1")){  //好评
                    tempMap = new HashMap();
                    if ("5".equals(utility) || "4".equals(utility)){
                        tempMap.put("star_level",commentByUtility);
                        tempMap.put("star",utility);
                        tempMap.put("memberCommentDetail",mc);
                        tempMap.put("memberCardNum",memberInfoService.getMemberById(mc.getMember_id()).getCardNum());//会员卡号
                        result.add(tempMap);
                    }
                }else if (star_level.equals("2")){
                    tempMap = new HashMap();
                    if ("3".equals(utility) || "2".equals(utility)){
                        tempMap.put("star",utility);
                        tempMap.put("memberCommentDetail",mc);
                        tempMap.put("memberCardNum",memberInfoService.getMemberById(mc.getMember_id()).getCardNum());//会员卡号
                        result.add(tempMap);
                    }
                }else {//差评
                    if ("1".equals(utility)){
                        tempMap = new HashMap();
                        tempMap.put("star",utility);
                        tempMap.put("memberCommentDetail",mc);
                        tempMap.put("memberCardNum",memberInfoService.getMemberById(mc.getMember_id()).getCardNum());//会员卡号
                        result.add(tempMap);
                    }
                }
            }
            Page pageList = PageUtils.getPageList(result, pageIndex, pageSize);
            finalMap.put("avg_star",count_star/mcList.size());
            finalMap.put("data",pageList);
            return finalMap;
        }
        return RestError.E_DATA_FAIL;
    }

    //查看本人的所有评论
    @PostMapping("/WeChat/console/api/getMemberCommentByEmp")
    public Object getMemberCommentByEmp(@RequestParam(value = "member_id") String member_id,
                                        @RequestParam(value = "lon",required = false)String lon,
                                        @RequestParam(value = "lat",required = false)String lat){
        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> tempMap ;

        Map params = new HashMap();
        params.put("member_id",member_id);
        List<MemberComment> mcList = memberCommentService.getMemberCommentByParams(params);

        if (mcList.size()>0){
            for (MemberComment mc:mcList){
                tempMap = new HashMap();
                String hotel_id = mc.getHotel_id();
                String commentByUtility = memberCommentService.getCommentByUtility(mc.getUtility());
                Map map = new HashMap();
                map.put("smhotel_code",hotel_id);
                map.put("is_active",(byte)1);
                ShopBase shopBase = shopBaseService.selectByParams(map);
                double distanceD = memberCollectionService.getHotelInfo(lon, lat, hotel_id);

                tempMap.put("comment",commentByUtility);
                tempMap.put("hotelName",shopBase.getShop_name());
                tempMap.put("areaName",shopBase.getProvince());
                tempMap.put("pic",null);
                tempMap.put("distance",distanceD);
                tempMap.put("memberCommentDetail",mc);
                result.add(tempMap);
            }
            return result;
        }
        return RestError.E_DATA_FAIL;
    }


    //获取会员权益
    @GetMapping(value = "/WeChat/console/api/getMemberEquity")
    public MemberLevel getMemberEquity(@RequestParam String memberId) {
        return memberInfoService.getMemberEquity(memberId);
    }

    //会员积分升级
    @GetMapping(value = "/WeChat/console/api/memberPromote")
    public Map<String, Object> memberPromote(@RequestParam String memberId, @RequestParam String vipType) {
        return memberInfoService.memberPromote(memberId, vipType);
    }

    //获取会员积分+积分流水
    @GetMapping(value = "/WeChat/console/api/getIntegralAll")
    public Map<String, Object> getIntegralAll(@RequestParam String memberId) {
        return memberCrsService.getMemberIntegralFlow(memberId, 1, 1000);
    }

    //查询会员信息
    @GetMapping(value = "/WeChat/console/api/getMemberInfo")
    public MemberInfoView getMemberInfo(@RequestParam String memberId) {
        return memberInfoService.getMemberInfo(memberId);
    }

    //修改会员信息
    @PostMapping(value = "/WeChat/console/api/updateMemberInfo")
    public Map<String, String> updateMemberInfo(@RequestBody Member member) {
       return memberInfoService.updateMemberInfo(member);
    }
}
