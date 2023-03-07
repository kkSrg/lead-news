package com.shawn.es.entities;

import lombok.Data;

import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 26日 20:54
 */
@Data
public class ArticleDoc {
    private Integer authorId;
    private String authorName;
    private String content ;
    private Long id;
    private String images;
    private Integer layout;
    private Date publishTime;
    private String staticUrl;
    private String title;

    @Override
    public String toString() {
        return "ArticleDoc{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", images='" + images + '\'' +
                ", layout=" + layout +
                ", publishTime=" + publishTime +
                ", staticUrl='" + staticUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
