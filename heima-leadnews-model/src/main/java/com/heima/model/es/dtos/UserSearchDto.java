package com.heima.model.es.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 29日 10:14
 */
@Data
public class UserSearchDto {

    /**
     * 搜索关键字
     */
    String searchWords;
    /**
     * 当前页
     */
    int pageNum;
    /**
     * 分页条数
     */
    int pageSize;
    /**
     * 最小时间
     */
    Date minBehotTime;

    public int getFromIndex(){
        if(this.pageNum<1)return 0;
        if(this.pageSize<1) this.pageSize = 10;
        return this.pageSize * (pageNum-1);
    }

}
