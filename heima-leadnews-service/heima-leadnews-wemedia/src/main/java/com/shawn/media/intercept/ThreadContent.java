package com.shawn.media.intercept;

import com.heima.model.media.entities.User;

import java.util.Objects;

/**
 * @author shawn
 * @date 2023年 01月 09日 15:43
 */

public class ThreadContent {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user){
        if (Objects.nonNull(user)){
            threadLocal.set(user);
        }
    }

    public static Integer getId(){
        if (Objects.nonNull(threadLocal.get())){
            return threadLocal.get().getId();
        }
        throw new RuntimeException("副本参数为空!");
    }

    public static void setId(Integer id){
        User user = new User();
        user.setId(id);
        threadLocal.set(user);
    }

    public static void clear(){
        threadLocal.remove();
    }

}
