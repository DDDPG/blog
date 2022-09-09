package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Picture;
import com.ican.mapper.PictureMapper;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.DeleteDTO;
import com.ican.model.dto.PictureInfoDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.PictureBackVO;
import com.ican.model.vo.PictureVO;
import com.ican.service.PictureService;
import com.ican.service.RedisService;
import com.ican.utils.BeanCopyUtils;
import com.ican.utils.FileUtils;
import com.ican.utils.HttpClientUtils;
import com.ican.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.ican.constant.RedisConstant.PICTURE;

/**
 * @author ican
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public String uploadBiliPicture(MultipartFile file, String api, String cookie) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        // 转换文件
        File tempFile = FileUtils.multipartFileToFile(file);
        // 上传图片
        String result = HttpClientUtils.uploadFileByHttpClient(api, headers, tempFile, tempFile.getName());
        // 删除临时文件
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
        // 解析结果
        Object data = JSON.parseObject(result).get("data");
        String imageUrl = JSON.parseObject(data.toString()).get("image_url").toString();
        return imageUrl.replaceFirst("http", "https");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePictures(List<String> pictureUrlList) {
        // 批量保存图片
        List<Picture> pictureList = pictureUrlList.stream().map(item -> Picture.builder()
                        .pictureName(IdWorker.getIdStr())
                        .pictureUrl(item)
                        .build())
                .collect(Collectors.toList());
        this.saveBatch(pictureList);
        redisService.deleteObject(PICTURE);
    }

    @Override
    public PageResult<PictureBackVO> listPictureBackVO(ConditionDTO condition) {
        List<PictureBackVO> pictureBackVOList = new ArrayList<>();
        // 查询图片数量数量
        Integer count = pictureMapper.selectCount(new LambdaQueryWrapper<Picture>().eq(Picture::getIsDelete, condition.getIsDelete()));
        if (count == 0) {
            return new PageResult<>(pictureBackVOList, count);
        }
        // 查询图片列表
        pictureBackVOList = pictureMapper.selectPictureBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(pictureBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePictureDelete(DeleteDTO delete) {
        // 批量更新图片删除状态
        List<Picture> pictureList = delete.getIdList().stream()
                .map(id -> Picture.builder()
                        .id(id)
                        .isDelete(delete.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(pictureList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePictures(List<Integer> pictureIdList) {
        pictureMapper.deleteBatchIds(pictureIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePictureInfo(PictureInfoDTO pictureInfo) {
        // 更新图片信息
        Picture picture = BeanCopyUtils.copyBean(pictureInfo, Picture.class);
        pictureMapper.updateById(picture);
    }

    @Override
    public List<PictureVO> selectPictureList() {
        List<PictureVO> pictureVOList;
        Object pictureList = redisService.getObject(PICTURE);
        if (Objects.nonNull(pictureList)) {
            pictureVOList = JSON.parseObject(pictureList.toString(), List.class);
        } else {
            pictureVOList = pictureMapper.selectPictureVOList();
            redisService.setObject(PICTURE, JSON.toJSONString(pictureVOList));
        }
        return pictureVOList;
    }
}
