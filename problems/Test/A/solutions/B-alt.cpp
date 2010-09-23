//* Problem  : 2960 - VECI
//* Contest  : acm.tju.edu.cn
//* Origin   : 
//* Type     : Solution
//* Date     : 2008.04.29
//* Author   : alt
//* Language : C++
//* Compiler : Microsoft Visual C++ 6.0
//* Algoritm : 

#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>

#include <algorithm>
#include <vector>
#include <queue>
#include <string>
#include <iostream>
using namespace std;
#define int64 long long

int n, cnt[10], t2[10], s;

int main()
{
#ifdef _DEBUG
	freopen("1366", "r", stdin);
#endif
	scanf("%d", &n); int t = n;
	while (t)
	{
		cnt[t%10]++; t /= 10; s++;
	}
	n++;
	for (; n <= 999999; n++)
	{
		t2[0] = t2[1] = t2[2] = t2[3] = t2[4] = t2[5] = t2[6] = t2[7] = t2[8] = t2[9] = 0;
		int t = n, c = 0;
		while (t)
		{
			t2[t%10]++; t /= 10; c++;
		}
		if (c != s)
		{
			n = 1000000; break;
		}
		if (t2[0] != cnt[0]) continue;
		if (t2[1] != cnt[1]) continue;
		if (t2[2] != cnt[2]) continue;
		if (t2[3] != cnt[3]) continue;
		if (t2[4] != cnt[4]) continue;
		if (t2[5] != cnt[5]) continue;
		if (t2[6] != cnt[6]) continue;
		if (t2[7] != cnt[7]) continue;
		if (t2[8] != cnt[8]) continue;
		if (t2[9] != cnt[9]) continue;
		break;
	}
	if (n == 1000000) n = 0;
	printf("%d\n", n);
	return 0;
}
