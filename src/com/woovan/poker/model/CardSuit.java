package com.woovan.poker.model;

public enum CardSuit {
	
	SPADE("s"),
	HEART("h"),
	CLUB("c"),
	DIAMOND("d");
	
	private final String symbol;
	
	private CardSuit(String symbol) {
	    this.symbol = symbol;
	}
	
	public static CardSuit of(String symbol) {
		for (CardSuit suit : values()) {
			if (suit.symbol.equals(symbol)) {
				return suit;
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
