package com.zq.shop.web.service.impl;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.bean.Room;
import com.zq.shop.web.bean.RoomUser;
import com.zq.shop.web.bean.Sequence;
import com.zq.shop.web.mappers.RoomMapper;
import com.zq.shop.web.mappers.RoomUserMapper;
import com.zq.shop.web.mappers.SequenceMapper;
import com.zq.shop.web.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 3:03 PM
 * @Package com.zq.shop.web.service.impl
 **/


@Service("iRoomService")
public class RoomServiceImpl implements IRoomService {
    @Autowired
    RoomMapper roomMapper;

    @Autowired
    RoomUserMapper roomUserMapper;

    @Autowired
    SequenceMapper sequenceMapper;


    @Override
    public ServerResponse joinRoom(Integer userId, Integer type) {

        List<Room> rooms = roomMapper.find();
        if (rooms.size() > 0) {
            for (Room room : rooms) {
                List<RoomUser> roomUsers = roomUserMapper.findByRoomIdAndStatus(room.getRoomId(), 0);
                if (roomUsers != null && roomUsers.size() < 3) {
                    roomAddUser(room, userId);
                    return ServerResponse.createBySuccess(room);
                }
            }
        }
        ServerResponse serverResponse = createRoom(type);
        roomAddUser(((Room) serverResponse.getData()), userId);
        return serverResponse;
    }

    @Override
    public ServerResponse leaveRoom(Integer userId) {
        List<RoomUser> byUidAndStatus = roomUserMapper.findByUidAndStatus(userId, 0);
        for (RoomUser roomUser : byUidAndStatus) {
            roomRemoveUser(roomUser.getRoomId(), userId);
        }

        return ServerResponse.createBySuccessMessage("离开房间");
    }

    @Override
    public ServerResponse getRoomInfo(Integer userId, Integer roomId) {

        Room oneByRoomId = roomMapper.findOneByRoomId(roomId);
        if (oneByRoomId != null)
            return ServerResponse.createBySuccess(oneByRoomId);
        else return ServerResponse.createByErrorMessage("roomId 错误");
    }

    private void roomRemoveUser(Integer roomId, Integer userId) {
        roomUserMapper.updateStatusByUidAndRoomId(3, userId, roomId);
        Room oneByRoomId = roomMapper.findOneByRoomId(roomId);
        roomMapper.updateUserNumByRoomId(oneByRoomId.getUserNum() - 1, roomId);
    }

    private ServerResponse createRoom(Integer type) {
        Sequence sequence = sequenceMapper.selectByPrimaryKey(2);
        int roomid = sequence.getCurrentValue() + sequence.getIncrement();
        sequence.setCurrentValue(roomid);
        sequenceMapper.updateByPrimaryKeySelective(sequence);

        Room room = new Room();
        room.setRoomId(roomid);
        if (type == 1) {
            room.setTimes(10);
        } else if (type == 2) {
            room.setTimes(100);
        } else if (type == 3) {
            room.setTimes(1000);
        } else {
            return ServerResponse.createByErrorMessage("倍率设置错误");
        }

        room.setType(type);

        room.setUserNum(0);

        roomMapper.insert(room);


        return ServerResponse.createBySuccess(room);

    }

    private void roomAddUser(Room room, Integer userId) {
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(room.getRoomId());
        roomUser.setUid(userId);
        roomUser.setStatus(0);
        roomUserMapper.insert(roomUser);

        roomMapper.updateUserNumByRoomId(room.getUserNum() + 1, room.getRoomId());

    }


}
