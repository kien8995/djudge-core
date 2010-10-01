#include <stdio.h>

int main(void) {
	int len, a, x, max;
	char ch;
	for (;;) {
		scanf("%d%d\n", &len, &a);
		if (a == 0) break;
		max = 0;
		while (a--) {
			scanf("%d %c\n", &x, &ch);
			if (ch != 'L') x = len - x;
			if (x > max) max = x;
		}
		printf ("The last ant will fall down in %d seconds.\n", max);
	}
	return 0;
}
