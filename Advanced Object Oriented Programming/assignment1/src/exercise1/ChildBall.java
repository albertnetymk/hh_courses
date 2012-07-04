package exercise1;

import java.awt.Color;

public class ChildBall extends Ball {
	private int otherField;

	public ChildBall(int x, int y, int r, Color c, int o) {
		super(x, y, r, c);
		otherField = o;
	}

}
