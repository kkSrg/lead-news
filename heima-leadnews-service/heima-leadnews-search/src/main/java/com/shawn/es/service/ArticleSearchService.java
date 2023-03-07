package com.shawn.es.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.es.dtos.UserSearchDto;

import java.io.IOException;

/**
 * @author shawn
 * @date 2023年 01月 29日 10:19
 */
public interface ArticleSearchService {
    ResponseResult search(UserSearchDto dto) throws IOException;
}
