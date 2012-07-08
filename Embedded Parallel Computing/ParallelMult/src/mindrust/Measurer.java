package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Measurer {
    private int dim;
    private int[][] matrixA;
    private int[][] matrixB;

    public Measurer(int dim) {
	this.dim = dim;
	this.matrixA = new int [dim][dim];
	this.matrixB = new int [dim][dim];
    }

    public void run(InputStream<Integer> inA,
	    InputStream<Integer> inB,
	    InputStream<Integer> inResult,
	    OutputStream<Integer> outA,
	    OutputStream<Integer> outB,
	    OutputStream<Integer> outResult) {
	int i = 0, j = 0;
	for (i = 0; i < dim; i++) {
	    for (j = 0; j < dim; j++) {
		matrixA[i][j] = inA.readInt();
	    }
	}
	for (i = 0; i < dim; i++) {
	    for (j = 0; j < dim; j++) {
		matrixB[i][j] = inB.readInt();
	    }
	}

	for (i = 0; i < dim; i++) {
	    for (j = 0; j < dim; j++) {
		outA.writeInt(matrixA[i][j]);
		outB.writeInt(matrixB[i][j]);
	    }
	}

	i = dim * dim;
	while (i-- > 0) {
	    outResult.writeInt(inResult.readInt());
	}
    }
}
