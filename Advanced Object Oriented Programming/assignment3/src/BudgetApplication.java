import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.*;
/**
 * One application manageing the budget.
 * @author minyan09
 *
 */
public class BudgetApplication extends JFrame {
	private BarView bv;
	private TextView tv;
	private StudentBudgetValue bm;

	public BudgetApplication() {
		bv = new BarView();
		bm = new StudentBudgetValue(bv);
		tv = new TextView(bm);
		bv.setBorder(new TitledBorder("Plot"));
		setLayout(new FlowLayout());
		add(tv);
		add(bv);
	}

	public static void main(String[] args) {
		JFrame frame = new BudgetApplication();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 1200);
		frame.pack();
		frame.setVisible(true);
	}
}
