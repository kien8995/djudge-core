/*
 * Sample solution to the Polygon problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Comparator;


public class polygon
{
	static final int MAX = 10000;

	static class Point
	{
		int x, y;
		Point[] next = new Point[2];
	}

	static Comparator<Point> COMP_HORZ = new Comparator<Point>() {
		public int compare(Point o1, Point o2) {
			return (o1.x != o2.x) ? o1.x - o2.x : o1.y - o2.y;
			}
	};
	static Comparator<Point> COMP_VERT = new Comparator<Point>() {
		public int compare(Point o1, Point o2) {
			return (o1.y != o2.y) ? o1.y - o2.y : o1.x - o2.x;
		}
	};

	int n;
	Point[] data = new Point[MAX];
	char[][] res = new char[2][MAX];
	Point first;

	void connect(Comparator<Point> comp, int targ) {
		Arrays.sort(data, 0, n, comp);
		for (int i = 0; i < n; i+=2) {
			data[i].next[targ] = data[i+1];
			data[i+1].next[targ] = data[i];
		}
	}

	int walk(int dir, char[] out) {
		Point lst = null, lst2 = null, now = first;
		int i = 0, turn = 0;
		do {
			lst2 = lst;
			lst = now;
			now = now.next[dir];
			dir = 1 - dir;
			out[i] = (now.x == lst.x)
					? ((now.y > lst.y) ? 'N' : 'S' )
					: ((now.x > lst.x) ? 'E' : 'W' );
			if (lst2 != null) {
				if ((now.x-lst.x)*(lst.y-lst2.y) > (now.y-lst.y)*(lst.x-lst2.x))
					++turn; else --turn;
			}
			++i;
		} while (now != first);
		return turn;
	}

	void printsol(char[] sol) {
		for (int i = 0; i < n; ++i) {
			System.out.print(sol[i]);
		}
		System.out.println();
	}

	void run() throws Exception {
		for (;;) {
			n = nextInt(); if (n == 0) break;
			for (int i = 0; i < n; ++i) {
				data[i] = new Point();
				data[i].x = nextInt();
				data[i].y = nextInt();
				if (i == 0) first = data[i];
			}
			connect(COMP_HORZ, 0);
			connect(COMP_VERT, 1);
			int t0 = walk(0, res[0]);
			int t1 = walk(1, res[1]);
			printsol(res[(t0>0) ? 0 : 1]);
		}
	}

	public static void main(String[] args) throws Exception {
		(new polygon()).run();
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
