package exercise2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank {
	private int money;

	public Bank(int m) {
		money = m;
	}

	public synchronized int withdraw(int m) {
		int original = check();
		money -= m;
		if (money != original - m) {
			System.out.println("Error");
			System.exit(1);
		}
		return money;
	}

	public synchronized int deposit(int m) {
		int original = check();
		money += m;
		if (money != original + m) {
			System.out.println("Error");
			System.exit(1);
		}
		return money;
	}

	public synchronized int check() {
		return money;
	}

	public static void main(String[] args) {
		Bank b = new Bank(0);
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 2; ++i) {
			exec.submit(new Deposit(b));
		}
		for (int i = 0; i < 2; ++i) {
			exec.submit(new Withdraw(b));
		}
		exec.shutdown();
	}
}
