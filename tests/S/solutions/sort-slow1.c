/*
 * Experimental solution to the Sort problem.
 * Quadratic solution #1.
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

int rev1[MAX], rev2[MAX], revc; /* list of reversed intervals */



int comp_idx(const int* a, const int* b) {
	return (nums[*a] != nums[*b]) ? nums[*a] - nums[*b] : *a - *b;
}


int main(void)
{
	int j;
	for (;;) {
		int i, j, w, n;
		int was = 0;
		if (scanf("%d", &n) < 1) break;
		if (!n) break;
		for (i = 0; i < n; ++i) {
			scanf("%d", nums+i);
			sort[i] = i;
		}
		qsort(sort, n, sizeof(int), comp_idx);
		revc = 0;
		for (i = 0; i < n; ++i) {
			int w = sort[i];
			for (j = 0; j < revc; ++j)
				if (w >= rev1[j] && w <= rev2[j]) {
					w = rev2[j] + rev1[j] - w;
				}
			if (w > i) {
				rev1[revc] = i;
				rev2[revc++] = w;
			}
			if (was) putchar(' ');
			printf("%d", w+1);
			was = 1;
		}
		putchar('\n');
	}
	return 0;
}

