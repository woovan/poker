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
	public static List<Card> cards(String symbol) {
		List<Card> cards = new ArrayList<Card>();
		if (symbol != null && symbol.length() % 2 == 0) {
			for (int i = 0; i < symbol.length(); i+=2) {
				Card card = new Card(symbol.substring(i, i + 2));
				cards.add(card);
			}
		}
		return cards;
	}
	
	public static List<List<Card>> cards(String... symbolList) {
		List<List<Card>> cards = new ArrayList<List<Card>>();
		for (String symbol : symbolList) {
			cards.add(cards(symbol));
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
	 * 逆序排列(从大到小)
	 * @param cards
	 * @return
	 */
	public static <T extends Comparable<? super T>> void sortDesc(List<T> list) {
		Collections.sort(list, new Comparator<T>() {

			@Override
			public int compare(T card1, T card2) {
				return card2.compareTo(card1);
			}
		});
	}
	
	/**
	 * 扑克牌是否连续
	 * @param cards 元素不重复且有序 例AKQJT JT987 特例 5432A
	 * @return
	 */
	public static boolean isCardsConnecting(List<Card> cards) {
		List<CardNumber> list = toCardNumber(cards);
		return isConnecting(list);
	}
	
	/**
	 * CardNumber是否连续
	 * @param cardNumbers 元素不重复且有序 例AKQJT JT987 特例 5432A
	 * @return
	 */
	public static boolean isConnecting(List<CardNumber> cardNumbers) {
		int size = cardNumbers.size();
		return interval(cardNumbers.get(0), cardNumbers.get(size - 1)) == size - 1;
	}
	
	/**
	 * 获得CardNumber之间的间隔
	 * @param first last
	 * @return
	 */
	public static int interval(CardNumber first, CardNumber last) {
		int interval = first.ordinal() - last.ordinal();
		if (interval < 0) {
			interval = first.ordinal() + 1;	//interval小于0表示last为A，这时A的序号为-1
		}
		return interval;
	}
	
}
