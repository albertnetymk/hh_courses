package exercise1;

import java.awt.Color;

/**
 * Report the state of one ball.
 * 
 * @author minyan09
 * 
 */
class Reports {
	public static void main(String[] a) {
		Ball redBall = new Ball(100, 50, 20, Color.red);
		Ball blueBall = new Ball(100, 50, 20, Color.blue);
		redBall.report();
		blueBall.report();
		redBall.move();
		redBall.report();
		System.out.println(redBall.hashCode());
		System.out.println(blueBall.hashCode());
	}
}
