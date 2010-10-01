// Author: Helmut Sedding
#include <cstdio>

#define D 0
#define setx {if(D) printf("%d=%d ",x,y);fflush(stdin);P[x]=y;x=y;}


int P[1000001];

int main(){
//	freopen("cyclic.in","r",stdin);
	while(1){
		int n;
		scanf("%d",&n);
		if(!n) break;

		if(n%2==1){
			int x=(n+1)/2,y;
			int h=x;
			
			while(1){
			if(x==n){
				y=h;
				setx;
				break;
			}

			y=x+1;
			setx;

			y=h-(x-h);
			setx;

			if(x==1){
				y=h;
				setx;
				break;
			}

			y=x-1;
			setx;

			y=h+(h-x);
			setx;

		if(D) printf("\n");
			}
		}
		if(n%2==0 && (n/2)%2==0){
		if(D) printf("asdh");
			int x=n/2+1,y;
			int h=x;
			
			while(1){

			y=x+1;
			setx;

			y=h-(x-h);
			setx;

			y=x-1;
			setx;

			if(x==1){
				y=h;
				setx;
				break;
			}

			y=h+(h-x);
			setx;

		if(D) printf("\n");
			}
		}


		if(n%2==0 && (n/2)%2>0){
			int x=n/2,y;
			int h=x;
			
			while(1){
			if(x==n){
				y=h;
				setx;
				break;
			}

			y=x+1;
			setx;

			if(x==n){
				y=h;
				setx;
				break;
				}

			y=h-(x-h);
			setx;

			if(x==1){
				y=h;
				setx;
				break;
			}

			y=x-1;
			setx;

			y=h+(h-x);
			setx;

		if(D) printf("\n");
			}
		}

		if(D) printf("\n");
		for(int i=1;i<=n;i++){
			printf("%d ",P[i]);
		}
		printf("\n");
			if(D) printf(" of %d \n",n);
	}
}
	





			
