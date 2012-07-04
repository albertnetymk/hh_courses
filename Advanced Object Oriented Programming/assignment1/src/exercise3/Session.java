package exercise3;

/**
 * Represent one session between the vending machine and the user.
 * 
 * @author minyan09
 * 
 */
public class Session {
	private String selected;
	private VendingMachine machine;
	private double coins;

	/**
	 * Construct one session
	 * 
	 * @param machine
	 *            the vending machine
	 * @param p
	 *            product name
	 * @param coins
	 *            coins the user inputed
	 */
	public Session(VendingMachine machine, String p, double coins) {
		if (machine.containsKey(p) && machine.getNumber(p) > 0) {
			selected = p;
			this.machine = machine;
			this.coins = coins;
		} else {
			System.out.println("No product");
		}
	}

	/**
	 * To process this session
	 * 
	 * @return changes, which could be 0
	 */
	public double process() {
		if (machine.getPrice(selected) <= coins) {
			System.out.println("Give the product : " + selected);
			machine.addMoney(machine.getPrice(selected));
			machine.removeProduct(selected, 1);
			return coins - machine.getPrice(selected);
		} else {
			System.out.println("Not enough money");
			return coins;
		}
	}
}
