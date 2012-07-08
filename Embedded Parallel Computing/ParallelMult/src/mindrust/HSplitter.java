package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class HSplitter {
	private int resultCol;
	private int innerDim;
	private int col;

	public HSplitter(int resultCol, int innerDim int col) {
		this.resultCol = resultCol;
		this.innerDim = innerDim;
		this.col = col;
	}

	public void run(InputStream<Integer> in,
					OutputStream<Integer> out_down,
					OutputStream<Integer> out_right) {
		for (int i = 0; i < innerDim; i++) {
			// Take an integer, it's for the cell
			out_down.writeInt(in.readInt());

			for (int j = 0; j < resultCol-(col+1); j++) {
				out_right.writeInt(in.readInt());
			}
		}
	}
}
