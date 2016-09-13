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
	public static List<Card> card(String cardSymbol) {
		List<Card> cards = new ArrayList<Card>();
		if (cardSymbol != null && cardSymbol.length() % 2 == 0) {
			for (int i = 0; i < cardSymbol.length(); i+=2) {
				Card card = new Card(cardSymbol.substring(i, i + 2));
				cards.add(card);
			}
		}
		return cards;
	}
	
	public static List<List<Card>> cards(String... cardList) {
		List<List<Card>> cards = new ArrayList<List<Card>>();
		
		for (String cardSymbol : cardList) {
			cards.add(card(cardSymbol));
		}
		return cards;
	}
	
	/**
	 * 扑克转扑克编号
	 * @param cards
	 * @return
	 */
	public static List<CardNumber> toCardNumber(List<Card> cards) {
        List<CardNumber> cardNumbers = new ArrayList<CardNumber>();
        for (Card card : cards) {
            cardNumbers.add(card.getNumber());
        }
        return cardNumbers;
    }
	
	/**
	 * 正序排列(从小到大) 不改变原序列
	 * @param cards
	 * @return
	 */
	public static <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		List<T> sortedList = new ArrayList<T>(list);
		Collections.sort(sortedList);
		return sortedList;
	}
	
	/**
	 * 逆序排列(从大到小) 不改变原序列
	 * @param cards
	 * @return
	 */
	public static <T extends Comparable<? super T>> List<T> sortDesc(List<T> list) {
		List<T> sortedList = new ArrayList<T>(list);
		Collections.sort(sortedList, new Comparator<T>() {

			@Override
			public int compare(T card1, T card2) {
				return card2.compareTo(card1);
			}
		});
		return sortedList;
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards
	 * @return
	 */
	public static boolean isConnecting(List<CardNumber> cards) {
		return isConnecting(cards, true);
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards
	 * @param needSort 指定是否需要排序
	 * @return
	 */
	public static boolean isConnecting(List<CardNumber> cards, boolean needSort) {
		if (cards == null || cards.isEmpty() || cards.size() == 1) {
			return false;
		}
		if (needSort) {
			cards = sortDesc(cards);
		}
		boolean hasAce = (cards.get(0) == CardNumber.ACE);
		if (hasAce) {
			List<CardNumber> cardsEndWithAce = new ArrayList<CardNumber>(cards.subList(1, cards.size()));
			cardsEndWithAce.add(cards.get(0));
			return _isConnecting(cards) || _isConnecting(cardsEndWithAce);
		} else {
			return _isConnecting(cards);
		}
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards
	 * @return
	 */
	public static boolean isCardsConnecting(List<Card> cards) {
		List<CardNumber> list = toCardNumber(cards);
		return isConnecting(list, true);
	}
	
	public static boolean isCardsConnecting(List<Card> cards, boolean needSort) {
		List<CardNumber> list = toCardNumber(cards);
		return isConnecting(list, needSort);
	}
	
	/**
	 * 扑克牌是否连续(内部)
	 * @param cards
	 * @return
	 */
	private static boolean _isConnecting(List<CardNumber> cardNumbers) {
		CardNumber prevNum = null;
		for (CardNumber num : cardNumbers) {
			if (prevNum != null && 
					prevNum.ordinal() != num.ordinal() + 1 && 
					prevNum.ordinal() != num.ordinal() - 12) {
				return false;
			}
			prevNum = num;
		}
		return true;
	}
	
	public static void main(String[] args) {
		String a = "As5s4s3s2s";
		List<Card> list = CardUtil.card(a);
		System.out.println(isCardsConnecting(list));
	}
}
