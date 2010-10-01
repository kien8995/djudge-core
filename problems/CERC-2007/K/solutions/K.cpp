/*
 * Sample solution to the Keys problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define MAXSIZE 100

int r,c;

int vis[MAXSIZE*MAXSIZE*18];
int q[MAXSIZE*MAXSIZE*18], qsteps[MAXSIZE*MAXSIZE*18];
int type[MAXSIZE*MAXSIZE];
int qb,qe;
int finished;

void go_try(int i, int j, int k, int s)
{
	int pos, tmp;
	pos = i*c+j;

	if(type[pos]==1) { finished=1; return; }
	if(type[pos]==-1) return;
	if(type[pos]>=10 && type[pos]<20)
	{
		tmp = type[pos]-10;
		if(((1<<tmp)&k)==0) return;
	}
	tmp = pos+r*c*k;
	if(vis[tmp]) return;
	q[qe] = tmp;
	qsteps[qe++] = s+1;
	vis[tmp] = 1;
	return;
}

int main(void)
{
	int i,j,tmp;
	int pos, keys, posr, posc, steps;
	char tmpc;

	while(1)
	{
		r=c=-1;
		scanf("%d %d ", &r, &c);
		if(r==0) { assert(c==0); break; }
		if(c==0) { assert(r==0); break; }
		assert(r>0 && c>0 && r<=100 && c<=100);
		tmp = -1;
		for(i=0;i<r;i++) for(j=0;j<c;j++)
		{
			scanf(" ");
			tmpc = getchar();
			pos = i*c+j;
			switch(tmpc)
			{
				case '.': type[pos] = 0; break;
				case '#': type[pos] = -1; break;
				case '*': type[pos] = 0; assert(tmp==-1); tmp = pos; break;
				case 'X': type[pos] = 1; break;
				case 'B': type[pos] = 10; break;
				case 'Y': type[pos] = 11; break;
				case 'R': type[pos] = 12; break;
				case 'G': type[pos] = 13; break;
				case 'b': type[pos] = 20; break;
				case 'y': type[pos] = 21; break;
				case 'r': type[pos] = 22; break;
				case 'g': type[pos] = 23; break;
				default: assert(0);
			}
		}
		for(i=0;i<r*c*16;i++) vis[i]=0;
		vis[tmp]=1; q[0]=tmp; qsteps[0]=0; qb=0; qe=1;
		finished = 0;
		while(qe!=qb)
		{
			pos = q[qb]%(r*c); posr = pos/c; posc = pos%c;
			keys = q[qb]/(r*c);
			steps = qsteps[qb];
			if(type[pos]>=20 && type[pos]<30) keys|=(1<<(type[pos]-20));
			if(posr>0) go_try(posr-1,posc,keys,steps);
			if(posr<r-1) go_try(posr+1,posc,keys,steps);
			if(posc>0) go_try(posr,posc-1,keys,steps);
			if(posc<c-1) go_try(posr,posc+1,keys,steps);
			if(finished) { printf("Escape possible in %d steps.\n", steps+1); break; }
			qb++;
		}
		if(qe==qb) printf("The poor student is trapped!\n");
	}
	return 0;
}
