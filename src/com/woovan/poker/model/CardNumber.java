package com.woovan.poker.model;

public enum CardNumber {

	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	NINE("9"),
	TEN("T"),
	JACK("J"),
	QUEEN("Q"),
	KING("K"),
	ACE("A");
	
	private final String symbol;
	
	private CardNumber(String symbol) {
	    this.symbol = symbol;
	}
	
	public static CardNumber getBySymbol(String symbol) {
		for (CardNumber num : values()) {
			if (num.symbol.equals(symbol)) {
				return num;
			}
		}
		return null;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
}
