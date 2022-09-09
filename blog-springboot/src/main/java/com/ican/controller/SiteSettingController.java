package com.ican.controller;

import com.ican.annotation.OptLog;
import com.ican.enums.FilePathEnum;
import com.ican.model.dto.AboutDTO;
import com.ican.model.dto.WebsiteDTO;
import com.ican.model.vo.Result;
import com.ican.model.vo.SiteBackInfoVO;
import com.ican.model.vo.SiteHomeInfoVO;
import com.ican.service.SiteSettingService;
import com.ican.strategy.context.UploadStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.ican.constant.OptTypeConstant.UPDATE;

/**
 * 网站配置控制器
 *
 * @author ican
 */
@Api(tags = "网站配置模块")
@RestController
public class SiteSettingController {

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 查看博客信息
     *
     * @return {@link Result<SiteHomeInfoVO>} 博客信息
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    public Result<SiteHomeInfoVO> getSiteHomeInfo() {
        return Result.success(siteSettingService.getSiteHomeInfo());
    }

    /**
     * 查看后台信息
     *
     * @return {@link Result<SiteBackInfoVO>} 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<SiteBackInfoVO> getSiteBackInfo() {
        return Result.success(siteSettingService.getSiteBackInfo());
    }

    /**
     * 上传博客配置图片
     *
     * @param file 图片
     * @return {@link Result<String>} 图片路径
     */
    @ApiOperation(value = "上传博客配置图片")
    @PostMapping("/admin/site/image")
    public Result<String> uploadSiteImg(MultipartFile file) {
        return Result.success(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }

    /**
     * 更新网站配置
     *
     * @param website 网站配置信息
     * @return {@link Result}
     */
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/site/setting")
    public Result<?> updateSiteSetting(@RequestBody WebsiteDTO website) {
        siteSettingService.updateSiteSetting(website);
        return Result.success();
    }

    /**
     * 获取网站配置
     *
     * @return {@link Result<WebsiteDTO>} 网站配置
     */
    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/site/setting")
    public Result<WebsiteDTO> getWebsiteConfig() {
        return Result.success(siteSettingService.getSiteSetting());
    }

    /**
     * 上传访客信息
     *
     * @return {@link Result<>}
     */
    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        siteSettingService.report();
        return Result.success();
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */
    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.success(siteSettingService.getAbout());
    }

    /**
     * 修改关于我信息
     *
     * @param about 关于我信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    public Result<?> updateAbout(@Validated @RequestBody AboutDTO about) {
        siteSettingService.updateAbout(about);
        return Result.success();
    }
}
