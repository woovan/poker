package com.woovan.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private final List<Card> cards = new ArrayList<Card>();

    public Deck() {
    	for (CardNumber number : CardNumber.values()) {
    		for (CardSuit suit : CardSuit.values()) {
                Card card = new Card(number, suit);
                cards.add(card);
            }
        }
    	shuffle();
    }
    
    public void shuffle() {
    	Collections.shuffle(cards);
    }
    
    public List<Card> getCards() {
        return cards;
    }
    
    public void remove(List<Card> cards) {
    	this.cards.removeAll(cards);
    }
    
    public List<Card> deal(int count) {
    	List<Card> takes = new ArrayList<Card>(cards.subList(0, count));
    	cards.removeAll(takes);
    	return takes;
    }
    
    public List<Card> show(int count) {
    	return new ArrayList<Card>(cards.subList(0, count));
    }
    
    @Override
    public String toString() {
    	return cards.toString();
    }
    
    public static void main(String[] args) {
		Deck deck = new Deck();
		System.out.println(deck);
		List<Card> cards = deck.deal(7);
		System.out.println(cards);
		System.out.println(deck);
	}
}
