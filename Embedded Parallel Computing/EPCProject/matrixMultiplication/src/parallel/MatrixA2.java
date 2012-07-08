package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class MatrixA2{
    private int value,i;
    public void run( InputStream<Integer> in,OutputStream<Integer> out_1, OutputStream<Integer> out_2, OutputStream<Integer> out_3, OutputStream<Integer> out_4){
	for(i=0;i<16;i++){
	    value=in.readInt();
	    out_1.writeInt(value);
	}
	for(i=0;i<16;i++){
	    value=in.readInt();
	    out_2.writeInt(value);
	}
	for(i=0;i<16;i++){
	    value=in.readInt();
	    out_3.writeInt(value);
	}
	for(i=0;i<16;i++){
	    value=in.readInt();
	    out_4.writeInt(value);
	}
    }
}
