/*
 * Sample solution to the Sort problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;


public class sort
{
	static final int MAX = 1000000;
	static final int SQMAX = 1000;

	int n;  // how many numbers
	int sqn;  // square root

	int[] sidx = new int[MAX];  // original values, then sorted indexes
	int[] nidx = new int[MAX];  // temporary: new values
	int[] pos = new int[MAX];  // temporary: position of this number
	Integer[] srt = new Integer[MAX];  // temporary: sorting only

	int rev1[] = new int[SQMAX];  // start of reversed part
	int rev2[] = new int[SQMAX];  // end of reversed part
	int revc;

	int prev[] = new int[MAX];  // forward link
	int next[] = new int[MAX];  // backward link

	void init() {
		revc = 0;
		for (int i = 0; i < n; ++i) {
			prev[i] = i-1;
			next[i] = i+1;
		}
		prev[0] = next[n-1] = -1;
	}

	int findCur(int w) {
		for (int j = 0; j < revc; ++j)
			if (w >= rev1[j] && w <= rev2[j]) {
				w = rev2[j] + rev1[j] - w;
			}
		return w;
	}

	boolean neg;
	int findOrig(int w) {
		neg = false;
		for (int j = revc; --j >= 0; )
			if (w >= rev1[j] && w <= rev2[j]) {
				w = rev2[j] + rev1[j] - w;
				neg = !neg;
			}
		return w;
	}

	void reverse(int beg, int end) {
		int bb = findOrig(beg);
		int[] bprev = neg ? next : prev;
		int ee = findOrig(end);
		int[] enext = neg ? prev : next;

		if (bprev[bb] >= 0) {
			if (next[bprev[bb]] == bb) next[bprev[bb]] = ee;
			else prev[bprev[bb]] = ee;
		}
		if (enext[ee] >= 0) {
			if (prev[enext[ee]] == ee) prev[enext[ee]] = bb;
			else next[enext[ee]] = bb;
		}
		int x = bprev[bb];
		bprev[bb] = enext[ee];
		enext[ee] = x;

		if (revc < sqn) {
			rev1[revc] = beg;
			rev2[revc++] = end;
		} else {
			reorder();
		}
	}

	void reorder() {
		for (int i = 0; i < n; ++i) pos[sidx[i]] = i;
		int x = findOrig(0), p = -1, c = 0;
		while (x >= 0) {
			nidx[pos[x]] = c++;
			int nx = (prev[x] == p) ? next[x] : prev[x];
			p = x;
			x = nx;
		}
		if (c != n) System.out.println("ERROR!");
		int[] tmp = nidx;
		nidx = sidx;
		sidx = tmp;
		init();
	}

	void compute() throws Exception {
		for (int i = 0; i < n; ++i) {
			int w = findCur(sidx[i]);
			if (w > i) {
				reverse(i, w);
			}
			if (i > 0) System.out.print(' ');
			System.out.print(w+1);
		}
	}

	void run() throws Exception {
		for (;;) {
			n = nextInt();
			sqn = 5 * (int) Math.sqrt(n);
			if (sqn < 1) sqn = 1;
			if (sqn > SQMAX) sqn = SQMAX;
			if (n == 0) break;
			for (int i = 0; i < n; ++i) {
				sidx[i] = nextInt();
				srt[i] = Integer.valueOf(i);
			}
			java.util.Arrays.sort(srt, 0, n, new java.util.Comparator<Integer>() {
				public int compare(Integer a, Integer b) {
					int a1 = sidx[a.intValue()], b1 = sidx[b.intValue()];
					return (a1 != b1) ? a1 - b1 : a.intValue() - b.intValue();
				}
			});
			for (int i = 0; i < n; ++i) {
				sidx[i] = srt[i].intValue();
			}
			init();
			compute();
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		(new sort()).run();
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
