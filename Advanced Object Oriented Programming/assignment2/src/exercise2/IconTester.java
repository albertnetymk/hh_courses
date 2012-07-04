package exercise2;

import javax.swing.JOptionPane;

public class IconTester {
	public static void main(String[] args) {
		// JOptionPane.showMessageDialog(null, "Hello, World!", "Message",
		// JOptionPane.INFORMATION_MESSAGE, new ImageIcon("globe.gif"));
		JOptionPane.showMessageDialog(null, "Hello, World!", "Message",
				JOptionPane.INFORMATION_MESSAGE, new MarsIcon(20));

		System.exit(0);
	}
}
