package com.ican.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.OperationLog;
import com.ican.mapper.OperationLogMapper;
import com.ican.model.vo.OperationExcelVO;
import com.ican.model.vo.OperationLogVO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.Result;
import com.ican.service.OperationLogService;
import com.ican.utils.PageUtils;
import com.ican.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.ican.enums.StatusCodeEnum.UNLOGIN;

/**
 * 操作日志业务接口实现类
 *
 * @author ican
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private HttpServletResponse response;

    @Override
    public PageResult<OperationLogVO> listOperationLogVO(ConditionDTO condition) {
        // 查询操作日志数量
        Integer count = operationLogMapper.selectCount(new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.hasText(condition.getOptModule()), OperationLog::getModule, condition.getOptModule())
                .like(StringUtils.hasText(condition.getKeyword()), OperationLog::getDescription, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询操作日志列表
        List<OperationLogVO> operationLogVOList = operationLogMapper.selectOperationLogVOList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(operationLogVOList, count);
    }

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        // 保存操作日志
        operationLogMapper.insert(operationLog);
    }

    @Override
    public void exportExceptionLog() {
        try {
            // 请求头
            WebUtils.setDownloadHeader("OperationLog.xlsx", response);
            // 查询操作日志
            List<OperationExcelVO> operationExcelVOList = operationLogMapper.selectOperationExcelVO();
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), OperationExcelVO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("操作日志导出")
                    .doWrite(operationExcelVOList);
        } catch (IOException e) {
            WebUtils.renderString(response, JSON.toJSONString(Result.fail("下载失败")));
        }
    }
}
