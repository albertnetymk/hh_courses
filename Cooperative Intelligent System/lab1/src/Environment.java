import java.util.Random;
import java.util.Observer;
import java.util.Observable;

public class Environment extends Observable {
	final public static int HEIGHT = 4;
	final public static int WIDTH = 4;
	final public static Random rand = new Random();

	public static Environment copy(Environment env) {
		Environment result = new Environment();
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				result.floor[i][j] = env.floor[i][j];
			}
		}
		return result;
	}

	// True : clean;
	private boolean[][] floor;

	public Environment() {
		floor = new boolean[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				floor[i][j] = true;
			}
		}
	}

	public boolean getFloor(int i, int j) {
		return floor[i][j];
	}

	public void setDirty(int i, int j) {
		setFloor(i,j, false);
		if(countObservers() > 0) {
			// notifyObservers({i, j});
			int[] tmpArray = {i, j};
			setChanged();
			notifyObservers(tmpArray);
		}
	}

	public void setFloor(int i, int j, boolean f) {
		floor[i][j] = f;
	}

	public void random() {
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				floor[i][j] = rand.nextBoolean();
			}
		}
	}

	public boolean isDirty(int row, int col) {
		return !floor[row][col];
	}

	public void clean(int row, int col) {
		floor[row][col] = true;
	}

	public boolean isSuccessful(int row, int col, Direction d) {
		switch (d) {
		case UP: {
				row--;
			break;
		}
		case DOWN: {
				row++;
			break;
		}
		case LEFT: {
				col--;
			break;
		}
		case RIGHT: {
				col++;
			break;
		}
		}
		return row < Environment.HEIGHT && row >= 0 && col < Environment.WIDTH && col >= 0;
	}

	public boolean allClean() {
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				if( !floor[i][j] ) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {
				if (floor[i][j]) {
					sb.append("E" + " ");
				} else {
					sb.append("D" + " ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
