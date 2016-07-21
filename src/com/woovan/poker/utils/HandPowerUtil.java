package com.woovan.poker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;
import com.woovan.poker.model.CardSuit;
import com.woovan.poker.model.Deck;
import com.woovan.poker.model.HandPower;
import com.woovan.poker.model.HandType;

public class HandPowerUtil {
	
	public static void main(String[] args) {
//		List<Card> cards = CardUtil.of("AsKsQsJsTs9s8s7s5s4s3s2s");
		Deck deck = new Deck();
		List<Card> cards = deck.deal(7);
		System.out.println(cards);
		System.out.println(CardUtil.sortDesc(cards));
		System.out.println(calculate(cards));
	}

	public static HandPower calculate(List<Card> cards) {
		
		//将扑克牌从大到小排序
		List<Card> sortedCards = CardUtil.sortDesc(cards);
		
		//根据Number和花色分组，MapList内部用LinkedHashMap实现，所以分组是按扑克Number从大到小遍历
		MapList<CardNumber, Card> numberGroup = getNumberGroup(sortedCards);
		MapList<CardSuit, Card> suitGroup = getSuitGroup(sortedCards);
		
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
		List<Card> fourOfAKind = getCardsByCount(4, numberGroup);
		if (fourOfAKind != null) {
			return new HandPower(HandType.FOUR_OF_A_KIND, fourOfAKind);
		}
		
		//葫芦
		List<Card> fullHouse = getFullHouse(numberGroup);
		if (fullHouse != null) {
			return new HandPower(HandType.FULL_HOUSE, fullHouse);
		}
		
		//同花
		if (flushCards != null) {
			List<Card> flush = getFlush(flushCards);
			return new HandPower(HandType.FLUSH, flush);
		}
		
		//顺子
		List<Card> straight = getStraight(sortedCards);
		if (straight != null) {
			return new HandPower(HandType.STRAIGHT, straight);
		}
		
		//三条
		List<Card> threeOfAKind = getCardsByCount(3, numberGroup);
		if (threeOfAKind != null) {
			return new HandPower(HandType.FOUR_OF_A_KIND, threeOfAKind);
		}
		
		//对子
		int pairCount = getPairCount(numberGroup);
		if (pairCount > 0) {
			List<Card> pairs = getPairs(numberGroup);
			if (pairCount >= 2) {
				return new HandPower(HandType.TWO_PAIR, pairs);
			}
			return new HandPower(HandType.ONE_PAIR, pairs);
		}
		
		//高牌
		List<Card> highCard = sortedCards.subList(0, 5);
		return new HandPower(HandType.HIGH_CARD, highCard);
	}
	
	/**
	 * 从同花牌中获得最大同花顺
	 * @param suitGroup
	 * @return
	 */
	private static List<Card> getStraightFlush(List<Card> flushCards) {
        return getStraight(flushCards);
    }
	
	/**
	 * 从分组中获取最大的葫芦
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getFullHouse(MapList<CardNumber, Card> numberGroup) {
		List<Card> fullHouse = new ArrayList<Card>();
		
		List<Card> threeOfAKind = getCardsByCount(3, numberGroup);
		if (threeOfAKind != null) {
			fullHouse.addAll(threeOfAKind);
			
			CardNumber cardNumber = threeOfAKind.get(0).getNumber();
			for (Map.Entry<CardNumber, List<Card>> entry : numberGroup.entrySet()) {
				if (entry.getValue().size() >= 2 && entry.getKey() != cardNumber) {
					if (entry.getValue().size() == 2) {
						fullHouse.addAll(entry.getValue());
					} else {
						fullHouse.addAll(entry.getValue().subList(0, 2));
					}
					return fullHouse;
				}
			}
		}
		return null;
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
	 * 获得分组中对子的数量
	 * @param numberGroup
	 * @return
	 */
	private static int getPairCount(MapList<CardNumber, Card> numberGroup) {
		int pairCount = 0;
		for (List<Card> cards : numberGroup) {
			if (cards.size() == 2) {
				pairCount ++;
			}
		}
		return pairCount;
	}
	
	/**
	 * 获得分组中的对子成牌
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getPairs(MapList<CardNumber, Card> numberGroup) {
        List<Card> pairs = new ArrayList<Card>();
        List<CardNumber> pairsCardNumber = new ArrayList<CardNumber>();
        for (List<Card> cards : numberGroup) {
            if (cards.size() == 2 && pairs.size() < 4) {
            	pairs.addAll(cards);
            	pairsCardNumber.add(cards.get(0).getNumber());
            }
        }
        for (List<Card> cards : numberGroup) {
        	if (pairs.size() < 5) {
        		if (!pairsCardNumber.contains(cards.get(0).getNumber())) {
        			pairs.add(cards.get(0));
        		}
        	} else {
        		break;
        	}
        }
        return pairs;
    }
	
	/**
	 * 获得数量为count的最大卡牌集合
	 * @param count
	 * @param numberGroup
	 * @return
	 */
	private static List<Card> getCardsByCount(Integer count, MapList<CardNumber, Card> numberGroup) {
        for (List<Card> cards : numberGroup) {
            if (cards.size() == count) {
                return new ArrayList<Card>(cards);
            }
        }
        return null;
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
}
