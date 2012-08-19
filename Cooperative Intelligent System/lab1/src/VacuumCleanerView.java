import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class VacuumCleanerView extends JPanel {
	private Agent agent;
	private EnvironmentView envView;
	private JLabel stepLabel;
	private JLabel performanceLabel;

	public VacuumCleanerView(Agent agent, Dimension d) {
		envView = new EnvironmentView(agent, d);
		stepLabel = new JLabel("Steps");
		performanceLabel = new JLabel("Performance");
		this.agent = agent;
		setLayout(new FlowLayout());
		add(envView);
		add(stepLabel);
		add(performanceLabel);
		setVisible(true);
	}

	public void summary() {
		// performanceLabel.setText("Performance: " + agent.performance);
	}

	public void update(String step, int p) {
		envView.update();
		stepLabel.setText(step);
		performanceLabel.setText("Performance: " + p);
	}

	public void update() {
		envView.update();
	}

	public void initUsingEnvironment(Environment env) {
		envView.initUsingEnvironment(env);
		stepLabel.setText("Steps");
	}
}
