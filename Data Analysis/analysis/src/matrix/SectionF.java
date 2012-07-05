package matrix;

public class SectionF extends Section {
	private double width;
	private double height_1;
	private double height_2;
	private double height_3;

	public SectionF(String name) {
		super(name, 3);
		width = 5;
		height_1 = 5.27;
		height_2 = 4.48;
		height_3 = 7.87;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight_1() {
		return height_1;
	}

	public void setHeight_1(double height_1) {
		this.height_1 = height_1;
	}

	public double getHeight_2() {
		return height_2;
	}

	public void setHeight_2(double height_2) {
		this.height_2 = height_2;
	}

	public double getHeight_3() {
		return height_3;
	}

	public void setHeight_3(double height_3) {
		this.height_3 = height_3;
	}

}
