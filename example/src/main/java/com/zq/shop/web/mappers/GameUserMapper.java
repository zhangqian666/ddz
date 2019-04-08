package com.zq.shop.web.mappers;
import java.util.List;

import com.zq.shop.web.bean.GameUser;
import org.apache.ibatis.annotations.Param;

public interface GameUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user
     *
     * @mbg.generated
     */
    int insert(GameUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user
     *
     * @mbg.generated
     */
    int insertSelective(GameUser record);
    GameUser findOneByUidAndGameId(@Param("uid")Integer uid, @Param("gameId")Integer gameId);
    GameUser findOneByGameIdAndRoomSort(@Param("gameId")Integer gameId,@Param("roomSort")Integer roomSort);
    List<GameUser> findByGameId(@Param("gameId")Integer gameId);

    int updateCardsByUidAndGameId(@Param("updatedCards")String updatedCards,@Param("uid")Integer uid,@Param("gameId")Integer gameId);

    int updateCardsAndTypeByUidAndGameId(@Param("updatedCards")String updatedCards,@Param("updatedType")Integer updatedType,@Param("uid")Integer uid,@Param("gameId")Integer gameId);



}