package com.example.zhli.lottery.engine;

import com.example.zhli.lottery.bean.User;
import com.example.zhli.lottery.net.protocal.Message;

/**
 * 用户相关的业务操作的接口
 * Created by zhli on 2015/2/9.
 */
public interface UserEngine {
    /**
     * 用户登录
     * @param user
     * @return
     */
    Message login(User user);
}
