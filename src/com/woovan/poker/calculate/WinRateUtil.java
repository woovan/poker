package com.woovan.poker.calculate;

import static com.woovan.poker.utils.CardUtil.cards;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.Deck;
import com.woovan.poker.model.HandPower;


public class WinRateUtil {
	
	public static void main(String[] args) {
		calculate(cards("AcAd", "KhKs"));
	}

	public static void calculate(List<List<Card>> hands) {
		
		long beginTm = System.currentTimeMillis();
		
		Deck deck = new Deck();
		for (List<Card> hand : hands) {
			deck.remove(hand);
		}
		
		long totalCount = 1500000;
		
		Map<List<Card>, BigDecimal> winStat = new HashMap<List<Card>, BigDecimal>();
		for (int i = 0; i < totalCount; i++) {
			List<Card> broad = deck.show(5);
			List<List<Card>> winHands = showdown(hands, broad);
//			System.out.println(winHands);
			
			BigDecimal winCount = winHands.size() == 1 ? BigDecimal.ONE : BigDecimal.ONE.divide(new BigDecimal(winHands.size()), 3, BigDecimal.ROUND_HALF_UP);
			for (List<Card> winHand : winHands) {
				BigDecimal count = winStat.get(winHand);
				if (count == null) {
					count = BigDecimal.ZERO;
				}
				winStat.put(winHand, count.add(winCount));
			}
			deck.shuffle();
		}
		
		for (Map.Entry<List<Card>, BigDecimal> entry : winStat.entrySet()) {
			BigDecimal winRate = entry.getValue().divide(new BigDecimal(totalCount), 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			System.out.println(String.format("%s: %.3f%%", entry.getKey(), winRate.doubleValue()));
		}
		
		long cost = System.currentTimeMillis() - beginTm;
		System.out.println(String.format("cost %dms", cost));
	}
	
	public static List<List<Card>> showdown(List<List<Card>> hands, List<Card> broad) {
		
//		System.out.println("Hands:" + hands);
//		System.out.println("Broad:" + broad);
		
		HandPower topPower = null;
		List<List<Card>> winHands = new ArrayList<List<Card>>();
		List<HandPower> powerList = new ArrayList<HandPower>();
		
		for (List<Card> hand : hands) {
			HandPower handPower = HandPowerUtil.calculate(hand, broad);
			powerList.add(handPower);
			if (topPower != null) {
				int result = handPower.compareTo(topPower);
				if (result > 0) {
					topPower = handPower;
					winHands.clear();
					winHands.add(hand);
				} else if (result == 0) {
					winHands.add(hand);
				}
			} else {
				topPower = handPower;
				winHands.add(hand);
			}
		}
//		System.out.println("Power:" + CardUtil.sortDesc(powerList));
		
		return winHands;
	}
}
