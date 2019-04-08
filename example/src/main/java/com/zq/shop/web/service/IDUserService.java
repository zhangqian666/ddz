package com.zq.shop.web.service;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.bean.DUser;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 1:54 PM
 * @Package com.zq.shop.web.service
 **/


public interface IDUserService {
    ServerResponse register(DUser dUser);

    ServerResponse getUserInfo(String username);

    ServerResponse getUserInfo(Integer userId);
}
