package com.zq.shop.web.service;

import com.zq.core.restful.ServerResponse;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 4:05 PM
 * @Package com.zq.shop.web.service
 **/


public interface IGameService {
    ServerResponse startGame(Integer userId, Integer roomId);

    ServerResponse robLandLord(Integer userId, Integer gameId);

    ServerResponse playCard(Integer userId, Integer gameId,String cards);

    ServerResponse jumpCard(Integer userId, Integer gameId);

    ServerResponse getGameInfo(Integer userId, Integer roomId);
}
