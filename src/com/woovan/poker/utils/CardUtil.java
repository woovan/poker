package com.woovan.poker.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;

public class CardUtil {

	/**
	 * 形如"AsKhQcJdTs"的字符串转扑克列表
	 * @param cardSymbols
	 * @return
	 */
	public static List<Card> of(String cardSymbols) {
		List<Card> cards = new ArrayList<Card>();
		if (cardSymbols != null && cardSymbols.length() % 2 == 0) {
			for (int i = 0; i < cardSymbols.length(); i+=2) {
				Card card = new Card(cardSymbols.substring(i, i + 2));
				cards.add(card);
			}
		}
		return cards;
	}
	
	/**
	 * 扑克转扑克编号
	 * @param cards
	 * @return
	 */
	public List<CardNumber> toCardNumber(List<Card> cards) {
        List<CardNumber> cardNumbers = new ArrayList<CardNumber>();

        for (Card card : cards) {
            cardNumbers.add(card.getNumber());
        }
        return cardNumbers;
    }
	
	/**
	 * 扑克牌正序排列(从小到大)
	 * @param cards
	 * @return
	 */
	public static List<Card> sort(List<Card> cards) {
		List<Card> sortedCards = new ArrayList<Card>(cards);
		Collections.sort(sortedCards);
		return sortedCards;
	}
	
	/**
	 * 扑克牌逆序排列(从大到小)
	 * @param cards
	 * @return
	 */
	public static List<Card> sortDesc(List<Card> cards) {
		List<Card> sortedCards = new ArrayList<Card>(cards);
		Collections.sort(sortedCards, new Comparator<Card>() {

			@Override
			public int compare(Card card1, Card card2) {
				return card2.compareTo(card1);
			}
		});
		return sortedCards;
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards
	 * @return
	 */
	public static boolean isConnecting(List<Card> cards) {
		return isConnecting(cards, true);
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards
	 * @param needSort 指定是否需要排序
	 * @return
	 */
	public static boolean isConnecting(List<Card> cards, boolean needSort) {
		if (cards == null || cards.isEmpty() || cards.size() == 1) {
			return false;
		}
		if (needSort) {
			List<Card> tmpCards = new ArrayList<Card>(cards);
			cards = sortDesc(tmpCards);
		}
		boolean hasAce = cards.get(0).is(CardNumber.ACE);
		if (hasAce) {
			List<Card> cardsEndWithAce = new ArrayList<Card>(cards.subList(1, cards.size()));
			cardsEndWithAce.add(cards.get(0));
			return _isConnecting(cards) || _isConnecting(cardsEndWithAce);
		} else {
			return _isConnecting(cards);
		}
	}
	
	/**
	 * 扑克牌是否连续(内部)
	 * @param cards
	 * @return
	 */
	private static boolean _isConnecting(List<Card> cards) {
		Card prevCard = null;
		for (Card card : cards) {
			if (prevCard != null && !prevCard.isConnecting(card)) {
				return false;
			}
			prevCard = card;
		}
		return true;
	}
	
	public static void main(String[] args) {
		String a = "KsAs";
		List<Card> list = CardUtil.of(a);
		System.out.println(isConnecting(list));
	}
}
