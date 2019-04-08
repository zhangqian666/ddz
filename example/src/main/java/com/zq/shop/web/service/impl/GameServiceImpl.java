package com.zq.shop.web.service.impl;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.bean.*;
import com.zq.shop.web.common.CardUtil;
import com.zq.shop.web.common.Const;
import com.zq.shop.web.common.DDZ_CardType;
import com.zq.shop.web.common.SocketServer;
import com.zq.shop.web.mappers.*;
import com.zq.shop.web.service.IGameService;
import com.zq.shop.web.vo.GameInfoVo;
import com.zq.shop.web.vo.MainUserInfo;
import com.zq.shop.web.vo.SecondUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.zq.shop.web.common.CardUtil.listToString;
import static com.zq.shop.web.common.CardUtil.stringToList;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 4:05 PM
 * @Package com.zq.shop.web.service.impl
 **/

@Service("IGameService")
@Slf4j
public class GameServiceImpl implements IGameService {
    @Autowired
    RoomUserMapper roomUserMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    SequenceMapper sequenceMapper;

    @Autowired
    GameMapper gameMapper;

    @Autowired
    GameUserMapper gameUserMapper;

    @Autowired
    DUserMapper dUserMapper;


    @Autowired
    GameUserCardsMapper gameUserCardsMapper;

    @Autowired
    SocketServer socketServer;

    @Override
    public ServerResponse startGame(Integer userId, Integer roomId) {


        List<RoomUser> byRoomIdAndStatus = roomUserMapper.findByRoomIdAndStatus(roomId, 0);
        if (byRoomIdAndStatus != null && byRoomIdAndStatus.size() == 3) {

            Room room = roomMapper.findOneByRoomId(roomId);

            Sequence sequence = sequenceMapper.selectByPrimaryKey(3);
            int currentvalue = sequence.getCurrentValue() + sequence.getIncrement();
            sequence.setCurrentValue(currentvalue);
            sequenceMapper.updateByPrimaryKeySelective(sequence);

            Game newGame = new Game();
            newGame.setGameId(currentvalue);
            newGame.setRoomId(roomId);
            newGame.setTimes(room.getTimes());
            newGame.setCurrentTimes(room.getTimes());
            newGame.setCurrentSendTime(0);
            newGame.setCurrentSendUser(4);

            List<Integer> pai = new ArrayList<>();
            for (int i = 0; i < 54; i++) {
                pai.add(i);
            }

            for (int i = 0; i < 3; i++) {
                GameUser gameUser = new GameUser();
                gameUser.setGameId(currentvalue);
                gameUser.setRoomId(roomId);
                gameUser.setUid(byRoomIdAndStatus.get(i).getUid());
                gameUser.setRoomSort(i);
                gameUser.setType(0);
                List<Integer> user = new ArrayList<>();
                serUserList(pai, user);

                gameUser.setCards(CardUtil.sort(user));
                gameUserMapper.insert(gameUser);
            }

            newGame.setCards(listToString(pai));
            gameMapper.insert(newGame);

            roomMapper.updateCurrentGameIdByRoomId(newGame.getGameId(), roomId);

            List<GameUser> byGameId = gameUserMapper.findByGameId(newGame.getGameId());
            //长链接 推送可以开始游戏的消息 10010

            socketServer.sendMsg(byGameId.get(0).getUid(), Const.JPush_MSG.START_GAME + 0);
            socketServer.sendMsg(byGameId.get(1).getUid(), Const.JPush_MSG.START_GAME + 0);
            socketServer.sendMsg(byGameId.get(2).getUid(), Const.JPush_MSG.START_GAME + 0);


            return ServerResponse.createBySuccess(newGame);

        }
        return ServerResponse.createByErrorMessage("人数不满");
    }

    @Override
    public ServerResponse robLandLord(Integer userId, Integer gameId) {
        Game game = gameMapper.findOneByGameId(gameId);

        if (game.getCards().isEmpty()) {
            return ServerResponse.createByErrorMessage("抢地主失败");
        }

        List<Integer> cards = stringToList(game.getCards());


        GameUser gameUser = gameUserMapper.findOneByUidAndGameId(userId, gameId);
        List<Integer> gameCards = stringToList(gameUser.getCards());
        gameCards.addAll(cards);


        gameUser.setCards(CardUtil.sort(gameCards));
        gameUserMapper.updateCardsAndTypeByUidAndGameId(listToString(gameCards), 1, userId, gameId);
        gameMapper.updateCardsAndCurrentSendUserAndCurrentSendTimeByGameId(
                "",
                gameUser.getRoomSort(),
                0,
                gameId);


        List<GameUser> gameUsers = gameUserMapper.findByGameId(gameId);
        //长链接推送 推送抢地主结束的消息 10020
        socketServer.sendMsg(gameUsers.get(0).getUid(), Const.JPush_MSG.ROB_LANDLORD + gameUser.getRoomSort());
        socketServer.sendMsg(gameUsers.get(1).getUid(), Const.JPush_MSG.ROB_LANDLORD + gameUser.getRoomSort());
        socketServer.sendMsg(gameUsers.get(2).getUid(), Const.JPush_MSG.ROB_LANDLORD + gameUser.getRoomSort());

        return ServerResponse.createBySuccessMessage("抢地主成功");
    }

    @Override
    public ServerResponse playCard(Integer userId, Integer gameId, String cards) {
        Game game = gameMapper.findOneByGameId(gameId);

        GameUser gameUser = gameUserMapper.findOneByUidAndGameId(userId, gameId);

        if (gameUser.getRoomSort().intValue() != game.getCurrentSendUser().intValue()) {
            return ServerResponse.createByErrorMessage("未轮到您出牌");
        }


        //第一次发牌
        if (game.getCurrentSendTime() == 0) {
            if (sendCard(userId, game, gameId, cards)) {
                return ServerResponse.createBySuccessMessage("发牌成功");
            } else {
                return ServerResponse.createByErrorMessage("不符合牌型");
            }
        }


        //判断是不是大于上家 或者 上次发送的还是自己
        GameUserCards gameUserCard = gameUserCardsMapper.findOneByGameIdAndSendCardTime(gameId, game.getCurrentSendTime());
        String upCards = gameUserCard.getCardName();
        if (gameUserCard.getUid().equals(userId)) {
            if (sendCard(userId, game, gameId, cards)) {
                return ServerResponse.createBySuccessMessage("发牌成功");
            } else {
                return ServerResponse.createByErrorMessage("不符合牌型");
            }

        }
        if (contrastCards(upCards, cards)) {
            if (sendCard(userId, game, gameId, cards)) {
                return ServerResponse.createBySuccessMessage("发牌成功");
            } else {
                return ServerResponse.createByErrorMessage("不符合牌型");
            }
        } else {
            return ServerResponse.createByErrorMessage("没有大过上家");
        }


    }

    @Override
    public ServerResponse jumpCard(Integer userId, Integer gameId) {
        Game oneByGameId = gameMapper.findOneByGameId(gameId);
        gameMapper.updateCurrentSendUserByGameId((oneByGameId.getCurrentSendUser() + 1) % 3, gameId);


        List<GameUser> gameUsers = gameUserMapper.findByGameId(gameId);
        //长链接推送 推送抢地主结束的消息 10020

        socketServer.sendMsg(gameUsers.get(0).getUid(), Const.JPush_MSG.JUMP_CARD + 0);
        socketServer.sendMsg(gameUsers.get(1).getUid(), Const.JPush_MSG.JUMP_CARD + 0);
        socketServer.sendMsg(gameUsers.get(2).getUid(), Const.JPush_MSG.JUMP_CARD + 0);

        return ServerResponse.createBySuccessMessage("跳牌成功");
    }

    @Override
    public ServerResponse getGameInfo(Integer userId, Integer roomId) {

        Room oneByRoomId = roomMapper.findOneByRoomId(roomId);
        Integer gameId = oneByRoomId.getCurrentGameId();

        Game game = gameMapper.findOneByGameId(gameId);

        List<GameUser> gameUsers = gameUserMapper.findByGameId(gameId);

        GameUserCards gameUserCardinfo = gameUserCardsMapper.findOneByGameIdAndSendCardTime(gameId, game.getCurrentSendTime());

        //构建界面信息
        GameInfoVo gameInfoVo = new GameInfoVo();

        //把游戏基础信息设置到界面信息
        gameInfoVo.setGame(game);

        List<SecondUserInfo> secondUserInfos = new ArrayList<>();
        for (GameUser gameUser : gameUsers) {
            DUser dUser = dUserMapper.selectByPrimaryKey(gameUser.getUid());
            if (gameUser.getUid().intValue() == userId.intValue()) {
                MainUserInfo mainUserInfo = new MainUserInfo();
                mainUserInfo.setAge(dUser.getAge());
                mainUserInfo.setEmail(dUser.getEmail());
                mainUserInfo.setImage(dUser.getImage());
                mainUserInfo.setPhone(dUser.getPhone());
                mainUserInfo.setRole(dUser.getRole());
                mainUserInfo.setRoll(dUser.getRoll());
                mainUserInfo.setSex(dUser.getSex());
                mainUserInfo.setUid(dUser.getUid());
                mainUserInfo.setUsername(dUser.getUsername());
                mainUserInfo.setRoomId(gameUser.getRoomId());
                mainUserInfo.setRoomSort(gameUser.getRoomSort());
                mainUserInfo.setType(gameUser.getType());
                mainUserInfo.setGameId(gameUser.getGameId());
                mainUserInfo.setCards(gameUser.getCards());
                //添加到界面信息
                gameInfoVo.setMainUserInfo(mainUserInfo);
            } else {
                SecondUserInfo secondUserInfo = new SecondUserInfo();
                secondUserInfo.setAge(dUser.getAge());
                secondUserInfo.setEmail(dUser.getEmail());
                secondUserInfo.setImage(dUser.getImage());
                secondUserInfo.setPhone(dUser.getPhone());
                secondUserInfo.setRole(dUser.getRole());
                secondUserInfo.setRoll(dUser.getRoll());
                secondUserInfo.setSex(dUser.getSex());
                secondUserInfo.setUid(dUser.getUid());
                secondUserInfo.setUsername(dUser.getUsername());
                secondUserInfo.setRoomId(gameUser.getRoomId());
                secondUserInfo.setRoomSort(gameUser.getRoomSort());
                secondUserInfo.setType(gameUser.getType());
                secondUserInfo.setGameId(gameUser.getGameId());
                secondUserInfo.setCardNum(stringToList(gameUser.getCards()).size());
                secondUserInfos.add(secondUserInfo);
            }
        }

        gameInfoVo.setSecondUserInfos(secondUserInfos);

        if (gameUserCardinfo != null) {
            //设置桌面上的卡牌
            gameInfoVo.setTableCards(gameUserCardinfo.getCardName());
        }


        return ServerResponse.createBySuccess(gameInfoVo);
    }

    /**
     * cardname 》 cards 返回true
     *
     * @param mcards
     * @param cards
     * @return
     */
    private boolean contrastCards(String cards, String mcards) {
        List<Integer> cardlist = CardUtil.stringToList(cards);
        List<Integer> mcardlist = CardUtil.stringToList(mcards);

        return DDZ_CardType.isSelectCardCanPut(mcardlist, DDZ_CardType.PopEnable(cardlist),
                cardlist, DDZ_CardType.PopEnable(cardlist));
    }

    private boolean sendCard(Integer userId, Game oneByGameId, Integer gameId, String cards) {

        if (DDZ_CardType.PopEnable(CardUtil.stringToList(cards)) == DDZ_CardType.DDZ_POKER_TYPE.DDZ_PASS) {
            return false;
        }

        //插入新的gameusercards 新的内容
        GameUserCards gameUserCards = new GameUserCards();
        gameUserCards.setCardName(cards);
        gameUserCards.setUid(userId);
        gameUserCards.setSendCardTime(oneByGameId.getCurrentSendTime() + 1);
        gameUserCards.setGameId(gameId);
        gameUserCardsMapper.insert(gameUserCards);


        //更新gameuser 内容 删除里面发送的内容
        GameUser gameUser = gameUserMapper.findOneByUidAndGameId(userId, gameId);

        List<Integer> firstCards = stringToList(gameUser.getCards());
        List<Integer> secondeCards = stringToList(cards);

        Iterator<Integer> iterator = firstCards.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            for (int i = 0; i < secondeCards.size(); i++) {
                if (next.intValue()
                        == secondeCards.get(i).intValue()) {
                    iterator.remove();
                }
            }
        }

        //如果本人手里没牌了 则本人胜利
        if (firstCards.size() == 0) {

            List<GameUser> gameUsers = gameUserMapper.findByGameId(gameId);
            //长链接推送 推送抢地主结束的消息 10020
            socketServer.sendMsg(gameUsers.get(0).getUid(), Const.JPush_MSG.GAME_OVER + gameUser.getRoomSort());
            socketServer.sendMsg(gameUsers.get(1).getUid(), Const.JPush_MSG.GAME_OVER + gameUser.getRoomSort());
            socketServer.sendMsg(gameUsers.get(2).getUid(), Const.JPush_MSG.GAME_OVER + gameUser.getRoomSort());
            return true;
        }
        gameUserMapper.updateCardsByUidAndGameId(listToString(firstCards), userId, gameId);

        //更新game内容
        gameMapper.updateCurrentSendTimeByGameId(oneByGameId.getCurrentSendTime() + 1, gameId);
        int user = (oneByGameId.getCurrentSendUser() + 1) % 3;
        gameMapper.updateCurrentSendUserByGameId(user, gameId);


        List<GameUser> gameUsers = gameUserMapper.findByGameId(gameId);
        //长链接推送 推送发送消息的消息 10050
        socketServer.sendMsg(gameUsers.get(0).getUid(), Const.JPush_MSG.SEND_OVER + gameUser.getRoomSort());
        socketServer.sendMsg(gameUsers.get(1).getUid(), Const.JPush_MSG.SEND_OVER + gameUser.getRoomSort());
        socketServer.sendMsg(gameUsers.get(2).getUid(), Const.JPush_MSG.SEND_OVER + gameUser.getRoomSort());
        return true;

    }

    private void serUserList(List<Integer> pai, List<Integer> user) {
        for (int i = 0; i < 17; i++) {
            int random = (int) (Math.random() * pai.size());
            user.add(pai.get(random));
            pai.remove(random);
        }


    }


}
