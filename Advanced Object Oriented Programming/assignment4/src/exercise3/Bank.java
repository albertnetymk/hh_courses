package exercise3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
	private int money;
	private ReentrantLock _lock = new ReentrantLock();

	public Bank(int m) {
		money = m;
	}

	public int withdraw(int m) {
		_lock.lock();
		try {
			if (money >= m) {
				int original = check();
				money -= m;
				if (money != original - m) {
					System.out.println("Error");
					System.exit(1);
				}
			}
		} finally {
			_lock.unlock();
		}
		return money;
	}

	public synchronized int deposit(int m) {
		_lock.lock();
		try {
			int original = check();
			money += m;
			if (money != original + m) {
				System.out.println("Error");
				System.exit(1);
			}
		} finally {
			_lock.unlock();
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
