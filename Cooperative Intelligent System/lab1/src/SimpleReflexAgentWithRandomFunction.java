public class SimpleReflexAgentWithRandomFunction extends Agent {
	// One direction is randomly selected every period or this agent gets bumped.
	private static final int period = 5;
	private int periodInstance;
	private boolean bumped;
	private Direction d;

	public SimpleReflexAgentWithRandomFunction(Environment env) {
		super(env);
		periodInstance = 0;
		bumped = false;
		d = Direction.RIGHT;
	}

	private void updatePosition(Direction d) {
		switch(d) {
			case UP:
				row--;
				break;
			case DOWN:
				row++;
				break;
			case LEFT:
				col--;
				break;
			case RIGHT:
				col++;
				break;
		}
	}

	public void move() {
		if(bumped) {
			d = Direction.values()[rand.nextInt(4)];
		} else if ( periodInstance >= period) {
			d = Direction.values()[rand.nextInt(4)];
			periodInstance = 0;
		}
		super.move();
		periodInstance++;
		if(env.isSuccessful(row, col, d)) {
			updatePosition(d);
			bumped = false;
		} else {
			bumped = true;
		}
	}
}
