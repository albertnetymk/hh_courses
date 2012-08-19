public class SimpleReflexAgent extends Agent {
	public SimpleReflexAgent(Environment env) {
		super(env);
	}

	public void move() {
		// We hard code it to go right
		if(col < Environment.WIDTH-1) {
			super.move();
			col++;
		}
	}
}
