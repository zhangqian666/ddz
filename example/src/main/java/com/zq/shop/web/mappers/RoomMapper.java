package com.zq.shop.web.mappers;

import java.util.List;

import com.zq.shop.web.bean.Room;
import org.apache.ibatis.annotations.Param;

public interface RoomMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_room
     *
     * @mbg.generated
     */
    int insert(Room record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_room
     *
     * @mbg.generated
     */
    int insertSelective(Room record);

    List<Room> find();

    Room findOneByRoomId(@Param("roomId") Integer roomId);

    int updateCurrentGameIdByRoomId(@Param("updatedCurrentGameId") Integer updatedCurrentGameId, @Param("roomId") Integer roomId);
    int updateUserNumByRoomId(@Param("updatedUserNum")Integer updatedUserNum,@Param("roomId")Integer roomId);



}