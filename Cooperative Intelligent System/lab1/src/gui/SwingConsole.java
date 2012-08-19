package gui;

import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SwingConsole {
	public static void run(final int width, final int height,
			final Class<? extends JFrame> type) {
		run(width, height, UIManager.getSystemLookAndFeelClassName(), type);
	}

	public static void run(final Class<? extends JFrame> type) {
		run(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit
				.getDefaultToolkit().getScreenSize().height, type);
	}

	public static void run(final int width, final int height,
			final String lookAndFeel, final Class<? extends JFrame> type) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(lookAndFeel);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					JFrame frame = type.getConstructor().newInstance();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(width, height);

					frame.setLocationRelativeTo(null);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
