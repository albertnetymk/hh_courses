package fft;
import ajava.io.InputStream;
import ajava.io.OutputStream;

class Distribute{
	int[][] tmp=new int[1][2];

    public void run(InputStream<Integer> in, OutputStream<Integer> out_1,
	    OutputStream<Integer> out_2){
	tmp[0][0]=in.readInt();
	tmp[0][1]=in.readInt();
	out_1.writeInt(tmp[0][0]);
	out_1.writeInt(tmp[0][1]);
	tmp[0][0]=in.readInt();
	tmp[0][1]=in.readInt();
	out_2.writeInt(tmp[0][0]);
	out_2.writeInt(tmp[0][1]);
    }
}

