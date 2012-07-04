package exercise1;
/**
 * CommandInterpreter can interpret SORT, SEARCH, RANDOM and QUIT four commands
 * 
 * @author albertnet
 * 
 */

public class CommandInterpreter {

	private enum Command {
		SORT, SEARCH, RANDOM, QUIT
	}

	private static int size = 100;
	private static int[] a = new int[size];
	private static int x = 0;

	private static java.util.Random r = new java.util.Random();
	private static java.io.BufferedReader in = new java.io.BufferedReader(
			new java.io.InputStreamReader(System.in));

	public static void execute(Command c) throws java.io.IOException {

		System.out.println("About to " + c + " ...");

		switch (c) {

		case SORT:
			java.util.Arrays.sort(a);
			System.out.println(java.util.Arrays.toString(a));
			break;

		case SEARCH:
			System.out.println("sought? ");
			x = new Integer(in.readLine());
			int pos = java.util.Arrays.binarySearch(a, x);
			if (pos < 0)
				System.out.println(x + " not found in "
						+ java.util.Arrays.toString(a));
			else
				System.out.println(x + " found at " + pos + " in "
						+ java.util.Arrays.toString(a));
			break;

		case RANDOM:
			System.out.println("size? ");
			size = new Integer(in.readLine());
			a = new int[size];
			for (int i = 0; i < size; i++)
				a[i] = r.nextInt(100);
			System.out.println(java.util.Arrays.toString(a));
			break;

		case QUIT:
			System.exit(0);

		default:
			;
		}

		System.out.println("Done with " + c + ".");
		System.out.println();

	}

	public static void main(String[] cmdLn) throws java.io.IOException {

		System.out.println();
		System.out.println("Welcome to the array command interpreter!\n");
		System.out.println("the following operations are available:");
		for (Command c : Command.values()) {
			System.out.println(c);
		}
		System.out.println();

		String line = null;
		while (true) {
			try {
				line = in.readLine();
				execute(Command.valueOf(line.toUpperCase()));
			} catch (IllegalArgumentException e) {
				System.out.println("Sorry! no Command " + line);
			}
		}
	}
}
