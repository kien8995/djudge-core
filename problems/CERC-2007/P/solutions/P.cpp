/*
 * Sample solution to the Polygon problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define MAXN 1000

int n;

typedef struct Point
{
	int x,y,orig,even;
	int wheresx, wheresy;
} Point;

Point pt[MAXN+2];
Point ptsx[MAXN+2];
Point ptsy[MAXN+2];

char dirc[MAXN+2];
int dirccnt;

int possx, possy;

int ptsx_cmp(const void *e1, const void *e2)
{
	Point *p1 = (Point *)e1;
	Point *p2 = (Point *)e2;
	if(p1->x!=p2->x) return p1->x - p2->x;
	else return p1->y - p2->y;
}

int ptsy_cmp(const void *e1, const void *e2)
{
	Point *p1 = (Point *)e1;
	Point *p2 = (Point *)e2;
	if(p1->y!=p2->y) return p1->y - p2->y;
	else return p1->x - p2->x;
}

void go_next(int dir)
{
	if(dir==0) 
	{
		dirc[dirccnt++] = 'N';
		possx = possx+1;
		possy = pt[ptsx[possx].orig].wheresy;
	}
	if(dir==1) 
	{
		dirc[dirccnt++] = 'E';
		possy = possy+1;
		possx = pt[ptsy[possy].orig].wheresx;
	}
	if(dir==2) 
	{
		dirc[dirccnt++] = 'S';
		possx = possx-1;
		possy = pt[ptsx[possx].orig].wheresy;
	}
	if(dir==3) 
	{
		dirc[dirccnt++] = 'W';
		possy = possy-1;
		possx = pt[ptsy[possy].orig].wheresx;
	}
}
	

int main(void)
{
	int i,j,tmp,begprint;
	int lastdir;

	while(1)
	{
		n=-1;
		scanf(" %d ", &n);
		if(n==0) break;
		assert(n<=MAXN && n>0);
		for(i=0;i<n;i++)
		{
			scanf("%d %d ", &(pt[i].x), &(pt[i].y));
			assert(abs(pt[i].x<=10000)); assert(abs(pt[i].y<=10000));
			pt[i].orig = i;
			ptsx[i].x = ptsy[i].x = pt[i].x;
			ptsx[i].y = ptsy[i].y = pt[i].y;
			ptsx[i].orig = ptsy[i].orig = pt[i].orig;
		}
		qsort(ptsx, n, sizeof(ptsx[0]), ptsx_cmp);
		qsort(ptsy, n, sizeof(ptsy[0]), ptsy_cmp);
		ptsx[0].even = 1;
		for(i=1;i<n;i++)
		{
			if(ptsx[i-1].x == ptsx[i].x) ptsx[i].even = 1-ptsx[i-1].even;
			else ptsx[i].even = 1;
		}
		ptsy[0].even = 1;
		for(i=1;i<n;i++)
		{
			if(ptsy[i-1].y == ptsy[i].y) ptsy[i].even = 1-ptsy[i-1].even;
			else ptsy[i].even = 1;
		}

/* for(i=0;i<n;i++)
printf("%d %d  %d\n", ptsx[i].x, ptsx[i].y, ptsx[i].even);
printf("\n");
for(i=0;i<n;i++)
printf("%d %d  %d\n", ptsy[i].x, ptsy[i].y, ptsy[i].even);
printf("\n"); */

		for(i=0;i<n;i++) 
		{
			pt[ptsx[i].orig].wheresx = i;
			pt[ptsy[i].orig].wheresy = i;
		}
		
	
/*		if( ptsx[possx].even &&  ptsy[possy].even) lastdir = 0;
		else if( ptsx[possx].even && !ptsy[possy].even) lastdir = 3;
		else if(!ptsx[possx].even &&  ptsy[possy].even) lastdir = 1;
		else lastdir = 2;*/
		possx = 0;
		possy = pt[ptsx[0].orig].wheresy;
		dirccnt = 0;	
		lastdir = 0;
		begprint = -1;
		if(ptsx[possx].orig==0) begprint = dirccnt;
		go_next(lastdir);
		
		while(possx != 0)
		{
			if(ptsx[possx].orig==0) begprint = dirccnt;
			if(lastdir&1) 
			{
				if(ptsx[possx].even) lastdir = 0;
				else lastdir = 2;
			}
			else
			{
				if(ptsy[possy].even) lastdir = 1;
				else lastdir = 3;
			}
			go_next(lastdir);
		}
		assert(begprint!=-1);
		assert(dirccnt==n);
		for(i=begprint; i<dirccnt; i++) printf("%c", dirc[i]);
		for(i=0; i<begprint; i++) printf("%c", dirc[i]);
		putchar('\n');
	}
	return 0;
}
