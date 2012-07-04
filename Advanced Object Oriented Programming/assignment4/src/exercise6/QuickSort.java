package exercise6;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class QuickSort {
	private AtomicLong comparisons = new AtomicLong(0);
	private AtomicLong exchanges = new AtomicLong(0);
	private AtomicLong threadCount = new AtomicLong(0);
	private AtomicLong partitionerCount = new AtomicLong(0);
	private CountDownLatch latch;

	private double[] a;

	private ExecutorService executor;
	private ExecutorService executorShutdown;

	public QuickSort(final double[] a) {
		this.a = a;
		this.latch = new CountDownLatch(a.length);
		executor = Executors.newCachedThreadPool(new ThreadFactory() {
			public Thread newThread(Runnable r) {
				threadCount.incrementAndGet();
				return new Thread(r);
			}
		});
		executorShutdown = Executors.newFixedThreadPool(1);
		// this task will run after the sorting
		executorShutdown.submit(new OutputStatistics(latch, a));
		executorShutdown.shutdown();

		new QuickSortThread(0, a.length - 1);
	}

	private class OutputStatistics implements Runnable {
		private CountDownLatch latch;
		private double[] a;
		private double[] b;

		public OutputStatistics(CountDownLatch latch, double[] a) {
			this.latch = latch;
			this.a = a;
		}

		public void run() {
			try {
				latch.await();
				executor.shutdown();
				while (!executor.isTerminated()) {
					TimeUnit.MILLISECONDS.sleep(100);
				}
				b = new double[a.length];
				System.arraycopy(a, 0, b, 0, a.length);
				Arrays.sort(b);
				// print statistics
				System.out.println("Comparisons:  " + comparisons.get());
				System.out.println("Exchanges:    " + exchanges.get());
				System.out.println("Threads:      " + threadCount.get());
				System.out.println("Partitioners: " + partitionerCount.get());
				System.out.println("isSorted:     " + Arrays.equals(a, b));
			} catch (InterruptedException e) {
				System.out.println("Interrupted.");
			}
		}
	}

	private class QuickSortThread implements Runnable {
		private int left, right;
		private int pivot;

		public QuickSortThread(int left, int right) {
			this.left = left;
			this.right = right;
			executor.submit(this);
		}

		public void run() {
			if (right < left) {
				return;
			} else if (right == left) {
				latch.countDown();
				return;
			} else {
				partitionerCount.incrementAndGet();
				int i = left - 1;
				int j = right;
				while (true) {
					while (less(a[++i], a[right])) {
						// find the first element, which is larger than or equal
						// to a[right]
						// the range of i is [left, right]
						// left indicates the first element in this array is
						// larger than or equal to the pivot
						// right indicates all elements on the left of pivot is
						// smaller than it
						;
					}
					while (less(a[right], a[--j])) {
						// find the first element, which is smaller than or
						// equal to a[right]
						// the range of j is [left, right-1]
						// left indicates all elements on the left of pivot is
						// larger than it
						// find item on right to swap
						if (j == left) {
							break; // don't go out-of-bounds
						}
					}
					if (i >= j) {
						// check if pointers cross
						break;
					}
					exch(a, i, j);
				}
				exch(a, i, right);
				latch.countDown();
				pivot = i;
			}
			new QuickSortThread(left, pivot - 1);
			new QuickSortThread(pivot + 1, right);
		}

		private boolean less(double x, double y) {
			comparisons.incrementAndGet();
			return (x < y);
		}

		private void exch(double[] a, int i, int j) {
			exchanges.incrementAndGet();
			double swap = a[i];
			a[i] = a[j];
			a[j] = swap;
		}
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		int N = args.length == 0 ? 100 : Integer.parseInt(args[0]);

		double[] a = new double[N];
		for (int i = 0; i < N; i++) {
			a[i] = Math.random();
		}
		new QuickSort(a);
	}
}
