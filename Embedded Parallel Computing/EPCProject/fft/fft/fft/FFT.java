package fft;
import ajava.io.InputStream;
import ajava.io.OutputStream;

class FFT{
    // number of values & pairs
    private int[][] pairs=new int[2][2];
    private int[][] twiddle=new int[2][2];
    private int[][] result=new int[1][2];
    private int[][] tmp=new int[1][2];

    private FixedPoint arithmetic=new FixedPoint(8,24);
	public FFT(int a1, int a2, int b1, int b2){
		twiddle[0][0]=a1;
		twiddle[0][1]=a2;	
		twiddle[1][0]=b1;
		twiddle[1][1]=b2;
	}
	private void multiply(int a1, int a2, int b1, int b2){
		tmp[0][0]=arithmetic.multiply(a1,b1)-arithmetic.multiply(a2,b2);
		tmp[0][1]=arithmetic.multiply(a1,b2)+arithmetic.multiply(a2,b1);
	}
    public void run(InputStream<Integer> in_1, InputStream<Integer> in_2,
	    OutputStream<Integer> out_1, OutputStream<Integer> out_2){
	
	//Every FFT will work with 2 point.
    
    	//Calculate the first point
	pairs[0][0]=in_1.readInt();
	pairs[0][1]=in_1.readInt();
	pairs[1][0]=in_2.readInt();
	pairs[1][1]=in_2.readInt();
	multiply(twiddle[0][0], twiddle[0][1], pairs[1][0], pairs[1][1]);
	result[0][0]=pairs[0][0]+tmp[0][0];
	result[0][1]=pairs[0][1]+tmp[0][1];
	out_1.writeInt(result[0][0]);
	out_1.writeInt(result[0][1]);
	out_2.writeInt(result[0][0]);
	out_2.writeInt(result[0][1]);
	
		//Calculate the second point
	pairs[0][0]=in_1.readInt();
	pairs[0][1]=in_1.readInt();
	pairs[1][0]=in_2.readInt();
	pairs[1][1]=in_2.readInt();
	multiply(twiddle[0][0], twiddle[0][1], pairs[1][0], pairs[1][1]);
	result[0][0]=pairs[1][0]+tmp[0][0];
	result[0][1]=pairs[1][1]+tmp[0][1];
	out_1.writeInt(result[0][0]);
	out_1.writeInt(result[0][1]);
	out_2.writeInt(result[0][0]);
	out_2.writeInt(result[0][1]);
    }
}
	    


