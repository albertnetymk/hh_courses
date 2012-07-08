package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Add {
    private int i,j;
    private int r,c;
    private int[] value=new int[16];
    public void run(InputStream<Integer> in_r, InputStream<Integer> in_c, OutputStream<Integer> out){ 
	for(j=0;j<16;j++){
	    for(i=0;i<16;i++){
		r=in_r.readInt();
		c=in_c.readInt();
		value[i]=value[i]+r*c;
	    }
	}
	for(i=0;i<16;i++){
	    out.writeInt(value[i]);
	}
    }
}


