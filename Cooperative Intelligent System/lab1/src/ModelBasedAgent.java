public class ModelBasedAgent extends Agent {
	private boolean[][] model;
	private boolean finished;

	public ModelBasedAgent(Environment env) {
		super(env);
		finished = false;
		model = new boolean[Environment.HEIGHT][Environment.WIDTH];
		for (int i = 0; i < Environment.HEIGHT; ++i) {
			for (int j = 0; j < Environment.WIDTH; ++j) {
				model[i][j] = false;
			}
		}
	}

	private boolean getModel(int i, int j) {
		if (i < 0 || i >= Environment.HEIGHT || j < 0 || j >= Environment.WIDTH) {
			// Pretend that we have been there.
			return true;
		}
		return model[i][j];
	}

	public void init() {
		super.init();
		setFinish(false);
		// clean memory
		for (int i = 0; i < Environment.HEIGHT; ++i) {
			for (int j = 0; j < Environment.WIDTH; ++j) {
				model[i][j] = false;
			}
		}
	}

	public boolean isDirty() {
		model[row][col] = true;
		return super.isDirty();
	}

	public String getModel() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Environment.HEIGHT; ++i) {
			for (int j = 0; j < Environment.WIDTH; ++j) {
				sb.append(getModel(i, j) + " ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public void move() {
		if (!finished) {
			boolean found = false;
			int i = 0, j = 0;
			int step = 0;
			// Try to find the unvisited block within "step" steps.
			while (true) {
				if (step > Environment.HEIGHT + Environment.WIDTH - 2) {
					setFinish(true);
					// finished = true;
					break;
				}
				for (i = -step; i <= step; ++i) {
					j = step - Math.abs(i);
					if (!getModel(row + i, col - j)) {
						// model[row + i][col - j] = true;
						j = -j;
						found = true;
						break;
					}
					if (!getModel(row + i, col + j)) {
						// model[row + i][col + j] = true;
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
				step++;
			}
			if (found) {
				// Move one step
				super.move();
				if (i < 0) {
					row--;
				} else if (i > 0) {
					row++;
				} else if (j < 0) {
					col--;
				} else if (j > 0) {
					col++;
				} else {
					throw new RuntimeException("Doesn't move anywhere");
				}
			}
		}
	}

	public static class Test {
		public static void main(String[] args) {
			Environment env = new Environment();
			Agent agent = new ModelBasedAgent(env);
			System.out.println(env.toString());

			for (int i = 0; i < 100; ++i) {
				if (agent.isDirty()) {
					agent.suck();
				} else {
					System.out.println("Row: " + agent.row + " Column: "
							+ agent.col);
					agent.move();
				}
			}
			System.out.println(env.toString());

		}
	}
}
