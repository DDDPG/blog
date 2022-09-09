package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.SiteSetting;
import com.ican.model.dto.AboutDTO;
import com.ican.model.dto.WebsiteDTO;
import com.ican.model.vo.SiteBackInfoVO;
import com.ican.model.vo.SiteHomeInfoVO;

/**
 * 网站配置业务接口
 *
 * @author ican
 */
public interface SiteSettingService extends IService<SiteSetting> {

    /**
     * 更新网站配置
     *
     * @param website 网站配置
     */
    void updateSiteSetting(WebsiteDTO website);

    /**
     * 获取网站配置
     *
     * @return 网站配置
     */
    WebsiteDTO getSiteSetting();

    /**
     * 查看博客信息
     *
     * @return 博客信息
     */
    SiteHomeInfoVO getSiteHomeInfo();

    /**
     * 获取后台首页数据
     *
     * @return 后台首页数据
     */
    SiteBackInfoVO getSiteBackInfo();

    /**
     * 上传访客信息
     */
    void report();

    /**
     * 查看关于我信息
     *
     * @return 关于我信息
     */
    String getAbout();

    /**
     * 修改关于我信息
     *
     * @param about 关于我信息
     */
    void updateAbout(AboutDTO about);
}
