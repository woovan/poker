package com.woovan.poker.model;

import java.util.List;

public class HandPower implements Comparable<HandPower> {

	private final HandType handType;
	
	private final List<Card> cards;
	
	public HandPower(final HandType handType, final List<Card> cards) {
		this.handType = handType;
		this.cards = cards;
	}
	
	@Override
	public String toString() {
		return handType.toString() + ":" + cards.toString();
	}
	
	@Override
	public int compareTo(HandPower o) {
		int typeDifference = this.handType.compareTo(o.handType);
		if (typeDifference == 0) {
			for (int i = 0; i < cards.size(); i++) {
				int difference = cards.get(i).compareTo(o.cards.get(i));
				if (difference != 0) {
					return difference;
				}
			}
			return 0;
		}
		return typeDifference;
	}
	
	public HandType getHandType() {
		return handType;
	}

	public List<Card> getCards() {
		return cards;
	}
	
	
}
