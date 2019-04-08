package com.zq.shop.web.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 7:47 PM
 * @Package com.zq.shop.web.common
 **/


public class CardUtil {
    public enum CardType {
        c1,//单牌。
        c2,//对子。
        c3,//3不带。
        c4,//炸弹。
        c31,//3带1。
        c32,//3带2。
        c411,//4带2个单，或者一对
        c422,//4带2对
        c123,//连子。
        c1122,//连队。
        c111222,//飞机。
        c11122234,//飞机带单排.
        c1112223344,//飞机带对子.
        c0//不能出牌
    }

    public static String listToString(List<Integer> list) {

        if (list == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        //第一个前面不拼接","
        for (Integer integer : list) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(integer);
        }
        return result.toString();
    }


    public static List<Integer> stringToList(String strs) {
        String str[] = strs.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : str) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }


    public static String sort(List<Integer> cardlist) {

        Collections.sort(cardlist, (o1, o2) -> {
            if ((o1 == 52 || o1 == 53) && (o2 == 52 || o2 == 53)) {
                if (o1 > o2) {
                    return -1;
                } else {
                    return 1;
                }
            }
            if (o1 == 52 || o1 == 53) return -1;

            if (o2 == 52 || o2 == 53) return 1;


            if (o1 % 13 == o2 % 13) {
                if (o1 > o2) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (o1 % 13 > o2 % 13) {
                return -1;
            } else {
                return 1;
            }
        });

        return listToString(cardlist);

    }


}
