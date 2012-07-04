package exercise2;

import java.util.Random;

import exercise2.Bank;

public class Withdraw implements Runnable {
	private static Random rand = new Random();
	private Bank bank;

	public Withdraw(Bank b) {
		bank = b;
	}

	public void run() {
		while (!Thread.interrupted()) {
			int input = rand.nextInt(20);
			bank.withdraw(input);
		}
	}
}
