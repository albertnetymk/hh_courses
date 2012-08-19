public class ErosiveEnvironment extends Environment {
	// This function is called for each step.
	public boolean isDirty(int row, int col) {
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				// Every clean block has the possibility to go bad.
				if(getFloor(i,j) && rand.nextInt(10) == 0) {
				//	System.out.println("Set dirty for " + i + " " + j);
					setDirty(i,j);
				}
			}
		}
		return !getFloor(row, col);
	}
}
