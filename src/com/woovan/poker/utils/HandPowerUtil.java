package com.woovan.poker.utils;

import java.util.List;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.CardNumber;
import com.woovan.poker.model.CardSuit;
import com.woovan.poker.model.HandPower;

public class HandPowerUtil {

	public static HandPower calculate(List<Card> cards) {
		
		List<Card> sortedCards = CardUtil.sortDesc(cards);
		MapList<CardNumber, Card> numberGroup = getNumberGroup(sortedCards);
		MapList<CardSuit, Card> suitGroup = getSuitGroup(sortedCards);
		
		
		return null;
	}
	
	public static void main(String[] args) {
		List<Card> cards = CardUtil.of("AsKsQsJsTs9s8s");
		System.out.println(CardUtil.sortDesc(cards).subList(1, 6));
	}
	
	private static List<Card> getStraightFlush(MapList<CardSuit, Card> suitGroup) {
        List<Card> flush = getFlush(suitGroup);
        if (flush == null) {
            return null;
        }
        
        if (flush.size() > 5) {
        	
        }
        return null;
    }
	
	private static List<Card> getFlush(MapList<CardSuit, Card> suitGroup) {
        for (List<Card> cards : suitGroup) {
            if (cards.size() >= 5) {
                return cards;
            }
        }
        return null;
    }
	
	private static List<Card> getStraight(List<Card> cards) {
		if (cards == null || cards.size() < 5) {
			return null;
		}
		
		for (int i = 0; i < cards.size() - 5 + 1; i++) {
        	List<Card> subList = cards.subList(i, i + 5);
		}
		return null;
	}
	
	private static MapList<CardNumber, Card> getNumberGroup(List<Card> cards) {
		MapList<CardNumber, Card> numberGroup = new MapList<CardNumber, Card>();
		for (Card card : cards) {
		    numberGroup.put(card.getNumber(), card);
		}
		return numberGroup;
	}
	
	private static MapList<CardSuit, Card> getSuitGroup(List<Card> cards) {
		MapList<CardSuit, Card> suitGroup = new MapList<CardSuit, Card>();
		for (Card card : cards) {
		    suitGroup.put(card.getSuit(), card);
		}
		return suitGroup;
	}
}
