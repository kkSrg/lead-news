package com.shawn.es.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.es.dtos.UserSearchDto;
import com.shawn.es.service.ArticleSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController {

    @Resource
    private ArticleSearchService articleSearchService;

    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto dto) throws IOException {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return articleSearchService.search(dto);
    }
}