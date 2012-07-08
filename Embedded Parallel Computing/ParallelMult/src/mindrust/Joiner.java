package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Joiner {
    private int resultRow;
    private int innerDim;
    private int row;

    public Joiner(int resultRow, int innerDim, int row) {
	this.resultRow = resultRow;
	this.innerDim = innerDim;
	this.row = row;
    }

    public void run(InputStream<Integer> in_left,
	    InputStream<Integer> in_bottom,
	    OutputStream<Integer> out_top) {
	// Forward the results of the whole row.
	for (int i = 0; i < innerDim; i++) {
	    out_top.writeInt(in_left.readInt());
	}

	// Forward bottom joiners values up only if not a sentinel joiner.
	if (row != resultRow-1) {
	    for (int j = 0; j < (resultRow-(row+1))*innerDim; j++) {
		out_top.writeInt(in_bottom.readInt());
	    }
	}
    }
}
