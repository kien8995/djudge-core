/*
 * Sample solution to the Polygon problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

#include <stdio.h>
#include <stdlib.h>

// must be at least one higher
#define MAX 1024

int x[MAX], y[MAX]; // coordinates
int ord[MAX]; // order
int nxt[2][MAX]; // horizontal and vertical neighbor index
int dir[2][MAX]; // horizontal and vertical neighbor direction

char result[MAX];

int comp_xy(const void* aa, const void* bb) {
	int a = *(const int*)aa, b = *(const int*)bb;
	return (x[a] != x[b]) ? x[a] - x[b] : y[a] - y[b];
}

int comp_yx(const void* aa, const void* bb) {
	int a = *(const int*)aa, b = *(const int*)bb;
	return (y[a] != y[b]) ? y[a] - y[b] : x[a] - x[b];
}

int walk(int o) {
	int i = 0, d = 0, pos = 0;
	char last = 'x';
	do {
		char ch = result[i++] = dir[o][pos];
		pos = nxt[o][pos];
		o = 1 - o;
		// count left and right turns
		switch (last) {
			case 'S': if (ch=='W') ++d; else --d; break;
			case 'N': if (ch=='E') ++d; else --d; break;
			case 'W': if (ch=='N') ++d; else --d; break;
			case 'E': if (ch=='S') ++d; else --d; break;
		}
		last = ch;
	} while (pos != 0);
	result[i] = '\0';
	return d;
}

int main()
{
	for (;;) {
		int cnt, i;
		if (scanf("%d", &cnt) < 1) break;
		if (cnt == 0) break;
		for (i = 0; i < cnt; ++i) {
			scanf("%d%d", x+i, y+i);
			ord[i] = i;
		}
		qsort(ord, cnt, sizeof(ord[0]), comp_yx);
		for (i = 0; i < cnt; i+=2) {
			nxt[0][ord[i]] = ord[i+1];
			dir[0][ord[i]] = 'E';
			nxt[0][ord[i+1]] = ord[i];
			dir[0][ord[i+1]] = 'W';
		}
		qsort(ord, cnt, sizeof(ord[0]), comp_xy);
		for (i = 0; i < cnt; i+=2) {
			nxt[1][ord[i]] = ord[i+1];
			dir[1][ord[i]] = 'N';
			nxt[1][ord[i+1]] = ord[i];
			dir[1][ord[i+1]] = 'S';
		}
		if (walk(0) < 0) walk(1);
		printf("%s\n", result);
	}
	return 0;
}
