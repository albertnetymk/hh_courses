/**********************************************************
*
* File: Splitter.java
*
* Description: this java object distribute points in even
* and odd order for bit-reversal sorting
*
**********************************************************/

package FFT8POINT;
import ajava.io.InputStream;
import ajava.io.OutputStream;
public class Splitter {
	// no of points to read from previous splitter object
	private int N;
	public Splitter(int propN){
		this.N = propN;
	}
	public void run(InputStream<Integer> inReal,
			InputStream<Integer> inImg,
			OutputStream<Integer> outReal1,
			OutputStream<Integer> outImg1,
			OutputStream<Integer> outReal2,
			OutputStream<Integer> outImg2) {
		for(int i=0; i<N; i+=2) {
			// send even point to upper output stream
			outReal1.writeInt( inReal.readInt() );
			outImg1.writeInt( inImg.readInt() );
			// send odd point to lower output stream
			outReal2.writeInt( inReal.readInt() );
			outImg2.writeInt( inImg.readInt() );
		}
	}
}
