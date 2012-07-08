package fft;
import ajava.io.InputStream;
import ajava.io.OutputStream;

class AssemblerTwo{
    private int[][] tmp=new int[1][2];

    public void run(InputStream<Integer> in_1, InputStream<Integer> in_2,
			OutputStream<Integer> out){
		tmp[0][0]=in_1.readInt();
		tmp[0][1]=in_1.readInt();
		out.writeInt(tmp[0][0]);
		out.writeInt(tmp[0][1]);
		tmp[0][0]=in_2.readInt();
		tmp[0][1]=in_2.readInt();
		out.writeInt(tmp[0][0]);
		out.writeInt(tmp[0][1]);
	}
}

