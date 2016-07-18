package com.woovan.poker.model;

import java.util.List;

public class HandPower implements Comparable<HandPower> {

	private final HandType handType;
	
	private final List<Card> topCards;
	
	public HandPower(final HandType handType, final List<Card> topCards) {
		this.handType = handType;
		this.topCards = topCards;
	}
	
	@Override
	public String toString() {
		return handType.toString() + ":" + topCards.toString();
	}
	
	@Override
	public int compareTo(HandPower o) {
		int typeDifference = this.handType.compareTo(o.handType);
		if (typeDifference == 0) {
			for (int i = 0; i < topCards.size(); i++) {
				int difference = topCards.get(i).compareTo(o.topCards.get(i));
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

	public List<Card> getTopCards() {
		return topCards;
	}

	
}
