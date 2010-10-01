/*
 * Experimental solution to the Sort problem.
 * Quadratic solution #3.
 * The time limit must not accept it!
 *
 * Central Europe Regional Contest 2007.
 * Martin Kacer, 2007
 */

#include <stdio.h>
#include <stdlib.h>

#define MAX 1000000

int nums[MAX];  /* input numbers */
int sort[MAX];  /* sorted indexes to 'nums' array */
int targ[MAX];  /* to which position should go the i-th number */


int comp_idx(const int* a, const int* b) {
	return (nums[*a] != nums[*b]) ? nums[*a] - nums[*b] : *a - *b;
}

void reverse(int beg, int end) {
	int i, o, t1, t2;
	for (i = beg, o = end; i < o; ++i, --o) {
		t1 = targ[i], t2 = targ[o];
		sort[t1] = o, sort[t2] = i;
		targ[i] = t2, targ[o] = t1;
	}
}

int main(void)
{
	for (;;) {
		int i, n;
		if (scanf("%d", &n) < 1) break;
		if (!n) break;
		for (i = 0; i < n; ++i) {
			scanf("%d", nums+i);
			sort[i] = i;
		}
		qsort(sort, n, sizeof(int), comp_idx);
		for (i = 0; i < n; ++i) targ[sort[i]] = i;
		for (i = 0; i < n; ++i) {
			int w = sort[i];
			if (w > i) {
				reverse(i, w);
			}
			if (i > 0) putchar(' ');
			printf("%d", w+1);
		}
		putchar('\n');
	}
	return 0;
}

