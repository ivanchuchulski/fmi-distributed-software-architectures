
import java.math.BigInteger;

/**
 * @author lisp
 */

public class TaskRunner {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("TaskRunner <num of elements> <num of threads>");
			System.exit(1);
		}

		int el_count = new Integer(args[0]);
		int thread_count = new Integer(args[1]);

		int chunk_size = el_count / thread_count;

		long a[] = new long[el_count];
		BigInteger res[] = new BigInteger[thread_count];

		Thread tr[] = new Thread[thread_count];

		for (int i = 0; i < thread_count; i++) {
			SumRunnable r = new SumRunnable(a, res, chunk_size, i, thread_count);
			Thread t = new Thread(r);
			tr[i] = t;
			t.start();
		}

		BigInteger sum = BigInteger.valueOf(0);
		// BigInteger sum = BigInteger.valueOf(1);
		for (int i = 0; i < thread_count; i++) {
			try {
				tr[i].join();
				sum = sum.add(res[i]);
				// sum = sum.multiply(res[i]);
			}
			catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}

		System.out.println("result: " + sum);
	}

}
