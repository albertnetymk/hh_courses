/**********************************************************
*
* File: Join.java
*
* Description: this java object combines total no of points
* and finally write to output
*
**********************************************************/
package FFT8POINT;
import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Join {
	// no of points to read from previous splitter object
	private int N;
	public Join(int propN){
		this.N = propN;
	}
	public void run(InputStream<Integer> inReal1,
			InputStream<Integer> inImg1,
			InputStream<Integer> inReal2,
			InputStream<Integer> inImg2,
			OutputStream<Integer> outReal,
			OutputStream<Integer> outImg) {
		for(int i=0; i<N; i++) {
			outReal.writeInt( inReal1.readInt() );
			outImg.writeInt( inImg1.readInt() );
		}
		for(int i=0; i<N; i++) {
			outReal.writeInt( inReal2.readInt() );
			outImg.writeInt( inImg2.readInt() );
		}
	}
}
