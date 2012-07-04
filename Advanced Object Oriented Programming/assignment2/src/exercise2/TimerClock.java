package exercise2;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

public class TimerClock extends JFrame {

	private JLabel _label;
	private Icon _icon;

	public TimerClock() {
		super("Java clock");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		_icon = new StringIcon(100);
		_label = new JLabel();
		_label.setIcon(_icon);
		_label.setBorder(new TitledBorder("boder"));
		add(_label);

		Timer t = new javax.swing.Timer(1000, new ClockListener());
		t.start();
		pack();
		setVisible(true);
	}

	private class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			_label.repaint();
		}
	}

	public static void main(String[] args) {
		JFrame clock = new TimerClock();
	}
}

class StringIcon implements Icon {
	private int _size;

	public StringIcon(int s) {
		_size = s;
	}

	@Override
	public int getIconHeight() {
		return _size;
	}

	@Override
	public int getIconWidth() {
		return _size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("Europe/Stockholm"));
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setCalendar(calendar);
		Font font = new Font("SanSerif", Font.BOLD, 20);
		FontMetrics metrics = g.getFontMetrics(font);
		g.setFont(font);
		String time = formatter.format(calendar.getTime());
		g.drawString(time,
				x + (getIconWidth() - metrics.stringWidth(time)) / 2, y
						+ (getIconHeight() - metrics.getHeight()) / 2);
	}
}
