package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class VSplitter {
    private int resultRow;
    private int innerDim;
    private int row;

    public VSplitter(int resultRow, int innerDim, int row) {
	this.resultRow = resultRow;
	this.innerDim = innerDim;
	this.row = row;
    }

    public void run(InputStream<Integer> in,
	    OutputStream<Integer> out_down,
	    OutputStream<Integer> out_right) {
	// Take a whole row and forward it to the cell.
	for (int i = 0; i < innerDim; i++) {
	    out_right.writeInt(in.readInt());
	}

	for (int i = 0; i < (resultRow-(row+1))*innerDim; i++) {
	    out_down.writeInt(in.readInt());
	}
    }
}
