package com.zq.shop.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/31 3:47 PM
 * @Package com.zq.shop.web.vo
 **/


@Getter
@Setter
public class SecondUserInfo {
    private Integer uid;
    private String username;
    private String email;
    private String phone;
    private Integer sex;
    private Integer age;
    private String image;
    private Integer role;
    private Integer roll;
    private Integer gameId;
    private Integer roomId;
    private Integer roomSort;
    private Integer cardNum;
    private Integer type;
}
