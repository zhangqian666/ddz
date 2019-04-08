package com.zq.shop.web.service;

import com.zq.core.restful.ServerResponse;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 3:03 PM
 * @Package com.zq.shop.web.service
 **/


public interface IRoomService {
    ServerResponse joinRoom(Integer userId,Integer type);

    ServerResponse leaveRoom(Integer userId);

    ServerResponse getRoomInfo(Integer userId, Integer roomId);

}
