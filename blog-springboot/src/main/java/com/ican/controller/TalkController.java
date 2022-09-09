package com.ican.controller;

import com.ican.annotation.AccessLimit;
import com.ican.annotation.OptLog;
import com.ican.enums.LikeTypeEnum;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TalkDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;
import com.ican.model.vo.TalkBackVO;
import com.ican.model.vo.TalkVO;
import com.ican.service.TalkService;
import com.ican.strategy.context.LikeStrategyContext;
import com.ican.strategy.context.UploadStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ican.constant.OptTypeConstant.DELETE;
import static com.ican.constant.OptTypeConstant.SAVE_OR_UPDATE;
import static com.ican.enums.FilePathEnum.TALK;


/**
 * 说说控制器
 *
 * @author ican
 */
@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 新增或修改说说
     *
     * @param talk 说说信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改说说")
    @PostMapping("/admin/talks")
    public Result<?> saveOrUpdateTalk(@Validated @RequestBody TalkDTO talk) {
        talkService.saveOrUpdateTalk(talk);
        return Result.success();
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return {@link Result<String>}
     */
    @ApiOperation(value = "上传说说图片")
    @PostMapping("/admin/talks/images")
    public Result<String> saveTalkImages(MultipartFile file) {
        return Result.success(uploadStrategyContext.executeUploadStrategy(file, TALK.getPath()));
    }

    /**
     * 删除说说
     *
     * @param talkId 说说id
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除说说")
    @DeleteMapping("/admin/talks/{talkId}")
    public Result<?> deleteTalk(@PathVariable("talkId") Integer talkId) {
        talkService.deleteTalk(talkId);
        return Result.success();
    }

    /**
     * 查看后台说说
     *
     * @param condition 条件
     * @return {@link Result<TalkBackVO>}
     */
    @ApiOperation(value = "查看后台说说")
    @GetMapping("/admin/talks")
    public Result<PageResult<TalkBackVO>> listTalkBackVO(ConditionDTO condition) {
        return Result.success(talkService.listTalkBackVO(condition));
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "点赞说说")
    @AccessLimit(seconds = 60, maxCount = 3)
    @PostMapping("/talks/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.TALK, talkId);
        return Result.success();
    }

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return {@link Result<TalkBackVO>}
     */
    @ApiOperation(value = "根据id查看后台说说")
    @GetMapping("/admin/talks/{talkId}")
    public Result<TalkBackVO> getBackTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.getBackTalkById(talkId));
    }

    /**
     * 查看首页说说
     *
     * @return {@link Result<String>}
     */
    @ApiOperation(value = "查看首页说说")
    @GetMapping("/home/talks")
    public Result<List<String>> listTalkHome() {
        return Result.success(talkService.listTalkHome());
    }

    /**
     * 查看说说列表
     *
     * @return {@link Result<TalkVO>}
     */
    @ApiOperation(value = "查看说说列表")
    @GetMapping("/talks")
    public Result<PageResult<TalkVO>> listTalkVO() {
        return Result.success(talkService.listTalkVO());
    }

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return {@link Result<TalkVO>}
     */
    @ApiOperation(value = "根据id查看说说")
    @GetMapping("/talks/{talkId}")
    public Result<TalkVO> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }

}
