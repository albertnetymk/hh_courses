package sequential;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Matrix{
    private int value,i,j,k;
    private int[][] a=new int[16][16];
    private int[][] b=new int[16][16];
    public void run( InputStream<Integer> in,OutputStream<Integer> out){
	for(i=0;i<16;i++){
	    for(j=0;j<16;j++){
		value=in.readInt();
		a[i][j]=value;
	    }
	}
	for(i=0;i<16;i++){
	    for(j=0;j<16;j++){
		value=in.readInt();
		b[i][j]=value;
	    }
	}
	for(i=0;i<16;i++){
	    for(j=0;j<16;j++){
		value=0;
		for(k=0;k<16;k++){
		    value=value+a[i][k]*b[k][j];
		}
		out.writeInt(value);
	    }
	}
    }
}
