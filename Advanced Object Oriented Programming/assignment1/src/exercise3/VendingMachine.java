package exercise3;

import java.util.HashMap;
import java.util.Map;

/**
 * Representing one vending machine.
 * 
 * @author minyan09
 * 
 */
public class VendingMachine {
	private double money;
	private Map<String, Product> stock;

	/**
	 * Constructing one vending machine.
	 * 
	 * @param m
	 *            the initial money
	 */
	public VendingMachine(double m) {
		money = m;
		stock = new HashMap<String, Product>();
	}

	/**
	 * Called by an operator, increase the number of product by n
	 * 
	 * @param p
	 *            the product you want to add
	 * @param n
	 *            the number of products
	 */
	public void addProduct(String p, int n) {
		int number = stock.get(p).getNumber() + n;
		stock.get(p).setNumber(number);

	}

	/**
	 * Called by an operator, add one new product
	 * 
	 * @param p
	 *            product name
	 * @param price
	 *            product price
	 * @param n
	 *            the number of products to add
	 */
	public void addProduct(String p, int price, int n) {
		stock.put(p, new Product(p, price, n));
	}

	/**
	 * After selling one product, this product should be removed from stock
	 * 
	 * @param p
	 *            product name
	 * @param n
	 *            the number of product to remove
	 */
	public void removeProduct(String p, int n) {
		int number = stock.get(p).getNumber();
		stock.get(p).setNumber(number - n);
	}

	/**
	 * Called by an operator
	 * 
	 * @param m
	 *            the money you want to remove
	 * @throws RuntimeException
	 */
	public void removeMoney(double m) throws RuntimeException {
		if (m > money) {
			throw new RuntimeException("Not enough money");
		} else {
			money -= m;
		}
	}

	/**
	 * After selling one product, the corresponding money is added to the pool
	 * 
	 * @param m
	 *            the money to add
	 */
	public void addMoney(double m) {
		money += m;
	}

	/**
	 * How much money is left in this machine.
	 * 
	 * @return the money left
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * Check if this product exists in the stock. Forwarding method.
	 * 
	 * @param p
	 *            product name
	 * @return true if this product exists in the stock, otherwise, false.
	 */
	public boolean containsKey(String p) {
		return stock.containsKey(p);
	}

	/**
	 * Forwarding method.
	 * 
	 * @param p
	 *            product name
	 * @return the product
	 */
	public Product getProduct(String p) {
		return stock.get(p);
	}

	public double getPrice(String p) {
		return stock.get(p).getPrice();
	}

	public int getNumber(String p) {
		return stock.get(p).getNumber();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName() + "\n");
		sb.append("Money is : " + money + "\n");
		for (String n : stock.keySet()) {
			sb.append("Product name : " + n);
			sb.append("Product number : " + stock.get(n).getNumber() + "\n");
		}
		return sb.toString();

	}

	/**
	 * Static member class, working as one auxilary tool.
	 * 
	 * @author minyan09
	 * 
	 */
	private static class Product {
		private String name;
		private int price;
		private int number;

		public Product(String n, int p, int number) {
			name = n;
			price = p;
			this.number = number;
		}

		/**
		 * Get the name of one product
		 * 
		 * @return the name of the product
		 */
		public String getName() {
			return name;
		}

		/**
		 * Get the price of one product
		 * 
		 * @return the price of the product
		 */
		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		/**
		 * Get the number of one product
		 * 
		 * @return the number of the product
		 */
		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}

	private static class Test {
		public static void main(String[] args) {
			VendingMachine vm = new VendingMachine(0);
			vm.addProduct("Product1", 10, 10);
			vm.addProduct("Product2", 20, 10);
			vm.addProduct("Product3", 30, 10);
			Session connection = new Session(vm, "Product1", 10);
			System.out.println(connection.process());
			System.out.println(vm.toString());
		}
	}
}
