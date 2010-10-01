/*
 * Experimental solution to the Sort problem.
 * Quadratic solution #2.
 * The time limit must not accept it!
 *
 * Central Europe Regional Contest 2007.
 * Martin Kacer, 2007
 */

#include <stdio.h>
#include <stdlib.h>

/*
 * Another quadratic solution.
 * The time limit must not accept it!
 */

#define MAX 1000000

int nums[MAX];  /* input numbers */
int sort[MAX];  /* sorted indexes to 'nums' array */


int comp_idx(const int* a, const int* b) {
	return (nums[*a] != nums[*b]) ? nums[*a] - nums[*b] : *a - *b;
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
		for (i = 0; i < n; ++i) {
			int w = sort[i];
			if (w > i) {
				register int j;
				for (j = 0; j < n; ++j) {
					register int p = sort[j];
					if (p >= i && p <= w) {
						sort[j] = i + w - p;
					}
				}
			}
			if (i > 0) putchar(' ');
			printf("%d", w+1);
		}
		putchar('\n');
	}
	return 0;
}

