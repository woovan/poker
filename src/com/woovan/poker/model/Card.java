package com.woovan.poker.model;


public class Card implements Comparable<Card> {

	private final CardNumber number;
	
	private final CardSuit suit;
	
	public Card(final CardNumber number, final CardSuit suit) {
		this.number = number;
		this.suit = suit;
	}
	
	public Card(String symbol) {
		if (symbol != null && symbol.length() == 2) {
			CardNumber number = CardNumber.getBySymbol(String.valueOf(symbol.charAt(0)));
			CardSuit suit = CardSuit.getBySymbol(String.valueOf(symbol.charAt(1)));
			if (number != null && suit != null) {
				this.number = number;
				this.suit = suit;
				return;
			}
		}
		throw new IllegalArgumentException("Initialize card with wrong symbol:" + symbol);
	}
	
	@Override
	public String toString() {
		return number.toString() + suit.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Card)) {
			return false;
		}
		Card other = (Card)obj;
		return number.equals(other.number) && suit.equals(other.suit);
	}

	@Override
	public int compareTo(Card card) {
		return number.compareTo(card.number);
	}
	
	public CardNumber getNumber() {
		return number;
	}
	
	public CardSuit getSuit() {
		return suit;
	}
	
	public int ordinal() {
		return number.ordinal();
	}
	
	public boolean is(CardNumber number) {
		return this.number == number;
	}
	
	public boolean is(CardSuit suit) {
		return this.suit == suit;
	}
	
	public boolean isConnecting(Card other) {
		return other != null && 
				(Math.abs(this.ordinal() - other.ordinal()) == 1 || 
				Math.abs(this.ordinal() - other.ordinal()) == 12);
	}
	
}
