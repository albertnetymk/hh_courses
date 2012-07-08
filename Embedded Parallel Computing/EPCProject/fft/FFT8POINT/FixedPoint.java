public class FixedPoint {
	private int nbInt; // no of bits in integral part
	private int nbFrac; // no of bits in fractional part
	private Math math = new Math();

	public FixedPoint(int nbInt, int nbFrac){
		this.nbInt = nbInt;
		this.nbFrac = nbFrac;
	}
	
	public int add(int a, int b){
		// add and store the result in accumulator
		math.addacc(a, b, Marker.FIRST_LAST);
		// read accumulator for result and returns it.
		return math.rdacc_sum(Marker.LAST);
	}

	public int subtract(int a, int b){
		// subtract and store the result in accumulator
		math.subacc(a, b, Marker.FIRST_LAST);
		// read accumulator for result and returns it.
		return math.rdacc_sum(Marker.LAST);
	}

	public int multiply_32(int a, int b){
		math.mult_32_32(a, b, Marker.LAST);
		// reads high and low part of accumulator
		int lo = math.rdacc_lo(Marker.MORE);
		int hi = math.rdacc_hi(Marker.LAST);
		hi = ((hi << nbInt) | (lo >>> nbFrac));
		return hi;
	}
}
