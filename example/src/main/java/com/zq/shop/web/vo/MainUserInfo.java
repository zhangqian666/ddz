package com.zq.shop.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/31 3:45 PM
 * @Package com.zq.shop.web.vo
 **/


@Getter
@Setter
public class MainUserInfo {
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
    private String cards;
    private Integer type;


}
