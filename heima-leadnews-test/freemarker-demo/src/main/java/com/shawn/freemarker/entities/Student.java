package com.shawn.freemarker.entities;

import lombok.Data;

import java.util.Date;

/**
 * @author shawn
 * @date 2023年 01月 07日 14:55
 */
@Data
public class Student {
    private String name;//姓名
    private int age;//年龄
    private Date birthday;//生日
    private Float money;//钱包
}
