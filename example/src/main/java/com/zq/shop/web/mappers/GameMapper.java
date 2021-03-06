package com.zq.shop.web.mappers;
import java.util.List;

import com.zq.shop.web.bean.Game;
import org.apache.ibatis.annotations.Param;

public interface GameMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game
     *
     * @mbg.generated
     */
    int insert(Game record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game
     *
     * @mbg.generated
     */
    int insertSelective(Game record);

    List<Game> findBygameId(@Param("gameId")Integer gameId);
    Game findOneByGameId(@Param("gameId")Integer gameId);

    int updateCardsByGameId(@Param("updatedCards")String updatedCards,@Param("gameId")Integer gameId);
    int updateCurrentSendTimeByGameId(@Param("updatedCurrentSendTime")Integer updatedCurrentSendTime,@Param("gameId")Integer gameId);
    int updateCurrentSendUserByGameId(@Param("updatedCurrentSendUser")Integer updatedCurrentSendUser,@Param("gameId")Integer gameId);
    int updateCardsAndCurrentSendUserAndCurrentSendTimeByGameId(@Param("updatedCards")String updatedCards,@Param("updatedCurrentSendUser")Integer updatedCurrentSendUser,@Param("updatedCurrentSendTime")Integer updatedCurrentSendTime,@Param("gameId")Integer gameId);









}