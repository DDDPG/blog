package com.ican.controller;

import com.ican.annotation.AccessLimit;
import com.ican.annotation.OptLog;
import com.ican.model.dto.CheckDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.MessageDTO;
import com.ican.model.vo.MessageBackVO;
import com.ican.model.vo.MessageVO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;
import com.ican.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ican.constant.OptTypeConstant.DELETE;
import static com.ican.constant.OptTypeConstant.UPDATE;

/**
 * 留言控制器
 *
 * @author ican
 */
@Api(tags = "留言模块")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 添加留言
     *
     * @param message 留言信息
     * @return {@link Result<>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    public Result<?> saveMessage(@Validated @RequestBody MessageDTO message) {
        messageService.saveMessage(message);
        return Result.success();
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return {@link Result<MessageBackVO>} 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/messages")
    public Result<PageResult<MessageBackVO>> listMessageBackVO(ConditionDTO condition) {
        return Result.success(messageService.listMessageBackVO(condition));
    }

    /**
     * 删除留言
     *
     * @param messageIdList 留言Id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/messages")
    public Result<?> deleteMessages(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核留言")
    @PutMapping("/admin/messages/pass")
    public Result<?> updateMessagesCheck(@Validated @RequestBody CheckDTO check) {
        messageService.updateMessagesCheck(check);
        return Result.success();
    }

    /**
     * 查看留言列表
     *
     * @return {@link Result<MessageVO>} 留言列表
     */
    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    public Result<List<MessageVO>> listMessageVO() {
        return Result.success(messageService.listMessageVO());
    }

}
