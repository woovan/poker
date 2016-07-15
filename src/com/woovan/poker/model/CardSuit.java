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
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
}
