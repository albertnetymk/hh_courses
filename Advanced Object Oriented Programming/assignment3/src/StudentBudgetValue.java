import java.util.Observable;
import java.util.Observer;
/**
 * Model in this application.
 * @author minyan09
 *
 */
public class StudentBudgetValue extends Observable {
	private StudentBudget budget;

	public String getName() {
		return budget.getName();
	}

	public int getIncome() {
		return budget.getIncome();
	}

	public void setIncome(int income) {
		if (budget.getIncome() != income) {
			budget.setIncome(income);
			setChanged();
			notifyObservers();
		}
	}

	public int getRent() {
		return this.budget.getRent();
	}

	public void setRent(int rent) {
		if (budget.getRent() != rent) {
			budget.setRent(rent);
			setChanged();
			notifyObservers();
		}
	}

	public int getFood() {
		return this.budget.getFood();
	}

	public void setFood(int food) {
		if (budget.getFood() != food) {
			budget.setFood(food);
			setChanged();
			notifyObservers();
		}
	}

	public int getBooks() {
		return this.budget.getBooks();
	}

	public void setBooks(int books) {
		if (budget.getBooks() != books) {
			budget.setBooks(books);
			setChanged();
			notifyObservers();
		}
	}

	public int getEntertainment() {
		return this.budget.getEntertainment();
	}

	public void setEntertainment(int entertainment) {
		if (budget.getEntertainment() != entertainment) {
			budget.setEntertainment(entertainment);
			setChanged();
			notifyObservers();
		}
	}

	public int getSaves() {
		return this.budget.getSaves();
	}

	public void setSaves(int saves) {
		if (budget.getSaves() != saves) {
			budget.setSaves(saves);
			setChanged();
			notifyObservers();
		}
	}

	public StudentBudgetValue(Observer ob) {
		budget = new StudentBudget();
		addObserver(ob);
	}
}
