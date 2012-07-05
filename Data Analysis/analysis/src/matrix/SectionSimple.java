package matrix;

public class SectionSimple extends Section {

	private double height;
	private double width;

	public SectionSimple(String name, int number, double height, double width) {
		super(name, number);
		this.height = height;
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

}