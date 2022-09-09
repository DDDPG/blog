package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Picture;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.DeleteDTO;
import com.ican.model.dto.PictureInfoDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.PictureBackVO;
import com.ican.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ican
 */

public interface PictureService extends IService<Picture> {

    /**
     * B站图片上传
     *
     * @param file   文件
     * @param api    接口
     * @param cookie 身份凭证
     * @return 图片链接
     */
    String uploadBiliPicture(MultipartFile file, String api, String cookie);

    /**
     * 保存图片
     *
     * @param pictureUrlList 图片链接
     */
    void savePictures(List<String> pictureUrlList);

    /**
     * 查询图片列表
     *
     * @param condition 条件
     * @return 图片列表
     */
    PageResult<PictureBackVO> listPictureBackVO(ConditionDTO condition);

    /**
     * 修改图片删除状态
     *
     * @param delete 删除信息
     */
    void updatePictureDelete(DeleteDTO delete);

    /**
     * 删除图片
     *
     * @param pictureIdList 图片id列表
     */
    void deletePictures(List<Integer> pictureIdList);

    /**
     * 更新图片信息
     *
     * @param pictureInfo 图片信息
     */
    void updatePictureInfo(PictureInfoDTO pictureInfo);

    /**
     * 获取图片列表
     *
     * @return 图片列表
     */
    List<PictureVO> selectPictureList();
}
