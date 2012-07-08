/**********************************************************
*
* File: FFT.java
*
* Description: this java object associated with FFT.astruct
* interface. This object performs one butterfly computation
*
**********************************************************/
package FFT8POINT;
import ajava.io.InputStream;
import ajava.io.OutputStream;


public class FFT {
	private int N; // no of points
	// array of complex numbers

	private Complex[],x;
	// twiddle factors
	private final int cos;
	private final int sin;
	// for fixed point calculations
	private FixedPoint fp = new FixedPoint(8,24);
	public FFT(int propPoints, int propCosVal, int propSinVal){
		N = propPoints;
		x = new Complex[N];
		cos = propCosVal;
		sin = propSinVal;
	}
	public void run(InputStream<Integer> inReal1,
			InputStream<Integer> inImg1,
			InputStream<Integer> inReal2,
			InputStream<Integer> inImg2,
			OutputStream<Integer> outReal1,
			OutputStream<Integer> outImg1,
			OutputStream<Integer> outReal2,
			OutputStream<Integer> outImg2) {
		int i;
		for(i=0; i<N; i+=2){ // read real and imaginary signal
			x[i].real = inReal1.readInt();
			x[i].img = inImg1.readInt();
			x[i+1].real = inReal2.readInt();
			x[i+1].img = inImg2.readInt();
		}
		// Butterfly calculation using fixed point numerics
		int TR = fp.subtract(fp.multiply_32(x[1].real, cos),
				fp.multiply_32(x[1].img, sin));
		int TI = fp.add(fp.multiply_32(x[1].real, sin),
				fp.multiply_32(x[1].img, cos));
		x[1].real = fp.subtract(x[0].real, TR);
		x[1].img = fp.subtract(x[0].img, TI);
		x[0].real = fp.add(x[0].real, TR);
		x[0].img = fp.add(x[0].img, TI);
		for(i=0; i<N; i+=2) {
			outReal1.writeInt(x[i].real);
			outImg1.writeInt(x[i].img);
			outReal2.writeInt(x[i+1].real);
			outImg2.writeInt(x[i+1].img);
		}
	}
}
