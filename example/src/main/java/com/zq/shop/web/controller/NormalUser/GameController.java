package com.zq.shop.web.controller.NormalUser;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.service.IGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 4:04 PM
 * @Package com.zq.shop.web.controller.NormalUser
 **/

@Slf4j
@Api(tags = "游戏管理")
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    IGameService iGameService;


    @ApiOperation("开始游戏")
    @PostMapping("/start_game")
    public ServerResponse startGame(Integer userId, Integer roomId) {
        if (userId != null) {
            return iGameService.startGame(userId, roomId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("获取当前游戏信息")
    @PostMapping("/get_game_info")
    public ServerResponse getGameInfo(Integer userId, Integer roomId) {
        if (userId != null) {
            return iGameService.getGameInfo(userId, roomId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("抢地主")
    @PostMapping("/rob_landlord")
    public ServerResponse robLandlord(Integer userId, Integer gameId) {
        if (userId != null) {
            return iGameService.robLandLord(userId, gameId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("出牌")
    @PostMapping("/play")
    public ServerResponse playCard(Integer userId, Integer gameId, String cards) {
        if (userId != null) {
            return iGameService.playCard(userId, gameId, cards);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("跳过")
    @PostMapping("/jump")
    public ServerResponse jumpCard(Integer userId, Integer gameId) {
        if (userId != null) {
            return iGameService.jumpCard(userId, gameId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }
}
