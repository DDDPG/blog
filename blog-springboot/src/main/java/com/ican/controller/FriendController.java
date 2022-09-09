package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.FriendDTO;
import com.ican.model.vo.FriendBackVO;
import com.ican.model.vo.FriendVO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;
import com.ican.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.ican.constant.OptTypeConstant.*;

/**
 * 友链控制器
 *
 * @author ican
 */
@Api(tags = "友链模块")
@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 查看友链列表
     *
     * @return {@link Result<FriendVO>} 友链列表
     */
    @ApiOperation(value = "查看友链列表")
    @GetMapping("/friends")
    public Result<List<FriendVO>> listFriendVO() {
        return Result.success(friendService.listFriendVO());
    }

    /**
     * 查看友链后台列表
     *
     * @param condition 查询条件
     * @return {@link PageResult<FriendBackVO>} 后台友链列表
     */
    @ApiOperation(value = "查看友链后台列表")
    @GetMapping("/admin/friends")
    public Result<PageResult<FriendBackVO>> listFriendBackVO(ConditionDTO condition) {
        return Result.success(friendService.listFriendBackVO(condition));
    }

    /**
     * 新增或修改友链
     *
     * @param friend 友链
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改友链")
    @PostMapping("/admin/friends")
    public Result<?> saveOrUpdateFriend(@Validated @RequestBody FriendDTO friend) {
        friendService.saveOrUpdateFriend(friend);
        return Result.success();
    }

    /**
     * 删除友链
     *
     * @param friendIdList 友链id集合
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除友链")
    @DeleteMapping("/admin/friends")
    public Result<?> deleteFriend(@RequestBody List<Integer> friendIdList) {
        friendService.removeByIds(friendIdList);
        return Result.success();
    }

}
