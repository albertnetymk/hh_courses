package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Cell {
    private int innerDim;
    private int row;
    private int col;
    private int vLeft[];
    private int vTop[];

    public Cell(int innerDim, int row, int col) {
	this.innerDim = innerDim;
	this.row = row;
	this.col = col;
	this.vLeft = new int[innerDim];
	this.vTop = new int[innerDim];
    }

    public void run(InputStream<Integer> in_top,
	    InputStream<Integer> in_left,
	    OutputStream<Integer> out_right, 
	    OutputStream<Integer> out_down) {

	int i = 0, result = 0;

	for (i = 0; i < innerDim; i++) {
	    this.vLeft[i] = in_left.readInt();
	    if (this.col != this.innerDim-1) // Forward right only if not sentinel cell
		out_right.writeInt(this.vLeft[i]);
	}

	for (i = 0; i < innerDim; i++) {
	    this.vTop[i] = in_top.readInt();
	    if (this.row != this.innerDim-1) // Forward down only if not sentinel cell
		out_down.writeInt(this.vTop[i]);
	}

	// Compute result
	for (i = 0; i < innerDim; i++) {
	    result += this.vLeft[i]*this.vTop[i];
	}

	// Now possibly forward neighbour values.
	for (int j = 0; j < col; j++) {
	    out_right.writeInt(in_left.readInt());
	}

	// Forward our own result to the neighbour.
	out_right.writeInt(result);
    }
}
