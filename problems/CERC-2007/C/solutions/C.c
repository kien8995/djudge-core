/*
 * Experimental solution to the Cell problem.
 * Relatively simple solution running in O(n^3).
 * Must be eliminated by the time limit!
 *
 * Central Europe Regional Contest 2007.
 * Martin Kacer, 2007
 */

#include <stdio.h>
#include <math.h>

#define MAX 10000
#define EPS 1E-8

int n;
int x[MAX], y[MAX];
int r;


int main (void) {
	int i, j, k, cnt, best;
	double d, w, cx, cy;
	for (;;) {
		scanf("%d%d", &n, &r);
		if (n == 0) break;
		for (i = 0; i < n; ++i) {
			scanf("%d%d", x+i, y+i);
		}
		best = 1;
		for (i = 0; i < n; ++i) {
			for (j = 0; j < n; ++j) {
				if (x[i] == x[j] && y[i] == y[j]) continue;
				d = sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
				if (d > 2*r + EPS) continue;
				w = sqrt(r*r - d*d/4 + 1E-16);
				cx = (x[i]+x[j])*1.0/2 + (y[i]-y[j])*w/d;
				cy = (y[i]+y[j])*1.0/2 - (x[i]-x[j])*w/d;
				cnt = 0;
				for (k = 0; k < n; ++k) {
					if ((x[k]-cx)*(x[k]-cx) + (y[k]-cy)*(y[k]-cy) < r*r + EPS) {
						++cnt;
					}
				}
				if (cnt > best) best = cnt;
			}
		}
		printf("%d\n", best);
	}
	return 0;
}
