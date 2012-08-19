import java.util.Random;

public class VacuumCleanerModel {
	Environment env;
	Agent[] agents;

	public VacuumCleanerModel(int number) {
		agents = new Agent[number];
		agents[0] = new SimpleReflexAgent(new Environment());
		// agents[1] = new ModelBasedAgent(new Environment());
		// agents[1] = new SimpleReflexAgentWithRandomFunction(new Environment());
		// agents[1] = new SensorLessAgent(new Environment());
		agents[1] = new InfiniteAgent(new ErosiveEnvironment());
		env = new Environment();
	}

	public Agent getAgent(int index) {
		return agents[index];
	}

	public void random() {
		env.random();
	}

	public void init() {
		env.random();
		Random rand = new Random();
		int x = rand.nextInt(Environment.WIDTH), y = rand
				.nextInt(Environment.HEIGHT);
		for (Agent a : agents) {
			a.init();
			a.setPosition(x, y);
		}
	}

	public Environment getEnvironment() {
		return env;
	}
}
