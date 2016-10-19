package com.woovan.poker.calculate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.woovan.poker.model.Card;
import com.woovan.poker.model.Deck;
import com.woovan.poker.model.HandPower;
import com.woovan.poker.model.HandType;

public class PokerRunner extends Thread {
	
	private final long times;
	
	private final Deck deck = new Deck();
	
	private final Map<HandType, Long> stat = new HashMap<HandType, Long>();
	
	private CountDownLatch latch;
	
	public PokerRunner(long times, CountDownLatch latch) {
		this.times = times;
		this.latch = latch;
	}
	
	public static void main(String[] args) throws InterruptedException {
		long totalTimes = 1000000;
		int runner = 4;
		long times = totalTimes / runner;
		
		long beginTm = System.currentTimeMillis();
		
		CountDownLatch latch = new CountDownLatch(runner);
		List<PokerRunner> list = new ArrayList<PokerRunner>();
		for (int i = 0; i < runner; i++) {
			PokerRunner pokerRunner = new PokerRunner(times, latch);
			pokerRunner.start();
			list.add(pokerRunner);
		}
		latch.await();
		
		Map<HandType, Long> finalStat = new HashMap<HandType, Long>();
		for (PokerRunner pokerRunner : list) {
			for (HandType handType : HandType.values()) {
				Long count = finalStat.get(handType);
				if (count == null) {
					count = 0l;
				}
				Long newCount = pokerRunner.getStat().get(handType);
				if (newCount != null) {
					finalStat.put(handType, count + newCount);
				}
			}
		}
		
		for (HandType type : HandType.values()) {
			Long count = finalStat.get(type);
			BigDecimal rate = new BigDecimal(count).divide(new BigDecimal(totalTimes), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			System.out.println(String.format("%s:%d:%.6f%%", type.name(), count, rate.doubleValue()));
		}
		
		long cost = System.currentTimeMillis() - beginTm;
		System.out.println(String.format("cost %dms", cost));
	}
	
	@Override
	public void run() {
		for (int i = 0; i < times; i++) {
			play();
		}
		latch.countDown();
	}
	
	public void play() {
		List<Card> cards = deck.show(7);
		HandPower handPower = HandPowerUtil.calculate(cards);
		stat(handPower.getHandType());
	}
	
	private void stat(HandType handType) {
		Long count = stat.get(handType);
		if (count == null) {
			count = 0l;
		}
		stat.put(handType, count + 1);
	}

	public Map<HandType, Long> getStat() {
		return stat;
	}
	
}
