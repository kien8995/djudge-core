/*
 * Sample solution to the Billboard problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class billboard
{
	public static final int MAX = 24 + 2;  // add two for "border" tiles
	public static final char CH_BLACK = 'X';

	private static final int INF = MAX*MAX;

	int x, y;
	int mask;
	int board[] = new int[MAX];  // initial board
	int work[] = new int[MAX];  // working copy
	int cnt;

	void togglebits(int row, int bits) {
		for (int b = 2; (b & mask) == b; b<<=1) {
			if ((bits & b) != 0) ++cnt;
		}
		work[row-1] ^= bits;
		work[row] ^= (bits ^ (bits<<1) ^ (bits>>1));
		work[row+1] ^= bits;
	}

	void run() throws Exception {
		for (;;) {
			x = nextInt(); y = nextInt();
			if (x*y == 0) break;
			mask = (~(-1<<y))<<1;
			for (int i = 1; i <= x; ++i) {
				String s = nextToken();
				board[i] = 0;
				for (int j = 0; j < y; ++j) {
					if (s.charAt(j) == CH_BLACK) board[i] |= 1;
					board[i] <<= 1;
				}
			}
			int best = INF;
			for (int f = 0; (f & mask) == f; f += 2) {
				for (int i = 1; i <= x; ++i) work[i] = board[i];
				cnt = 0;
				togglebits(1, f);
				for (int i = 2; i <= x; ++i) {
					togglebits(i, mask & work[i-1]);
				}
				if ((mask & work[x]) == 0 && cnt < best) {
					best = cnt;
				}
			}
			if (best < INF) {
				System.out.println("You have to tap " + best + " tiles.");
			} else {
				System.out.println("Damaged billboard.");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		(new billboard()).run();
	}

	StringTokenizer st = new StringTokenizer("");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	String nextToken() throws Exception {
		while (!st.hasMoreTokens()) st = new StringTokenizer(input.readLine());
		return st.nextToken();
	}

	int nextInt() throws Exception {
		return Integer.parseInt(nextToken());
	}
}
