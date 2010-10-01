/*
 * Sample solution to the Keys problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.HashSet;


public class keys
{
	static final int MAX = 128;
	static final int MAXMAX = MAX * MAX * 16;

	static final char CH_START = '*';
	static final char CH_WALL = '#';
	static final char CH_EXIT = 'X';
	static final String CH_KEYS = "rgby";
	static final String CH_DOOR = "RGBY";

	static class Position {
		int xx, yy, kk, dd;
		static int toCode(int x, int y, int k) {
			return (x * MAX + y) << 4 | k;
		}
		void fromCode(int val) {
			kk = val & 0xF;
			val >>= 4;
			xx = val / MAX;
			yy = val % MAX;
		}
	}
	Position pos = new Position();

	char[][] map = new char[MAX][MAX];
	int[] clos = new int[MAXMAX];
	LinkedList<Integer> queue = new LinkedList<Integer>();
	int dist;
	int r, c;

	void enqueue(int x, int y, int k, int d) {
		if (x < 0 || x >= r || y < 0 || y >= c) return;
		if (map[x][y] == CH_WALL) return;
		if (map[x][y] == CH_EXIT) { if (dist<0) dist=d; return; }
		int i = CH_DOOR.indexOf(map[x][y]);
		if (i >= 0 && (k & (1 << i)) == 0) return;
		i = CH_KEYS.indexOf(map[x][y]);
		if (i >= 0) k |= (1 << i);
		int p = Position.toCode(x, y, k);
		if (clos[p] >= 0) return;
		clos[p] = d;
		queue.addLast(Integer.valueOf(p));
	}

	void run() throws Exception {
		int si, sj;
		for (;;) {
			r = nextInt(); c = nextInt();
			if (r * c == 0) break;
			si = sj = -1;
			for (int i = 0; i < r; ++i) {
				String s = nextToken();
				for (int j = 0; j < c; ++j) {
					if ((map[i][j] = s.charAt(j)) == CH_START) {
						si = i; sj = j;
					}
				}
			}
			for (int i = 0; i < MAXMAX; ++i) clos[i] = -1;
			queue.clear();
			enqueue(si, sj, 0, 0);
			dist = -1;
			while (dist < 0 && !queue.isEmpty()) {
				int p = queue.removeFirst().intValue();
				int d = clos[p];
				pos.fromCode(p);
				enqueue(pos.xx-1, pos.yy, pos.kk, d+1);
				enqueue(pos.xx+1, pos.yy, pos.kk, d+1);
				enqueue(pos.xx, pos.yy-1, pos.kk, d+1);
				enqueue(pos.xx, pos.yy+1, pos.kk, d+1);
			}
			if (dist < 0) System.out.println("The poor student is trapped!");
			else System.out.println("Escape possible in " + dist + " steps.");
		}
	}

	public static void main(String[] args) throws Exception {
		(new keys()).run();
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
