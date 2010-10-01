#include <stdio.h>
#include <string.h>

char buffer[102];

int main()
{
	int i,n,j;
	int all, bad;
	scanf("%i\n", &n);
	for (i=0; i<n; i++)
	{
		all = 0;
		bad = 0;
		while (fgets(buffer, 101, stdin) != NULL && buffer[0] != '\n')
		{
			strchr(buffer, 0)[-1] = 0;
			for (j=0; buffer[j]; j++)
			{
				all++;
				if (buffer[j] == '#') bad++;
			}
		}
		int frac = (10000 * (all-bad)) / all + 5;
		
		printf("Efficiency ratio is %d", frac/100);
		if ((frac%100)/10)
			printf(".%i", (frac%100)/10);
		printf("%%.\n");
	}
	return 0;
}
