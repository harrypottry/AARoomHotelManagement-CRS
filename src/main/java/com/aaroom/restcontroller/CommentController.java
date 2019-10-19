package com.aaroom.restcontroller;

import com.aaroom.exception.RestError;
import com.aaroom.model.StrListView;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.bean.MemberComment;
import com.aaroom.wechat.service.MemberCommentService;
import com.shangmei.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    private MemberCommentService memberCommentService;



    //查询评论列表信息
    @PostMapping(value = "/console/api/getMemberCommentByParams")
    public Object getMemberCommentByParams(@RequestParam(value = "province",required = false)String province,
                                           @RequestParam(value = "city",required = false)String city,
                                           @RequestParam(value = "hotel_id",required = false)String hotel_id,
                                           @RequestParam(value = "begin_time",required = false)String begin_time,
                                           @RequestParam(value = "end_time",required = false)String end_time,
                                           @RequestParam(value = "is_active",required = false)String is_active,
                                           @RequestParam(value = "pageIndex",defaultValue = "1",required = false)Integer pageIndex,
                                           @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize){
        Map params = new HashMap();
        params.put("province",province);
        params.put("city",city);
        params.put("begin_time",begin_time);
        params.put("end_time",end_time);
        if ("0".equals(hotel_id)){
            params.put("hotel_id",null);
        }else {
            params.put("hotel_id",hotel_id);
        }
        if ("2".equals(is_active)){  //查询所有
            params.put("is_active",null);
        }else {
            params.put("is_active",is_active);
        }

        List<Map<String,Object>> commentListByParams = memberCommentService.getCommentListByParams(params);

        for (Map<String,Object> map:commentListByParams){
            int id = (int)map.get("id");//mc的id
            MemberComment memberComment = memberCommentService.selectByPrimaryKey(id);
            map.put("create_time",DateUtil.format(memberComment.getCreate_time()));
        }
        Page pageList = PageUtils.getPageList(commentListByParams, pageIndex, pageSize);
        return pageList;
    }

    //批量修改评论信息权限
    @PostMapping(value = "/console/api/updateCommentByIds")
    public Object getMemberCommentByParams(@RequestBody StrListView strListView){
        if (strListView.getIds().size()>0){
            for (String id:strListView.getIds()){
                Map params = new HashMap();
                params.put("id",id);
                params.put("is_active",strListView.getIs_active());
                memberCommentService.updateByPrimaryKeySelective(params);
            }
            return RestError.E_OK;
        }
        return RestError.E_DATA_FAIL;
    }

}
