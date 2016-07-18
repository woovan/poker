package com.woovan.poker.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;

public class CardUtil {

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
	
	public static List<Card> sort(List<Card> cards) {
		List<Card> sortedCards = new ArrayList<Card>(cards);
		Collections.sort(sortedCards);
		return sortedCards;
	}
	
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
	
	public static int getCardRange(List<Card> cards) {
		return getCardRange(cards, false);
	}
	
	public static int getCardRange(List<Card> cards, boolean needSort) {
		if (cards == null || cards.isEmpty()) {
			return 0;
		}
		if (cards.size() == 1) {
			return 1;
		}
		if (needSort) {
			List<Card> tmpCards = new ArrayList<Card>(cards);
			sortDesc(tmpCards);
			cards = tmpCards;
		}
		Card head = cards.get(0);
		Card tail = cards.get(cards.size() - 1);
		int range = head.getNumber().ordinal() - tail.getNumber().ordinal() + 1;
		if (head.getNumber() == CardNumber.ACE) {
			int range2 = cards.get(1).getNumber().ordinal() + 2;
			range = Math.min(range, range2);
		}
		return range;
	}
	
	public static void main(String[] args) {
		String a = "AsKs5s4s2s";
		List<Card> list = CardUtil.of(a);
		System.out.println(getCardRange(list, false));
	}
}
