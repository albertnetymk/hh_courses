package fft;
import ajava.io.InputStream;
import ajava.io.OutputStream;

class FirstFFT{
    private int[][] pairs=new int[2][2];
	private int[][] tmp=new int[1][2];

    public void run(InputStream<Integer> in, OutputStream<Integer> out_1, 
    		OutputStream<Integer> out_2){
	pairs[0][0]=in.readInt();
	pairs[0][1]=in.readInt();
	pairs[1][0]=in.readInt();
	pairs[1][1]=in.readInt();
	//twiddle operation
	tmp[0][0]=pairs[0][0]+pairs[1][0];
	tmp[0][1]=pairs[0][1]+pairs[1][1];
	out_1.writeInt(tmp[0][0]);
	out_1.writeInt(tmp[0][1]);
	out_2.writeInt(tmp[0][0]);
	out_2.writeInt(tmp[0][1]);
	tmp[0][0]=pairs[0][0]-pairs[1][0];
	tmp[0][1]=pairs[0][1]-pairs[1][1];
	out_1.writeInt(tmp[0][0]);
	out_1.writeInt(tmp[0][1]);
	out_2.writeInt(tmp[0][0]);
	out_2.writeInt(tmp[0][1]);
    }
}
