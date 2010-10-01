#include <stdio.h>
#include <string.h>

int main()
{
    int diff,i,ll;
    char LINE1[200];
    char LINE2[200];
    for (;;)
    {
	scanf("%s\n",LINE1);
	if (LINE1[0]!='0' && LINE1[0]!='1') break;
	scanf("%s\n",LINE2);
	ll=strlen(LINE1);
	diff=0;
	for (i=0;i<ll;i++)
	    if (LINE1[i]!=LINE2[i]) ++diff;
	printf("Hamming distance is %d.\n",diff);
    }
    return 0;
}
