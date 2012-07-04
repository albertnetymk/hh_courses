import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
/**
 * Bar representation of data.
 * @author minyan09
 *
 */
public class BarView extends JPanel implements Observer {
	private Map<String, Integer> values;

	public BarView() {
		values = new TreeMap<String, Integer>();
		setPreferredSize(new Dimension(600, 600));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (values.size() == 0)
			return;
		double minValue = 0;
		double maxValue = 0;
		for(String s : values.keySet()) {
			if (minValue > values.get(s))
				minValue = values.get(s);
			if (maxValue < values.get(s))
				maxValue = values.get(s);
		}
		if (maxValue == minValue)
			return;

		// the size of the canvas
		int clientWidth = (int)(0.9*getWidth());
		int clientHeight = (int)(0.9*getHeight());
		int barWidth = clientWidth / values.size();
		int bias = (int)(0.05*getWidth());


		Font labelFont = new Font("SansSerif", Font.PLAIN, 20);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

		int bottom = labelFontMetrics.getHeight();
		double scale = (clientHeight - bottom) / (maxValue - minValue);
		int x, y;
		y = clientHeight - labelFontMetrics.getDescent();
		g.setFont(labelFont);

		int i=0;
		for(String s : values.keySet()) {
			int valueX = (int)((i+0.25) * barWidth);
			int valueY = 0;
			int height = (int) (values.get(s) * scale);
			if (values.get(s) >= 0)
				valueY += (int) ((maxValue - values.get(s)) * scale);
			else {
				valueY += (int) (maxValue * scale);
				height = -height;
			}

			g.setColor(Color.red);
			g.fillRect(bias+valueX, bias+valueY,
					(int)(barWidth * 0.5), height);
			g.setColor(Color.black);
			g.drawRect(bias+valueX, bias+valueY,
					(int)(barWidth * 0.5), height);
			int labelWidth = labelFontMetrics.stringWidth(s);
			x = i * barWidth + (barWidth - labelWidth) / 2;
			g.drawString(s, bias+x, bias+y);
			i++;
		}
	}

	public void update(Observable arg0, Object arg1) {
		StudentBudgetValue v = (StudentBudgetValue)arg0;
		values.put("Rent", v.getRent());
		values.put("Food", v.getFood());
		values.put("Books", v.getBooks());
		values.put("Entertainment", v.getEntertainment());
		repaint();
	}
}
