package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Assembler3{
    private int i;
    private int value;
    public void run(InputStream<Integer> in_1, InputStream<Integer> in_2, 
	    InputStream<Integer> in_3, InputStream<Integer> in_4, OutputStream<Integer> out){ 
	for(i=0;i<64;i++){
	    value=in_1.readInt();
	    out.writeInt(value);
	}
	for(i=0;i<64;i++){
	    value=in_2.readInt();
	    out.writeInt(value);
	}
	for(i=0;i<64;i++){
	    value=in_3.readInt();
	    out.writeInt(value);
	}
	for(i=0;i<64;i++){
	    value=in_4.readInt();
	    out.writeInt(value);
	}
    }
}



