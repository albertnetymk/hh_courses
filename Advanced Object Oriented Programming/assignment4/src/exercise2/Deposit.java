package exercise2;

import java.util.Random;

import exercise2.Bank;

public class Deposit implements Runnable {
	private static Random rand = new Random();
	private Bank bank;

	public Deposit(Bank b) {
		bank = b;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			int input = rand.nextInt(20);
			bank.deposit(input);
		}
	}
}
