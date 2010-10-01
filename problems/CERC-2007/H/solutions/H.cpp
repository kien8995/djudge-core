/*
 * Sample solution to the Hexagon problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define MAXN 100000

int vcnt;
int neig[MAXN+2][6];
int neigcnt[MAXN+2];
int inset[MAXN+2];
int conn[MAXN+2][18];

int h;

int vis[MAXN+2];

int get_inset()
{
	char pomc;
	pomc='\0';
	scanf(" %c ",&pomc);
	if(pomc!='.')
	{
		assert(pomc>='A' && pomc<='D');
		return (pomc-'A');
	}
	else return -1;
}

int ciphsum(int b)
{
	return (b&1) + (b&2)/2 + (b&4)/4 + (b&8)/8;
}

void dij(int req)
{
	int i,j,k,l,tmp,mindist;
	int v;
	int cur;

	for(i=0;i<16;i++) for(j=i;j<16;j++) if((j&i)==0)
		for(k=j;k<16;k++) if((k&i)==0 && (k&j)==0)
			for(l=k;l<16;l++) if((l&i)==0 && (l&j)==0 && (l&k)==0)
				if((i|j|k|l) == req)
				{
					for(v=0;v<vcnt;v++)
					{
						if(conn[v][i]<0 || conn[v][j]<0 || conn[v][k]<0 || conn[v][l]<0) continue;
						tmp = conn[v][i] + conn[v][j] + conn[v][k] + conn[v][l];
						tmp++;
						if(i!=0) tmp--;
						if(j!=0) tmp--;
						if(k!=0) tmp--;
						if(l!=0) tmp--;
						assert(tmp>=0);
						if(conn[v][req]<0 || conn[v][req]>tmp) conn[v][req] = tmp;
					}
				}

	for(i=0;i<vcnt;i++) vis[i] = 0;
	
	while(1)
	{
		cur=-1;
		for(i=0;i<vcnt;i++) if(!vis[i] && conn[i][req]>=0)
		{
			if(cur==-1 || conn[i][req]<conn[cur][req]) cur=i;
		}
		if(cur==-1) break;
		vis[cur]=1;
		for(j=0;j<neigcnt[cur];j++) 
		{
			tmp = neig[cur][j];
			if(conn[tmp][req]<0 || conn[tmp][req]>conn[cur][req]+1) conn[tmp][req] = conn[cur][req]+1;
		}
	}
	
	for(i=0;i<vcnt;i++) assert(conn[i][req]>=0);
	
	for(i=0;i<4;i++)
	{
		mindist = -1;
		for(j=0;j<vcnt;j++) if(inset[j]==i)	
		{
			if(mindist==-1 || conn[j][req]<mindist) mindist = conn[j][req];
		}
		for(j=0;j<vcnt;j++) if(inset[j]==i)	
		{
			conn[j][req] = mindist;
		}
	}
}

int main(void)
{
	int i,j,tmp;
	int res;
	
	while(1)
	{
		scanf("%d", &h);
		if(h==0) break;
		assert(h>=2 && h<=100);
		vcnt = 0;
		for(i=0;i<h-1;i++)
		{
			for(j=0;j<h+i;j++)
			{
				inset[vcnt]=get_inset();
				neigcnt[vcnt]=0;
				if(i>0 && j>0)     neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i);
				if(i>0 && j<h+i-1) neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i-1);
				if(j>0)            neig[vcnt][neigcnt[vcnt]++]=vcnt-1;
				if(j<h+i-1) 		   neig[vcnt][neigcnt[vcnt]++]=vcnt+1;
				neig[vcnt][neigcnt[vcnt]++]=vcnt+h+i;
				neig[vcnt][neigcnt[vcnt]++]=vcnt+h+i+1;
				vcnt++;
			}
		}
		i=h-1;
		for(j=0;j<h+i;j++)
		{
			inset[vcnt]=get_inset();
			neigcnt[vcnt]=0;
			if(j>0)     neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i);
			if(j<h+i-1) neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i-1);
			if(j>0)     neig[vcnt][neigcnt[vcnt]++]=vcnt-1;
			if(j<h+i-1) neig[vcnt][neigcnt[vcnt]++]=vcnt+1;
			if(j>0)     neig[vcnt][neigcnt[vcnt]++]=vcnt+h+i-1;
			if(j<h+i-1) neig[vcnt][neigcnt[vcnt]++]=vcnt+h+i;
			vcnt++;
		}
		for(i=h-2;i>=0;i--)
		{
			for(j=0;j<h+i;j++)
			{
				inset[vcnt]=get_inset();
				neigcnt[vcnt]=0;
				neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i+1);
				neig[vcnt][neigcnt[vcnt]++]=vcnt-(h+i);
				if(j>0)            neig[vcnt][neigcnt[vcnt]++]=vcnt-1;
				if(j<h+i-1) 		   neig[vcnt][neigcnt[vcnt]++]=vcnt+1;
				if(i>0 && j>0)     neig[vcnt][neigcnt[vcnt]++]=vcnt+(h+i-1);
				if(i>0 && j<h+i-1) neig[vcnt][neigcnt[vcnt]++]=vcnt+(h+i);
				vcnt++;
			}
		}
/*		for(i=0;i<vcnt;i++) 
		{
			printf("i==%d, neighbors: ", i);
			for(j=0;j<neigcnt[i];j++) printf("%d ",neig[i][j]);
			printf("\n");
		}*/
		for(i=0;i<vcnt;i++) for(j=0;j<16;j++) conn[i][j]=-1;
		for(i=0;i<vcnt;i++) conn[i][0]=0;
		for(i=0;i<vcnt;i++) if(inset[i]!=-1) conn[i][1<<inset[i]]=0;

		for(i=1;i<=4;i++)
		{
			for(j=0;j<16;j++) if(ciphsum(j)==i)
			{
				dij(j);
			}	
		}
/*		for(i=0;i<vcnt;i++)
		{
			printf("i==%d\n", i);
			for(j=0;j<16;j++) printf("%3d",conn[i][j]);
			printf("\n");
		}*/
		res = -1;
		for(i=0;i<vcnt;i++)
		{
			if(res==-1 || conn[i][15]<res) res = conn[i][15];
		}
		printf("You have to buy %d parcels.\n", res);	
	}
	return 0;
}

