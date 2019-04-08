package com.zq.shop.web.controller.NormalUser;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.service.IRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 3:02 PM
 * @Package com.zq.shop.web.controller.NormalUser
 **/

@Slf4j
@Api(tags = "房间管理")
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    IRoomService iRoomService;

    @ApiOperation("加入房间")
    @PostMapping("/room_info")
    public ServerResponse getRoomInfo(Integer userId, Integer roomId) {
        if (userId != null) {
            return iRoomService.getRoomInfo(userId, roomId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("加入房间")
    @PostMapping("/join_room")
    public ServerResponse joinRoom(Integer userId, Integer type) {
        if (userId != null) {
            return iRoomService.joinRoom(userId, type);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }

    @ApiOperation("离开房间")
    @PostMapping("/leave_room")
    public ServerResponse leaveRoom(Integer userId) {
        if (userId != null) {
            return iRoomService.leaveRoom(userId);
        } else {
            return ServerResponse.createByErrorMessage("userId 不能为空");
        }
    }


}
