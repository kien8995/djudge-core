/*
 * Sample solution to the Numbers problem.
 * Central Europe Regional Contest 2007.
 *
 * Jan Cibulka, 2007
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#define MAXLEN 100
#define MAXN 1000000

int main(void)
{
	int i,j,tmp;
	int res,tmpmult,tmpmod;
	int baseneg, val;
	char valstr[MAXLEN+20];
	int valstrlen;
	char pomc;
	
	while(1)
	{
		do
		{
			pomc = getchar();
		} while(pomc==' ' || pomc=='\n');
		
//		assert(pomc=='f' || pomc=='t' || pomc == 'e');
		if(pomc=='e') break;
		if(pomc == 'f')
		{
			scanf("rom-%d %s ", &baseneg, valstr);
//			assert(baseneg>=2 && baseneg<=10);
/*printf("to-%d ", baseneg);*/
			baseneg *= -1;
/* printf("Read: %d %s\n", baseneg, valstr); */
			res = 0; tmpmult = 1;

			for(i=strlen(valstr)-1;valstr[i]>=0;i--)
			{
				tmp = valstr[i]-'0';
/* printf("Tmp == %d Res == %d\n", tmp, res); */
//				assert(tmp<-baseneg && tmp>=0);
				res+=tmp*tmpmult;
				tmpmult*=baseneg;
			}
//			assert(abs(res)<=MAXN);
			printf("%d\n", res);
		}
		if(pomc == 't')
		{
			scanf("o-%d %d ", &baseneg, &val);
//			assert(baseneg>=2 && baseneg<=10);
//			assert(abs(val)<=MAXN);
/*printf("from-%d ", baseneg);*/
			valstrlen = 0;
			while(val!=0)
			{
				tmp = val; if(tmp<0) tmp -= baseneg*tmp;
				tmpmod = tmp%baseneg;
/* printf("tmp %d tmpmod %d\n", tmp, tmpmod); */
				if(tmpmod>0)
				{
					if((valstrlen&1)==0)
					{
						valstr[valstrlen++] = '0' + tmpmod;
						val -= tmpmod;
					}
					else 
					{
						valstr[valstrlen++] = '0' + baseneg-tmpmod;
						val += baseneg-tmpmod;
					}
				}
				else valstr[valstrlen++] = '0';
				tmpmult*=-baseneg;
/* printf("valstr == %c, valmod %d\n", valstr[valstrlen-1], val%baseneg); */
				assert(val%baseneg == 0);
				val/=baseneg;
			}
			valstr[valstrlen] = '\0';
			for(i=valstrlen-1; i>=0; i--) printf("%c",valstr[i]);
			if (valstrlen<1) printf("0");
			printf("\n");
		}
	}
	return 0;
}

