package com.zq.shop.web.vo;

import com.zq.shop.web.bean.Game;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/31 3:43 PM
 * @Package com.zq.shop.web.vo
 **/

@Getter
@Setter
public class GameInfoVo {

    private Game game;

    private MainUserInfo mainUserInfo;

    private List<SecondUserInfo> secondUserInfos;

    private String tableCards;
}
