package com.heima.model.media.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @author shawn
 * @date 2023年 01月 09日 16:43
 */
@Data
public class PageDto extends PageRequestDto {
    private String isCollection;
}
