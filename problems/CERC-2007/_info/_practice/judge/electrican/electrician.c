#include <stdio.h>
#include <string.h>

char buffer[102];

int main()
{
	int x;
	for (;;)
	{
		scanf("%d", &x);
		if (x == 0) break;
		printf((x == 2) ? "Bad luck!\n"
				: "Electrician needs 1 trips.\n");
	}
	return 0;
}
