//* Problem  : C: Cyclic antimonotonic permutations
//* Origin   : Ulm-2009
//* Type     : Judge
//* Date     : 2008.08.07
//* Author   : alt
//* Language : C++

#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <string>
#include <iostream>
#include <set>
#include <map>
#include <vector>
#include <algorithm>
#include "testlib.h"

using namespace std;

int n;

int nt = 0;

vector<int> f, a;

int main(int argc, char *argv[])
{
	registerTestlibCmd(argc, argv);
	//quitf(_wa, "wa");
	ouf.close();

	while (n = inf.readInteger())
	{
		f.assign(n, 0);
		a.clear();
		++nt;
		for (int i = 0; i < n; i++)
		{
			int t = ans.readInteger() - 1;
			if (t < 0 || t >= n)
				quitf(_wa, "WA. %d test. Number out of range", nt);
			if (f[t])
				quitf(_wa, "WA. %d test. Duplicate number", nt);
			f[t] = 1;
			a.push_back(t);
		}
		for (int i = 1; i < n - 1; i++)
		{
			if (a[i-1] < a[i] && a[i] < a[i+1])
				quitf(_wa, "WA. %d test. Monotonic: %d, %d, %d", nt, a[i-1], a[i], a[i+1]);
			if (a[i-1] > a[i] && a[i] > a[i+1])
				quitf(_wa, "WA. %d test. Monotonic: %d, %d, %d", nt, a[i-1], a[i], a[i+1]);
		}
		int p = 0;
		f[p] = 0;
		while (a[p])
		{
			p = a[p];
			f[p] = 0;
		}
		for (int i = 0; i < n; i++)
			if (f[i])
				quitf(_wa, "WA. %d test. Number %d not in cycle", nt, p + 1);
	}
	quitf(_ok, "OK. %d tests", nt);
}