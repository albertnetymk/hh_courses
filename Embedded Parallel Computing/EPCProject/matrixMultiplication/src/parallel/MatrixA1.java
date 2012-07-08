package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class MatrixA1{
    private int value,i,j;
    public void run( InputStream<Integer> in,OutputStream<Integer> out_1, OutputStream<Integer> out_2, OutputStream<Integer> out_3, OutputStream<Integer> out_4){
	for(j=0;j<4;j++){
	    for(i=0;i<16;i++){
		value=in.readInt();
		out_1.writeInt(value);
	    }
	}
	for(j=0;j<4;j++){
	    for(i=0;i<16;i++){
		value=in.readInt();
		out_2.writeInt(value);
	    }
	}
	for(j=0;j<4;j++){
	    for(i=0;i<16;i++){
		value=in.readInt();
		out_3.writeInt(value);
	    }
	}
	for(j=0;j<4;j++){
	    for(i=0;i<16;i++){
		value=in.readInt();
		out_4.writeInt(value);
	    }
	}
    }
}
