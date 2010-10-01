/*
 * Sample solution to the Cell problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;


public class cell
{
	static final int MAX = 10000;
	static final double EPS = 1E-6;

	static class interest implements Comparable<interest> {
		double cx, cy;  // circle center (relative)
		boolean left;  // left or right edge?

		public int compareTo(interest o) {
			if (iszero(this.cx - o.cx) && iszero(this.cy - o.cy)) {
				if (this.left && !o.left) return 1;
				if (!this.left && o.left) return -1;
				return 0;
			}
			if (iszero(this.cx) && this.cy > 0) {
				return this.left ? -1 : 1;
			}
			if (iszero(o.cx) && o.cy > 0) {
				return o.left ? 1 : -1;
			}
			if (this.cx > 0 && o.cx < 0) return -1;
			if (this.cx < 0 && o.cx > 0) return 1;
			if (this.cx > 0) {
				return (this.cy > o.cy) ? -1 : 1;
			} else {
				return (this.cy < o.cy) ? -1 : 1;
			}
		}
	}

	double r;
	int n;
	double[] xx = new double[MAX];  // point coordinates
	double[] yy = new double[MAX];

	int cnt;
	interest[] ip;

	cell() {
		ip = new interest[2*MAX];
		for (int i = 0; i < ip.length; ++i) {
			ip[i] = new interest();
		}
	}

	static boolean iszero(double x) {
		return (x < EPS) && (x > -EPS);
	}

	double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}

	double pyth(double c, double a) {
		double bb = c*c - a*a;
		if (bb < 0 && bb + EPS > 0) bb = 0;
		return Math.sqrt(bb);
	}

	int testPoint(int p) throws Exception {
		int plus = 0;
		int inside = 0, best, start;
		double x = xx[p], y = yy[p];
		cnt = 0;
		for (int i = 0; i < n; ++i) {
			if (iszero(x-xx[i]) && iszero(y-yy[i])) { ++plus; continue; }
			double d = dist(x,y, xx[i],yy[i]);
			if (d > 2*r + EPS) continue;
			double w = pyth(r, d/2);
			ip[cnt].cx = (xx[i]-x)/2 + (yy[i]-y) * w/d;
			ip[cnt].cy = (yy[i]-y)/2 - (xx[i]-x) * w/d;
			ip[cnt++].left = true;
			ip[cnt].cx = (xx[i]-x)/2 - (yy[i]-y) * w/d;
			ip[cnt].cy = (yy[i]-y)/2 + (xx[i]-x) * w/d;
			ip[cnt++].left = false;
			if (dist(x,y+r, xx[i],yy[i]) < r+EPS) ++inside;
		}
		start = best = inside;
		Arrays.sort(ip, 0, cnt);
		//System.out.println("DBG: POINT #" + (p+1) + ": [" + xx[p] + "," + yy[p] + "] start with: " + inside);
		for (int i = 0; i < cnt; ++i) {
			if (ip[i].left) --inside; else ++inside;
			if (inside > best) best = inside;
			//System.out.println("  - [" + ip[i].cx + "," + ip[i].cy + "] " + (ip[i].left?"left ":"right ") + inside);
		}
		if (start != inside) System.out.println("ERROR!!!");
		return best + plus;
	}

	void run() throws Exception {
		for (;;) {
			n = nextInt(); if (n == 0) break;
			r = nextInt();
			for (int i = 0; i < n; ++i) {
				xx[i] = nextInt();
				yy[i] = nextInt();
			}
			int now, max = 0;
			for (int i = 0; i < n; ++i) {
				now = testPoint(i);
				if (now > max) max = now;
			}
			System.out.println("It is possible to cover " + max + " points.");
		}
	}

	public static void main(String[] args) throws Exception {
		(new cell()).run();
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
