package com.zq.shop.web.mappers;

import java.util.List;

import com.zq.shop.web.bean.GameUserCards;
import org.apache.ibatis.annotations.Param;

public interface GameUserCardsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer cardId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    int insert(GameUserCards record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    int insertSelective(GameUserCards record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    GameUserCards selectByPrimaryKey(Integer cardId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(GameUserCards record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zq_game_user_cards
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(GameUserCards record);

    GameUserCards findOneByGameIdAndSendCardTime(@Param("gameId")Integer gameId,@Param("sendCardTime")Integer sendCardTime);



}