package mindrust;

import ajava.io.InputStream;
import ajava.io.OutputStream;

public class Output {
    public void run(InputStream<Integer> in, OutputStream<Integer> out) {
	out.writeInt(in.readInt());
    }
}
