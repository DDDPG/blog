package com.ican.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 审核DTO
 *
 * @author ican
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckDTO {

    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    private List<Integer> idList;

    /**
     * 是否通过 (0否 1是)
     */
    @NotNull(message = "状态值不能为空")
    private Integer isCheck;
}
