package com.shawn.freemarker.controller;

import com.shawn.freemarker.entities.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * @author shawn
 * @date 2023年 01月 07日 15:01
 */
@Controller
public class FreeMarkerController {

    @GetMapping("/basic")
    public String getBasic(Model model){
        //1.纯文本形式的参数
        model.addAttribute("name", "freemarker");
        //2.实体类相关的参数

        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        model.addAttribute("stu", student);

        return "basic";
    }

    @GetMapping("/list")
    public String getList(Model model){

        //1.纯文本形式的参数
        model.addAttribute("name", "freemarker");
        //2.实体类相关的参数

        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        student.setMoney(1000.86f);
        student.setBirthday(new Date());
        model.addAttribute("stu", student);

        //1.纯文本形式的参数
        model.addAttribute("name", "freemarker");
        //2.实体类相关的参数

        Student stu1 = new Student();
        stu1.setName("小花");
        stu1.setAge(19);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小凯");
        stu2.setAge(20);
        stu2.setMoney(1000.10f);
        stu2.setBirthday(new Date());

        List<Student> list = new ArrayList<>(Arrays.asList(stu1,stu2,student));
        model.addAttribute("stus", list);

        Map<String,Student> map = new HashMap<>();
        map.put("stu1",stu1);
        map.put("stu2",stu2);

        model.addAttribute("stuMap",map);
        return "basic";
    }

}
