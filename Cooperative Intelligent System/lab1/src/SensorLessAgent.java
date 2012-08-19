public class SensorLessAgent extends InfiniteAgent {
	private Direction dir;
	// Indicating which direction this agent is moving.
	private boolean forward;

	public SensorLessAgent(Environment env) {
		super(env);
		forward = true;
	}

	// This operation will fail 25% of the time.
	public void suck() {
		if(rand.nextInt(4) == 0) {
			// This operation failed.
			if(!env.isDirty(row, col)) {
				env.setDirty(row, col);
			}
		} else {
			if(env.isDirty(row, col)) {
				super.suck();
			}
		}
		
	}

	// The sensor will give the wrong result 10% of the time.
	public boolean isDirty() {
		if(rand.nextInt(10) == 0) {
			// Wrong result.
			return !env.isDirty(row, col);
		} else {
			return env.isDirty(row, col);
		}
	}
}
