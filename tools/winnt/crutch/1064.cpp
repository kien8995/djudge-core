#include <stdio.h>
#include <windows.h>
#include <math.h>
#include <string.h>

#include <string>

using namespace std;

int main(int argc, char *argv[])
{
	string s;
	for (int i = 0; argv[2][i]; i++)
		if (argv[2][i] == '/')
			argv[2][i] = '\\';
	for (int i = 2; i < argc; i++)
	{
	//	puts(argv[i]);
		s += argv[i];
		s += " ";
	}
	SetCurrentDirectory(argv[1]);
	int ret_val = system(s.c_str());
	return ret_val;
}

