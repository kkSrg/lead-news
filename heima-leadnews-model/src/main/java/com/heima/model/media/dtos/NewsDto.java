package com.heima.model.media.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 09日 19:14
 */
@Data
public class NewsDto extends PageRequestDto {
    private String beginPubDate;
    private Integer channelId;
    private String endPubDate;
    private String keyword;
    private Integer status;

    private Date beginDate;
    private Date endDate;
}
