/**
 * Representing budget.
 * @author minyan09
 *
 */
public class StudentBudget {
	private String name;
	private int income;
	private int rent;
	private int food;
	private int books;
	private int entertainment;
	private int saves;

	public String getName() {
		return name;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getBooks() {
		return books;
	}

	public void setBooks(int books) {
		this.books = books;
	}

	public int getEntertainment() {
		return entertainment;
	}

	public void setEntertainment(int entertainment) {
		this.entertainment = entertainment;
	}

	public int getSaves() {
		return saves;
	}

	public void setSaves(int saves) {
		this.saves = saves;
	}

	public StudentBudget(String n, int i, int r, int f, int b, int e, int s) {
		name = n;
		income = i;
		rent = r;
		food = f;
		books = b;
		entertainment = e;
		saves = s;
	}
	public StudentBudget() {
		this("albert",0,0,0,0,0,0);
	}
}
