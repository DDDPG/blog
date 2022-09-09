package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.DeleteDTO;
import com.ican.model.dto.PictureInfoDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.PictureBackVO;
import com.ican.model.vo.Result;
import com.ican.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

import static com.ican.constant.OptTypeConstant.*;

/**
 * 图片控制器
 *
 * @author ican
 */
@Api(tags = "图片模块")
@RestController
public class PictureController {

    @Autowired
    private PictureService pictureService;

    /**
     * B站图片批量上传
     *
     * @param file   文件
     * @param api    接口
     * @param cookie cookie
     * @return {@link Result<String>} 图片链接
     */
    @ApiOperation(value = "B站图片批量上传")
    @PostMapping("/admin/pictures/bili")
    public Result<String> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                     @RequestParam(value = "api") String api,
                                     @RequestParam(value = "cookie") String cookie) {
        return Result.success(pictureService.uploadBiliPicture(file, api, cookie));
    }

    /**
     * 保存图片
     *
     * @param pictureUrlList 图片链接列表
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE)
    @ApiOperation(value = "保存图片")
    @PostMapping("/admin/pictures")
    public Result<?> savePictures(@RequestBody List<String> pictureUrlList) {
        pictureService.savePictures(pictureUrlList);
        return Result.success();
    }

    /**
     * 获取图片列表
     *
     * @param condition 条件
     * @return {@link Result<PictureBackVO>} 照片列表
     */
    @ApiOperation(value = "获取图片列表")
    @GetMapping("/admin/pictures")
    public Result<PageResult<PictureBackVO>> listPictureBackVO(ConditionDTO condition) {
        return Result.success(pictureService.listPictureBackVO(condition));
    }

    /**
     * 逻辑删除图片
     *
     * @param delete 删除信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "逻辑删除图片")
    @PutMapping("/admin/pictures/delete")
    public Result<?> updatePictureDelete(@Validated @RequestBody DeleteDTO delete) {
        pictureService.updatePictureDelete(delete);
        return Result.success();
    }

    /**
     * 删除图片
     *
     * @param pictureIdList 图片id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除图片")
    @DeleteMapping("/admin/pictures")
    public Result<?> deletePictures(@RequestBody List<Integer> pictureIdList) {
        pictureService.deletePictures(pictureIdList);
        return Result.success();
    }

    /**
     * 更新图片信息
     *
     * @param photoInfo 照片信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新图片信息")
    @PutMapping("/admin/pictures")
    public Result<?> updatePictureInfo(@Validated @RequestBody PictureInfoDTO photoInfo) {
        pictureService.updatePictureInfo(photoInfo);
        return Result.success();
    }

}
