/*
 * Sample solution to the Roshambo problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#define MAXNAMELEN 20

char name1[MAXNAMELEN+5], name2[MAXNAMELEN+5];
int win1, win2;

int get_val(char *str)
{
	int val = -1;
	
	assert(strlen(str)>=2);
	if(!strncmp("Ka", str,2) || !strncmp("Ro", str,2) || 
		 !strncmp("Pi", str,2) || !strncmp("St", str,2) || 
		 !strncmp("Ko", str,2) || !strncmp("Sa", str,2) || 
		 !strncmp("Gu", str,2)) { assert(val==-1); val = 0; }
	if(!strncmp("Nu", str,2) || !strncmp("Sc", str,2) || 
		 !strncmp("Ci", str,2) || !strncmp("Ol", str,2) || 
		 !strncmp("Fo", str,2) || !strncmp("Ch", str,2) || 
		 !strncmp("No", str,2) || !strncmp("Ti", str,2)) 
	   { assert(val==-1); val = 1; }
	if(!strncmp("Pa", str,2) || !strncmp("Fe", str,2) || 
		 !strncmp("Ca", str,2) || !strncmp("Re", str,2)) 
		 { assert(val==-1); val = 2; }
	assert(val!=-1);
	return val;
}

int main(void)
{
	int val1, val2, ii, endnow=0;
	char tmps[100];
	
	for(ii=1;!endnow;ii++)
	{
		scanf(" %*s %s ", name1);
		scanf(" %*s %s ", name2);
		win1=win2=0;
		while(1)
		{
			scanf(" %s ", tmps);
			if(tmps[0]=='-') break;
			if(tmps[0]=='.') { endnow = 1; break; }
			val1 = get_val(tmps);
			scanf(" %s ", tmps);
			val2 = get_val(tmps);
			if(val1!=val2)
			{
				if((val1==1 && val2==2) || (val1==0 && val2==1) || 
						(val1==2 && val2==0) ) win1++;
				else win2++;
			}
		}
		printf("Game #%d:\n", ii);
		printf("%s: %d point", name1, win1);
		if(win1!=1) printf("s");
		printf("\n");
		printf("%s: %d point", name2, win2);
		if(win2!=1) printf("s");
		printf("\n");
		if(win1>win2) printf("WINNER: %s\n\n", name1);
		if(win1<win2) printf("WINNER: %s\n\n", name2);
		if(win1==win2) printf("TIED GAME\n\n");
	}
	return 0;
}

