package com.woovan.poker.calculate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;
import com.woovan.poker.model.CardSuit;
import com.woovan.poker.model.Deck;
import com.woovan.poker.model.HandPower;
import com.woovan.poker.model.HandType;
import com.woovan.poker.utils.CardUtil;
import com.woovan.poker.utils.MapList;

public class HandPowerUtil {
	
	public static void main(String[] args) {
		play(2000000);
	}
	
	public static void play(long totalCount) {
		
		long beginTm = System.currentTimeMillis();
		Map<HandType, Long> stat = new HashMap<HandType, Long>();
		
		Deck deck = new Deck();
		for (int i = 0; i < totalCount; i++) {
			deck.shuffle();
			List<Card> cards = deck.show(7);
			HandPower handPower = calculate(cards);
			Long count = stat.get(handPower.getHandType());
			if (count == null) {
				count = 0l;
			}
			stat.put(handPower.getHandType(), count + 1);
		}
		for (HandType type : HandType.values()) {
			Long count = stat.get(type);
			BigDecimal rate = BigDecimal.ZERO;
			if (count != null) {
				rate = new BigDecimal(count).divide(new BigDecimal(totalCount), 7, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			} 
			System.out.println(String.format("%s:%d:%.5f%%", type.name(), count != null ? count : 0l, rate.doubleValue()));
		}
		
		long cost = System.currentTimeMillis() - beginTm;
		System.out.println(String.format("cost %dms", cost));
	}
	
	public static HandPower calculate(List<Card> hands, List<Card> broad) {
		List<Card> cards = new ArrayList<Card>(hands);
		cards.addAll(broad);
		return calculate(cards);
	}

	public static HandPower calculate(List<Card> hands) {
		
		//将扑克牌从大到小排序
		CardUtil.sortDesc(hands);
		
		//根据Number和花色分组，MapList内部用LinkedHashMap实现，所以分组和组内列表都是按扑克Number从大到小排列
		MapList<CardNumber, Card> numberGroup = getNumberGroup(hands);
		MapList<CardSuit, Card> suitGroup = getSuitGroup(hands);
		MapList<Integer, List<Card>> countGroup = getCountGroup(numberGroup);
		
		//同花顺
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
		
		//四条
		if (countGroup.containsKey(4)) {
			List<Card> fourOfAKind = countGroup.get(4).get(0);
			completeCardsWithSingleCard(fourOfAKind, numberGroup);
			return new HandPower(HandType.FOUR_OF_A_KIND, fourOfAKind);
		}
		
		//葫芦
		if (countGroup.containsKey(3) && countGroup.containsKey(2)) {
			List<Card> fullHouse = getFullHouse(countGroup);
			return new HandPower(HandType.FULL_HOUSE, fullHouse);
		}
		
		//同花
		if (flushCards != null) {
			List<Card> flush = getFlush(flushCards);
			return new HandPower(HandType.FLUSH, flush);
		}
		
		//顺子
		List<Card> straight = getStraight(numberGroup);
		if (straight != null) {
			return new HandPower(HandType.STRAIGHT, straight);
		}
		
		//三条
		if (countGroup.containsKey(3)) {
			List<Card> threeOfAKind = countGroup.get(3).get(0);
			completeCardsWithSingleCard(threeOfAKind, numberGroup);
			return new HandPower(HandType.THREE_OF_A_KIND, threeOfAKind);
		}
		
		//对子
		if (countGroup.containsKey(2)) {
			List<List<Card>> pairList = countGroup.get(2);
			List<Card> pairs = getPairs(pairList, numberGroup);
			if (pairList.size() >= 2) {
				return new HandPower(HandType.TWO_PAIR, pairs);
			} else {
				return new HandPower(HandType.ONE_PAIR, pairs);
			}
		}
		
		//高牌
		List<Card> highCard = hands.subList(0, 5);
		return new HandPower(HandType.HIGH_CARD, highCard);
	}
	
	/**
	 * 从同花牌中获得最大同花顺
	 * @param flushCards 同花扑克牌组(牌数>=5)
	 * @return
	 */
	private static List<Card> getStraightFlush(List<Card> flushCards) {
		if (flushCards.get(0).is(CardNumber.ACE) && flushCards.get(flushCards.size() - 1).is(CardNumber.TWO)) {
			List<Card> tmpCards;
			if (flushCards.get(1).is(CardNumber.KING)) {
				tmpCards = new ArrayList<Card>(flushCards);
			} else {
				tmpCards = new ArrayList<Card>(flushCards.subList(1, flushCards.size()));
			}
			tmpCards.add(flushCards.get(0));
			flushCards = tmpCards;
		}
		if (flushCards.size() == 5) {
			if (CardUtil.isCardsConnecting(flushCards)) {
        		return flushCards;
        	}
		} else {
			for (int i = 0; i < flushCards.size() - 5 + 1; i++) {
	        	List<Card> subList = flushCards.subList(i, i + 5);
	        	if (CardUtil.isCardsConnecting(subList)) {
	        		return subList;
	        	}
			}
		}
		return null;
    }
	
	/**
	 * 用单张补全牌组
	 * @param numberGroup
	 * @return
	 */
	private static void completeCardsWithSingleCard(List<Card> cardList, MapList<CardNumber, Card> numberGroup) {
		CardNumber cardNumber = cardList.get(0).getNumber();
		for (List<Card> cards : numberGroup) {
			if (cards.get(0).getNumber() != cardNumber) {
				cardList.add(cards.get(0));
			}
			if (cardList.size() == 5) break;
		}
	}
	
	/**
	 * 获得葫芦
	 * @param countGroup
	 * @return
	 */
	private static List<Card> getFullHouse(MapList<Integer, List<Card>> countGroup) {
		List<Card> threeOfAKind = countGroup.get(3).get(0);
		List<Card> pair = countGroup.get(2).get(0);
		
		List<Card> fullHouse = new ArrayList<Card>();
		fullHouse.addAll(threeOfAKind);
		fullHouse.addAll(pair);
		return fullHouse;
	}
	
	/**
	 * 从同花卡牌中获得最大的同花组合
	 * @param flushCards
	 * @return
	 */
	private static List<Card> getFlush(List<Card> flushCards) {
		return flushCards.size() == 5 ? flushCards : flushCards.subList(0, 5);
	}
	
	/**
	 * 从分组中获得同花
	 * @param suitGroup
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<Card> getFlush(MapList<CardSuit, Card> suitGroup) {
		for (List<Card> cards : suitGroup) {
            if (cards.size() >= 5) {
                return getFlush(cards);
            }
        }
        return null;
	}
	
	/**
	 * 获得分组中最大的顺子
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getStraight(MapList<CardNumber, Card> numberGroup) {
		if (numberGroup.size() < 5) {
			return null;
		}
		
		List<CardNumber> cardNumbers = new ArrayList<CardNumber>(numberGroup.keySet());
		if (cardNumbers.get(0) == CardNumber.ACE && cardNumbers.get(cardNumbers.size() - 1) == CardNumber.TWO) {
			List<CardNumber> tmpCardNumbers;
			if (cardNumbers.get(1) == CardNumber.KING) {
				tmpCardNumbers = new ArrayList<CardNumber>(cardNumbers);
			} else {
				tmpCardNumbers = new ArrayList<CardNumber>(cardNumbers.subList(1, cardNumbers.size()));
			}
			tmpCardNumbers.add(cardNumbers.get(0));
			cardNumbers = tmpCardNumbers;
		}
		
		List<CardNumber> straightNumbers = null;
		if (cardNumbers.size() == 5) {
			if (CardUtil.isConnecting(cardNumbers)) {
				straightNumbers = cardNumbers;
			}
		} else {
			for (int i = 0; i < cardNumbers.size() - 5 + 1; i++) {
	        	List<CardNumber> subList = cardNumbers.subList(i, i + 5);
	        	if (CardUtil.isConnecting(subList)) {
	        		straightNumbers = new ArrayList<CardNumber>(subList);
	        		break;
	        	}
			}
		}
		if (straightNumbers != null) {
			List<Card> straight = new ArrayList<Card>();
			for (CardNumber cardNumber : straightNumbers) {
				straight.add(numberGroup.get(cardNumber).get(0));
			}
			return straight;
		}
		return null;
	}
	
	/**
	 * 获得分组中的对子成牌
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getPairs(List<List<Card>> pairList, MapList<CardNumber, Card> numberGroup) {
        List<Card> pairs = new ArrayList<Card>();
        List<CardNumber> pairCardNumbers = new ArrayList<CardNumber>();
        for (List<Card> pair : pairList) {
        	pairs.addAll(pair);
        	pairCardNumbers.add(pair.get(0).getNumber());
        	if (pairs.size() >= 4) break;
        }
        for (List<Card> cards : numberGroup) {
    		if (!pairCardNumbers.contains(cards.get(0).getNumber())) {
    			pairs.add(cards.get(0));
    		}
    		if (pairs.size() >= 5) break;
        }
        return pairs;
    }
	
	/**
	 * 获得同花数量大于等于5的卡牌
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
	
	/**
	 * 按CardNumber相同的张数分组  例 <4, [[Ah,As,Ac,Ad]]>  <2, [[Ks,Kd],[8c,8d]]>
	 * @param numberGroup
	 * @return
	 */
	private static MapList<Integer, List<Card>> getCountGroup(MapList<CardNumber, Card> numberGroup) {
		MapList<Integer, List<Card>> countGroup = new MapList<Integer, List<Card>>();
		for (List<Card> cards : numberGroup) {
			if (cards.size() > 1) {
				countGroup.put(cards.size(), cards);
			}
		}
		return countGroup;
	}
}
