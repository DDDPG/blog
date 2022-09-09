package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.UniqueView;
import com.ican.model.vo.UniqueViewVO;

import java.util.List;


/**
 * 用户量统计接口
 *
 * @author ican
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 获取7天用户量统计
     *
     * @return 用户量
     */
    List<UniqueViewVO> listUniqueView();

}
