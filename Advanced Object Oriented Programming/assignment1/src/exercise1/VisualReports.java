package exercise1;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Visual report of one ball.
 * 
 * @author minyan09
 * 
 */
class VisualReports extends Frame implements ActionListener {

	private Ball redBall, blueBall;

	public static void main(String[] a) {
		VisualReports vr = new VisualReports();
		vr.setVisible(true);
	}

	public VisualReports() {
		setSize(600, 600);
		setTitle("Reporting Positions");
		setBackground(Color.white);

		// a panel with three control buttons.
		Panel control = new Panel();
		Button quit = new ControlButton(this, "quit");
		Button rb = new ControlButton(this, "red ball");
		Button bb = new ControlButton(this, "blue ball");
		control.add(quit);
		control.add(rb);
		control.add(bb);
		add("South", control);

		redBall = new Ball(100, 50, 20, Color.red);
		blueBall = new Ball(100, 50, 20, Color.blue);
	}

	public void paint(Graphics g) {
		redBall.report(g);
		blueBall.report(g);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("quit"))
			System.exit(0);
		else if (e.getActionCommand().equals("red ball")) {
			redBall.move();
			repaint();
		} else {
			blueBall.move();
			repaint();
		}

	}
}

class ControlButton extends Button {
	public ControlButton(ActionListener f, String name) {
		super(name);
		setForeground(Color.black);
		addActionListener(f);
	}
}
