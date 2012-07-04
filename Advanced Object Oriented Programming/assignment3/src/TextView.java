import java.util.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Text fields for user to input data.
 * @author minyan09
 *
 */
public class TextView extends JPanel {
	private JLabel labelForRent = new JLabel("Rent");
	private JLabel labelForFood = new JLabel("Food");
	private JLabel labelForBooks = new JLabel("Books");
	private JLabel labelForIncome = new JLabel("Income");
	private JLabel labelForEntertainment = new JLabel("Entertainment");
	private JLabel labelForSaves = new JLabel("Saves");

	private JTextField textForRent = new JTextField(10);
	private JTextField textForFood = new JTextField(10);
	private JTextField textForBooks = new JTextField(10);
	private JTextField textForIncome = new JTextField(10);
	private JTextField textForEntertainment = new JTextField(10);
	private JTextField textForSaves = new JTextField(10);

	public TextView(final StudentBudgetValue bm) {
		// setPreferredSize(new Dimension(300, 300));
		setLayout(new GridLayout(6, 2));
		add(labelForBooks);
		textForBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setBooks(Integer.valueOf(textForBooks.getText()));
			}
		});
		add(textForBooks);

		add(labelForEntertainment);
		textForEntertainment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setEntertainment(Integer.valueOf(textForEntertainment.getText()));
			}
		});
		add(textForEntertainment);

		add(labelForFood);
		textForFood.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setFood(Integer.valueOf(textForFood.getText()));
			}
		});
		add(textForFood);

		add(labelForIncome);
		textForIncome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setIncome(Integer.valueOf(textForIncome.getText()));
			}
		});
		add(textForIncome);

		add(labelForRent);
		textForRent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setRent(Integer.valueOf(textForRent.getText()));
			}
		});
		add(textForRent);

		add(labelForSaves);
		textForSaves.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bm.setSaves(Integer.valueOf(textForSaves.getText()));
			}
		});
		add(textForSaves);
	}
}
