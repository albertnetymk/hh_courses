package matrix;

public class SectionB extends Section {
	private double width_1;
	private double width_2;
	private double width_3;
	private double width_4;

	private double height;
	private double interval_1;
	private double interval_2;
	private double interval_3;

	public SectionB(String name) {
		super(name, 42);
		width_1 = 4;
		width_2 = 3;
		width_3 = 3;
		width_4 = 2.73;

		height = 7.85;
		interval_1 = 10.50;
		interval_2 = 13;
		interval_3 = 12.81;
	}

	public double getWidth_1() {
		return width_1;
	}

	public void setWidth_1(double width_1) {
		this.width_1 = width_1;
	}

	public double getWidth_2() {
		return width_2;
	}

	public void setWidth_2(double width_2) {
		this.width_2 = width_2;
	}

	public double getWidth_3() {
		return width_3;
	}

	public void setWidth_3(double width_3) {
		this.width_3 = width_3;
	}

	public double getWidth_4() {
		return width_4;
	}

	public void setWidth_4(double width_4) {
		this.width_4 = width_4;
	}

	public double getInterval_1() {
		return interval_1;
	}

	public void setInterval_1(double interval_1) {
		this.interval_1 = interval_1;
	}

	public double getInterval_2() {
		return interval_2;
	}

	public void setInterval_2(double interval_2) {
		this.interval_2 = interval_2;
	}

	public double getInterval_3() {
		return interval_3;
	}

	public void setInterval_3(double interval_3) {
		this.interval_3 = interval_3;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
