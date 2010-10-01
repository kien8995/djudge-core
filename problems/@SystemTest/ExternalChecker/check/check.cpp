//* Problem  : A - Addition Chains
//* Contest  : UzhNU Local
//* Origin   : Ulm-1997
//* Type     : Judge
//* Date     : 2008.05.31
//* Author   : alt
//* Language : C++
//* Compiler : Microsoft Visual C++ 6.0

#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <string>
#include <iostream>
#include <set>
#include <map>
#include "testlib.h"

using namespace std;

int n, a[100], res_j, res_u, it;


int main(int argc, char *argv[])
{
	registerTestlibCmd(argc, argv);
	///
	while (1)
	{
		string t = inf.readString();
		if (sscanf(t.c_str(), "%d", &n) != 1 || !n) break;
		it++; res_j = res_u = 0;
		while (!ouf.eoln())
		{
			t = ouf.readWord();
			res_j++;
		}
		ouf.skipChar();
		while (!ans.eoln())
			a[res_u++] = ans.readInt();
		ans.skipChar();
		if (res_j != res_u)
			quitf(_wa, "Test #%d. Different length of answer. Expected: %d, received: %d", it, res_j, res_u);
		for (int k = 1; k < res_u; k++)
		{
			int f = 0;
			for (int i = 0; i < k; i++)
				for (int j = 0; j < k; j++)
					if (a[i] + a[j] == a[k])
					{
						f = 1; break;
					}
			if (!f)
				quitf(_wa, "Test #%d. %d is not a sum of previous integers", it, a[k]);
		}
		if (a[res_u - 1] != n)
			quitf(_wa, "Test #%d. Last element (%d) != n (%d)", it, a[res_u-1], n);
	}
	quitf(_ok, "%d tests passed", it);
}