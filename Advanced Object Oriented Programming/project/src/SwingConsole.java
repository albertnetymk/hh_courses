import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Select look and feel for one application.
 * 
 * @author minyan09
 * 
 */
public class SwingConsole {
	public static void run(final Class<? extends JFrame> type, final int width,
			final int height) {
		run(type, width, height, UIManager.getSystemLookAndFeelClassName());
	}

	public static void run(final Class<? extends JFrame> type, final int width,
			final int height, final String lookAndFeel) {
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
