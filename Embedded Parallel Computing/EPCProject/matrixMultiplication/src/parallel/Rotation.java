package parallel;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Rotation{
    public int i,j;
    public int rotation;
    public int[] b=new int[16];
    public Rotation(int r){
	rotation=r;
    }
    public void run(InputStream<Integer> in, OutputStream<Integer> out){
	for(i=0;i<16;i++){
	   b[i]=in.readInt();
	} 
	for(i=rotation;i<16;i++){
	    for(j=i;j<16;j++){
		out.writeInt(b[j]);
	    }
	    for(j=0;j<i;j++){
		out.writeInt(b[j]);
	    }
	}
	for(i=0;i<rotation;i++){
	    for(j=i;j<16;j++){
		out.writeInt(b[j]);
	    }
	    for(j=0;j<i;j++){
		out.writeInt(b[j]);
	    }
	}
    }

}
