/*
 * Sample solution to the Cell problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define PI 3.14159
#define EPS 0.0000001
#define MAXN 10000
#define SQR(a) ((a)*(a))

typedef struct Ang
{
	double val;
	int cid;
} Ang;	

int x[MAXN+2], y[MAXN+2];
int is_in[MAXN+2];
struct Ang angle[MAXN+2];
int ang_cnt;
int res;
int n,r;

int ang_cmp(const void *e1, const void *e2)
{
	double d1 = ((struct Ang *)e1)->val;
	double d2 = ((struct Ang *)e2)->val;
	if(d1>d2) return 1;
	if(d1<d2) return -1;
	return 0;
}


int main(void)
{
	int i,j,tmp,tmp2;
	double tmpdist, tmpang, tmpd;
	int res;
	
	while(1)
	{
	scanf("%d %d ", &n, &r);
	if(n==0) break;
	for(i=0;i<n;i++) scanf("%d %d ", &x[i], &y[i]);
	res = 1;

	for(i=0;i<n;i++)
	{
		ang_cnt = 0;
		for(j=0;j<n;j++) if(j!=i)
		{
			tmpd = SQR(x[j]-x[i]) + SQR(y[j]-y[i]);
			if(SQR(2*r)+EPS<tmpd) continue;
			tmpdist = sqrt(tmpd);
			if(x[i]==x[j]) 
			{
				if(y[i]==y[j]) continue;
				if(y[j]>y[i]) tmpang=PI/2; else tmpang=3*PI/2;
			}
			else if(y[i]==y[j]) 
			{
				if(x[j]>x[i]) tmpang=0; else tmpang=PI;
			}
			else tmpang = atan2(y[j]-y[i], x[j]-x[i]);
			tmpd = acos(tmpdist/(2*r));
			angle[ang_cnt].val = tmpang+tmpd+EPS/10;				
			angle[ang_cnt++].cid = j;
			angle[ang_cnt].val = tmpang-tmpd-EPS/10;
			angle[ang_cnt++].cid = j;
		}
		for(j=0;j<ang_cnt;j++)
		{
			while(angle[j].val<0) angle[j].val+=2*PI;
			while(angle[j].val>=2*PI) angle[j].val-=2*PI;
		}
		qsort(angle, ang_cnt, sizeof(angle[0]), ang_cmp);
		
		tmp = 0;
		for(j=0;j<n;j++)
		{
			if(SQR(x[j]-(x[i]+r)) + SQR(y[j]-y[i]) <= SQR(r)+EPS) 
			{
				is_in[j]=1;
				tmp++;
			} else is_in[j]=0;
		}
		if(tmp>res) res = tmp;
/*printf("%d      %d\n", i+1,tmp);*/
		for(j=0;j<ang_cnt;j++) 
		{
			tmp2 = angle[j].cid;
			if(is_in[tmp2]) tmp--; else tmp++;
			is_in[tmp2] = 1 - is_in[tmp2];
			if(tmp>res) res = tmp;
/*printf("%d %d  %8.4f  %d\n", i+1,angle[j].cid+1,angle[j].val*180/PI,tmp);*/
		}
	}
	printf("It is possible to cover %d points.\n",res);
	}
	return 0;
}

