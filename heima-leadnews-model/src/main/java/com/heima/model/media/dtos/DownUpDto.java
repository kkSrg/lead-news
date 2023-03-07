package com.heima.model.media.dtos;

import lombok.Data;

/**
 * @author shawn
 * @date 2023年 01月 25日 22:30
 */

@Data
public class DownUpDto {

    private Integer id;
    /**
     * 是否上架  0 下架  1 上架
     */
    private Short enable;
}
