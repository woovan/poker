package com.woovan.poker.utils;

import java.util.ArrayList;
import java.util.List;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;
import com.woovan.poker.model.CardSuit;
import com.woovan.poker.model.HandPower;
import com.woovan.poker.model.HandType;

public class HandPowerUtil {

	public static HandPower calculate(List<Card> cards) {
		
		//将扑克牌从大到小排序
		List<Card> sortedCards = CardUtil.sortDesc(cards);
		
		//根据Number和花色分组，MapList内部用LinkedHashMap实现，所以分组是按扑克Number从大到小遍历
		MapList<CardNumber, Card> numberGroup = getNumberGroup(sortedCards);
		MapList<CardSuit, Card> suitGroup = getSuitGroup(sortedCards);
		
		//获得同花数量大于等于5的卡牌
		List<Card> flushCards = getFlushCards(suitGroup);
		if (flushCards != null) {
			List<Card> straightFlush = getStraightFlush(flushCards);
			if (straightFlush != null) {
				if (straightFlush.get(0).is(CardNumber.ACE)) {
					return new HandPower(HandType.ROYAL_FLUSH, straightFlush);
				} else {
					return new HandPower(HandType.STRAIGHT_FLUSH, straightFlush);
				}
			}
		}
		
		List<Card> fourOfAKind = getCardsByCount(4, numberGroup);
		if (fourOfAKind != null) {
			return new HandPower(HandType.FOUR_OF_A_KIND, fourOfAKind);
		}
		
		List<Card> threeOfAKind = getCardsByCount(3, numberGroup);
		if (threeOfAKind != null) {
			return new HandPower(HandType.FOUR_OF_A_KIND, fourOfAKind);
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		List<Card> cards = CardUtil.of("AsKsQsJsTs9s8s7s5s4s3s2s");
		System.out.println(getStraight(cards));
	}
	
	/**
	 * 从同花牌中获得最大同花顺
	 * @param suitGroup
	 * @return
	 */
	private static List<Card> getStraightFlush(List<Card> flushCards) {
        return getStraight(flushCards);
    }
	
	private static List<Card> getFullHouse(MapList<CardNumber, Card> numberGroup) {
		List<Card> fullHouse = new ArrayList<Card>();
		
		List<Card> threeOfAKind = getCardsByCount(3, numberGroup);
		if (threeOfAKind != null) {
			fullHouse.addAll(threeOfAKind);
		}
		
		return null;
	}
	
	/**
	 * 获得扑克牌中最大顺子
	 * @param cards
	 * @return
	 */
	private static List<Card> getStraight(List<Card> cards) {
		if (cards == null || cards.size() < 5) {
			return null;
		}
		if (cards.get(0).is(CardNumber.ACE)) {
			List<Card> tmpCards = new ArrayList<Card>(cards);
			tmpCards.add(cards.get(0));
			cards = tmpCards;
		}
		for (int i = 0; i < cards.size() - 5 + 1; i++) {
        	List<Card> subList = cards.subList(i, i + 5);
        	if (CardUtil.isConnecting(subList, false)) {
        		return new ArrayList<Card>(subList);
        	}
		}
		return null;
	}
	
	/**
	 * 获得数量为count的最大卡牌集合
	 * @param count
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getCardsByCount(Integer count, MapList<CardNumber, Card> numberGroup) {
        for (List<Card> cards : numberGroup.values()) {
            if (cards.size() == count) {
                return new ArrayList<Card>(cards);
            }
        }
        return null;
    }
	
	/**
	 * 获得同花的所有扑克牌
	 * @param suitGroup
	 * @return
	 */
	private static List<Card> getFlushCards(MapList<CardSuit, Card> suitGroup) {
        for (List<Card> cards : suitGroup) {
            if (cards.size() >= 5) {
                return cards;
            }
        }
        return null;
    }
	
	/**
	 * 扑克牌按Number分组
	 * @param cards
	 * @return
	 */
	private static MapList<CardNumber, Card> getNumberGroup(List<Card> cards) {
		MapList<CardNumber, Card> numberGroup = new MapList<CardNumber, Card>();
		for (Card card : cards) {
		    numberGroup.put(card.getNumber(), card);
		}
		return numberGroup;
	}
	
	/**
	 * 扑克牌按花色分组
	 * @param cards
	 * @return
	 */
	private static MapList<CardSuit, Card> getSuitGroup(List<Card> cards) {
		MapList<CardSuit, Card> suitGroup = new MapList<CardSuit, Card>();
		for (Card card : cards) {
		    suitGroup.put(card.getSuit(), card);
		}
		return suitGroup;
	}
}
