package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class AssemDistri{
	private int i,j;
	private int value;
	public void run(InputStream<Integer> in_1, InputStream<Integer> in_2, 
		InputStream<Integer> in_3, InputStream<Integer> in_4, OutputStream<Integer> out_1, OutputStream<Integer> out_2, OutputStream<Integer> out_3, OutputStream<Integer> out_4){ 
	    	for(i=0;i<4;i++){
	    
	    	for(j=0;j<4;j++){
		value=in_1.readInt();
		out_1.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_2.readInt();
		out_1.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_3.readInt();
		out_1.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_4.readInt();
		out_1.writeInt(value);
	    }
	    }
	    for(i=0;i<4;i++){
	    for(j=0;j<4;j++){
		value=in_1.readInt();
		out_2.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_2.readInt();
		out_2.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_3.readInt();
		out_2.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_4.readInt();
		out_2.writeInt(value);
	    }
	    }
	    for(i=0;i<4;i++){
	    for(j=0;j<4;j++){
		value=in_1.readInt();
		out_3.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_2.readInt();
		out_3.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_3.readInt();
		out_3.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_4.readInt();
		out_3.writeInt(value);
	    }
	    }
	    for(i=0;i<4;i++){
	    for(j=0;j<4;j++){
		value=in_1.readInt();
		out_4.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_2.readInt();
		out_4.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_3.readInt();
		out_4.writeInt(value);
	    }
	    for(j=0;j<4;j++){
		value=in_4.readInt();
		out_4.writeInt(value);
	    }
	    
	    }
	}
}


