package com.zq.shop.web.common;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/4/6 1:23 PM
 * @Package com.zq.shop.web.common
 **/


@Slf4j
public class DDZ_CardType {
    public enum DDZ_POKER_TYPE {
        DDZ_PASS(0),    //过牌，不出
        SINGLE(1),
        TWIN(2),
        TRIPLE(3),
        TRIPLE_WITH_SINGLE(4),
        TRIPLE_WITH_TWIN(5),
        STRAIGHT_SINGLE(6),
        STRAIGHT_TWIN(7),
        PLANE_PURE(8),
        PLANE_WITH_SINGLE(9),
        PLANE_WITH_TWIN(10),
        FOUR_WITH_SINGLE(11),
        FOUR_WITH_TWIN(12),
        FOUR_BOMB(13),
        KING_BOMB(14);
        private int num;

        DDZ_POKER_TYPE(int num) {
            this.num = num;
        }
    }

    /// <summary>
    /// 是否是单
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsSingle(List<Integer> cards) {
        if (cards.size() == 1)
            return true;
        else
            return false;
    }

    /// <summary>
    /// 是否是对子
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsDouble(List<Integer> cards) {
        if (cards.size() == 2) {
            if (cards.get(0) == cards.get(1))
                return true;
        }

        return false;
    }

    /// <summary>
    /// 是否是顺子
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsStraight(List<Integer> cards) {
        if (cards.size() < 5 || cards.size() > 12)
            return false;
        for (int i = 0; i < cards.size() - 1; i++) {
            int w = cards.get(i);
            if (cards.get(i + 1) - w != 1)
                return false;

            //不能超过A
            if (w > 12 || cards.get(i + 1) > 12)
                return false;
        }

        return true;
    }

    /// <summary>
    /// 是否是双顺子
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsDoubleStraight(List<Integer> cards) {
        if (cards.size() < 6 || cards.size() % 2 != 0)
            return false;

        for (int i = 0; i < cards.size(); i += 2) {
            if (cards.get(i + 1) != cards.get(i))
                return false;

            if (i < cards.size() - 2) {
                if (cards.get(i + 2) - cards.get(i) != 1)
                    return false;

                //不能超过A
                if (cards.get(i) > 12 || cards.get(i + 2) > 12)
                    return false;
            }
        }

        return true;
    }


    /// <summary>
    /// 三不带
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsOnlyThree(List<Integer> cards) {
        if (cards.size() % 3 != 0)
            return false;
        if (cards.get(0) != cards.get(1))
            return false;
        if (cards.get(1) != cards.get(2))
            return false;
        if (cards.get(0) != cards.get(2))
            return false;

        return true;
    }


    /// <summary>
    /// 三带一
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsThreeAndOne(List<Integer> cards) {
        if (cards.size() != 4)
            return false;

        if (cards.get(0) == cards.get(1) &&
                cards.get(1) == cards.get(2))
            return true;
        else if (cards.get(1) == cards.get(2) &&
                cards.get(2) == cards.get(3))
            return true;
        return false;
    }

    /// <summary>
    /// 三代二
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsThreeAndTwo(List<Integer> cards) {
        if (cards.size() != 5)
            return false;

        if (cards.get(0) == cards.get(1) &&
                cards.get(1) == cards.get(2)) {
            if (cards.get(3) == cards.get(4))
                return true;
        } else if (cards.get(2) == cards.get(3) &&
                cards.get(3) == cards.get(4)) {
            if (cards.get(0) == cards.get(1))
                return true;
        }

        return false;
    }

    /// <summary>
    /// 炸弹
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsBoom(List<Integer> cards) {
        if (cards.size() != 4)
            return false;

        if (cards.get(0) != cards.get(1))
            return false;
        if (cards.get(1) != cards.get(2))
            return false;
        if (cards.get(2) != cards.get(3))
            return false;

        return true;
    }


    /// <summary>
    /// 王炸
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsJokerBoom(List<Integer> cards) {
        if (cards.size() != 2)
            return false;
        if (cards.get(0) == 52) {
            if (cards.get(1) == 53)
                return true;
            return false;
        } else if (cards.get(0) == 53) {
            if (cards.get(1) == 52)
                return true;
            return false;
        }

        return false;
    }

    /// <summary>
    /// 飞机不带
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsTripleStraight(List<Integer> cards) {
        if (cards.size() < 6 || cards.size() % 3 != 0)
            return false;

        for (int i = 0; i < cards.size(); i += 3) {
            if (cards.get(i + 1) != cards.get(i))
                return false;
            if (cards.get(i + 2) != cards.get(i))
                return false;
            if (cards.get(i + 1) != cards.get(i + 2))
                return false;

            if (i < cards.size() - 3) {
                if (cards.get(i + 3) - cards.get(i) != 1)
                    return false;

                //不能超过A
                if (cards.get(i) > 12 || cards.get(i + 3) > 12)
                    return false;
            }
        }

        return true;
    }

    /// <summary>
    /// 飞机带单
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean isPlaneWithSingle(List<Integer> cards) {
        if (!HaveFour(cards)) {
            List<Integer> tempThreeList = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                int tempInt = 0;
                for (int j = 0; j < cards.size(); j++) {

                    if (cards.get(i) == cards.get(j)) {
                        tempInt++;
                    }

                }
                if (tempInt == 3) {
                    tempThreeList.add(cards.get(i));
                }
            }
            if (tempThreeList.size() % 3 != cards.size() % 4) {

                return false;
            } else {
                if (IsTripleStraight(tempThreeList)) {
                    return true;
                } else {

                    return false;
                }
            }
        }

        return false;
    }

    /// <summary>
    /// 飞机带双
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean isPlaneWithTwin(List<Integer> cards) {
        if (!HaveFour(cards)) {
            List<Integer> tempThreeList = new ArrayList<>();
            List<Integer> tempTwoList = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                int tempInt = 0;
                for (int j = 0; j < cards.size(); j++) {

                    if (cards.get(i) == cards.get(j)) {
                        tempInt++;
                    }

                }
                if (tempInt == 3) {
                    tempThreeList.add(cards.get(i));
                } else if (tempInt == 2) {
                    tempTwoList.add(cards.get(i));
                }

            }
            if (tempThreeList.size() % 3 != cards.size() % 5 && tempTwoList.size() % 2 != cards.size() % 5) {

                return false;
            } else {
                if (IsTripleStraight(tempThreeList)) {
                    if (IsAllDouble(tempTwoList)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {

                    return false;
                }
            }
        }
        return false;
    }

    /// <summary>
    /// 判断牌里面是否是拥有4张牌
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean HaveFour(List<Integer> cards) {

        for (int i = 0; i < cards.size(); i++) {
            int tempInt = 0;
            for (int j = 0; j < cards.size(); j++) {

                if (cards.get(i) == cards.get(j)) {
                    tempInt++;
                }
            }
            if (tempInt == 4) {
                return true;
            }
        }
        return false;
    }

    /// <summary>
    /// 判断牌里面全是对子
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean IsAllDouble(List<Integer> cards) {
        for (int i = 0; i < cards.size() % 2; i += 2) {
            if (cards.get(i) != cards.get(i + 1)) {
                return false;
            }
        }
        return true;
    }


    /// <summary>
    /// 判断是否是四带二
    /// </summary>
    /// <param name="cards"></param>
    /// <returns></returns>
    public static boolean isSiDaiEr(List<Integer> cards) {
        boolean flag = false;
        if (cards != null && cards.size() == 6) {


            for (int i = 0; i < 3; i++) {
                int grade1 = cards.get(i);
                int grade2 = cards.get(i + 1);
                int grade3 = cards.get(i + 2);
                int grade4 = cards.get(i + 3);

                if (grade2 == grade1 && grade3 == grade1 && grade4 == grade1) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /// <summary>
    /// 判断是否符合出牌规则
    /// </summary>
    /// <param name="cards"></param>
    /// <param name="type"></param>
    /// <returns></returns>
    public static DDZ_POKER_TYPE PopEnable(List<Integer> cardlist) {
        DDZ_POKER_TYPE type = DDZ_POKER_TYPE.DDZ_PASS;
        List<Integer> cards = new ArrayList<>();
        for (Integer i : cardlist) {
            if (i == 52 || i == 53) {
                cards.add(i);
            } else {
                cards.add(i % 13);
            }
        }

        Collections.sort(cards);

        boolean isRule = false;
        switch (cards.size()) {
            case 1:
                isRule = true;
                type = DDZ_POKER_TYPE.SINGLE;
                break;
            case 2:
                if (IsDouble(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.TRIPLE;
                } else if (IsJokerBoom(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.KING_BOMB;
                }
                break;
            case 3:
                if (IsOnlyThree(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.TRIPLE;
                }
                break;
            case 4:
                if (IsBoom(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.FOUR_BOMB;
                } else if (IsThreeAndOne(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.TRIPLE_WITH_SINGLE;
                }

                break;
            case 5:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsThreeAndTwo(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.TRIPLE_WITH_TWIN;
                }
                break;
            case 6:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsTripleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_PURE;
                } else if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isSiDaiEr(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.FOUR_WITH_SINGLE;   //四带二
                }
                break;
            case 7:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                }
                break;
            case 8:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isPlaneWithSingle(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_SINGLE;   //飞机带单
                }
                break;
            case 9:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsTripleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_PURE;
                }
                break;
            case 10:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isPlaneWithTwin(cards))           //飞机带对
                {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_TWIN;
                }
                break;

            case 11:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                }
                break;
            case 12:
                if (IsStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_SINGLE;
                } else if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isPlaneWithSingle(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_SINGLE;   //飞机带单
                } else if (IsTripleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_PURE;
                }
                break;
            case 13:
                break;
            case 14:
                if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                }
                break;
            case 15:
                if (IsTripleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_PURE;
                } else if (isPlaneWithTwin(cards))           //飞机带对
                {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_TWIN;
                }
                break;
            case 16:
                if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isPlaneWithSingle(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_SINGLE;   //飞机带单
                }
                break;
            case 17:
                break;
            case 18:
                if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (IsTripleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_PURE;
                }
                break;
            case 19:
                break;

            case 20:
                if (IsDoubleStraight(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.STRAIGHT_TWIN;
                } else if (isPlaneWithSingle(cards)) {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_SINGLE;   //飞机带单
                } else if (isPlaneWithTwin(cards))           //飞机带对
                {
                    isRule = true;
                    type = DDZ_POKER_TYPE.PLANE_WITH_TWIN;
                }
                break;
            default:
                break;
        }

        return type;
    }


    public static boolean isSelectCardCanPut(List<Integer> myCardslist, DDZ_POKER_TYPE myCardType, List<Integer> lastCardslist, DDZ_POKER_TYPE lastCardTye) {
        List<Integer> myCards = new ArrayList<>();
        List<Integer> lastCards = new ArrayList<>();


        for (Integer i : myCardslist) {
            if (i == 52 || i == 53) {
                myCards.add(i);
            } else {
                myCards.add(i % 13);
            }
        }

        Collections.sort(myCards);
        
        
        for (Integer i : lastCardslist) {
            if (i == 52 || i == 53) {
                lastCards.add(i);
            } else {
                lastCards.add(i % 13);
            }
        }

        Collections.sort(lastCards);
        
        
        


        // 我的牌和上家的牌都不能为null
        if (myCards.size() == 0 || lastCards.size() == 0) {
            return false;
        }

        if (myCardType == null || lastCardTye == null) {
            log.error("上家出的牌不合法，所以不能出。");
            return false;
        }

        // 上一首牌的个数
        int prevSize = lastCards.size();
        int mySize = myCards.size();

        // 我先出牌，上家没有牌
        if (prevSize == 0 && mySize != 0) {
            return true;
        }

        // 集中判断是否王炸，免得多次判断王炸
        if (lastCardTye == DDZ_POKER_TYPE.KING_BOMB) {
            log.error("上家王炸，肯定不能出。");
            return false;
        } else if (myCardType == DDZ_POKER_TYPE.KING_BOMB) {
            log.error("我王炸，肯定能出。");
            return true;
        }

        // 集中判断对方不是炸弹，我出炸弹的情况
        if (lastCardTye != DDZ_POKER_TYPE.FOUR_BOMB && myCardType == DDZ_POKER_TYPE.FOUR_BOMB) {
            return true;
        }

        //所有牌提前排序过了

        int myGrade = myCards.get(0);
        int prevGrade = lastCards.get(0);

        // 比较2家的牌，主要有2种情况，1.我出和上家一种类型的牌，即对子管对子；
        // 2.我出炸弹，此时，和上家的牌的类型可能不同
        // 王炸的情况已经排除

        // 单
        if (lastCardTye == DDZ_POKER_TYPE.SINGLE && myCardType == DDZ_POKER_TYPE.SINGLE) {
            // 一张牌可以大过上家的牌
            return compareGrade(myGrade, prevGrade);
        }
        // 对子
        else if (lastCardTye == DDZ_POKER_TYPE.TWIN
                && myCardType == DDZ_POKER_TYPE.TWIN) {
            // 2张牌可以大过上家的牌
            return compareGrade(myGrade, prevGrade);

        }
        // 3不带
        else if (lastCardTye == DDZ_POKER_TYPE.TRIPLE
                && myCardType == DDZ_POKER_TYPE.TRIPLE) {
            // 3张牌可以大过上家的牌
            return compareGrade(myGrade, prevGrade);
        }
        // 炸弹
        else if (lastCardTye == DDZ_POKER_TYPE.FOUR_BOMB
                && myCardType == DDZ_POKER_TYPE.FOUR_BOMB) {
            // 4张牌可以大过上家的牌
            return compareGrade(myGrade, prevGrade);

        }
        // 3带1
        else if (lastCardTye == DDZ_POKER_TYPE.TRIPLE_WITH_SINGLE) {

            // 3带1只需比较第2张牌的大小
            myGrade = myCards.get(1);
            prevGrade = lastCards.get(1);
            return compareGrade(myGrade, prevGrade);

        } else if (lastCardTye == DDZ_POKER_TYPE.TRIPLE_WITH_TWIN) {

            // 3带2只需比较第3张牌的大小
            myGrade = myCards.get(2);
            prevGrade = lastCards.get(2);
            return compareGrade(myGrade, prevGrade);

        }

        // 4带2
        else if (lastCardTye == DDZ_POKER_TYPE.FOUR_WITH_SINGLE
                && myCardType == DDZ_POKER_TYPE.FOUR_WITH_SINGLE) {

            // 4带2只需比较第3张牌的大小
            myGrade = myCards.get(2);
            prevGrade = lastCards.get(2);

        }
        // 4带2对子
        else if (lastCardTye == DDZ_POKER_TYPE.FOUR_WITH_SINGLE
                && myCardType == DDZ_POKER_TYPE.FOUR_WITH_SINGLE) {


            myGrade = myCards.get(2);
            prevGrade = lastCards.get(2);

        }
        // 顺子
        else if (lastCardTye == DDZ_POKER_TYPE.STRAIGHT_SINGLE
                && myCardType == DDZ_POKER_TYPE.STRAIGHT_SINGLE) {
            if (mySize != prevSize) {
                return false;
            } else {
                // 顺子只需比较最大的1张牌的大小
                myGrade = myCards.get(mySize - 1);
                prevGrade = lastCards.get(prevSize - 1);
                return compareGrade(myGrade, prevGrade);
            }

        }
        // 连对
        else if (lastCardTye == DDZ_POKER_TYPE.STRAIGHT_TWIN
                && myCardType == DDZ_POKER_TYPE.STRAIGHT_TWIN) {
            if (mySize != prevSize) {
                return false;
            } else {
                // 顺子只需比较最大的1张牌的大小
                myGrade = myCards.get(mySize - 1);
                prevGrade = lastCards.get(prevSize - 1);
                return compareGrade(myGrade, prevGrade);
            }

        }
        // 飞机不带
        else if (lastCardTye == DDZ_POKER_TYPE.PLANE_PURE
                && myCardType == DDZ_POKER_TYPE.PLANE_PURE) {
            if (mySize != prevSize) {
                return false;
            } else {
                //333444555666算飞机不带 不算飞机带单
                myGrade = myCards.get(4);
                prevGrade = lastCards.get(4);
                return compareGrade(myGrade, prevGrade);
            }
        }
        //飞机带单
        else if (lastCardTye == DDZ_POKER_TYPE.PLANE_WITH_SINGLE
                && myCardType == DDZ_POKER_TYPE.PLANE_WITH_SINGLE) {
            if (mySize != prevSize) {
                return false;
            } else {
                List<Integer> tempThreeList = new ArrayList<>();
                for (int i = 0; i < myCards.size(); i++) {
                    int tempInt = 0;
                    for (int j = 0; j < myCards.size(); j++) {

                        if (myCards.get(i) == myCards.get(j)) {
                            tempInt++;
                        }

                    }
                    if (tempInt == 3) {
                        tempThreeList.add(myCards.get(i));
                    }
                }
                myGrade = tempThreeList.get(4);
                prevGrade = lastCards.get(4);
                return compareGrade(myGrade, prevGrade);
            }
        }
        //飞机带双
        else if (lastCardTye == DDZ_POKER_TYPE.PLANE_WITH_TWIN
                && myCardType == DDZ_POKER_TYPE.PLANE_WITH_TWIN) {
            if (mySize != prevSize) {
                return false;
            } else {
                List<Integer> tempThreeList = new ArrayList<>();
                List<Integer> tempTwoList = new ArrayList<>();
                for (int i = 0; i < myCards.size(); i++) {
                    int tempInt = 0;
                    for (int j = 0; j < myCards.size(); j++) {

                        if (myCards.get(i) == myCards.get(j)) {
                            tempInt++;
                        }

                    }
                    if (tempInt == 3) {
                        tempThreeList.add(myCards.get(i));
                    } else if (tempInt == 2) {
                        tempTwoList.add(myCards.get(i));
                    }

                }
                myGrade = tempThreeList.get(4);
                prevGrade = lastCards.get(4);
                if (compareGrade(myGrade, prevGrade)) {
                    return DDZ_CardType.IsAllDouble(tempTwoList);
                }
            }
        }

        // 默认不能出牌
        return false;
    }

    private static boolean compareGrade(int myGrade, int prevGrade) {
        return myGrade > prevGrade;
    }
}
