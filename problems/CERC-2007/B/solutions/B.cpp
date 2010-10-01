/*
 * Sample solution to the Billboard problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

#include <stdio.h>
#include <memory.h>

#define MAX  24
#define CH_ON  'X'
#define CH_OFF  '.'

int sizex, sizey;
int best;
typedef int map_t [MAX<<1];   /* [1..MAX], bits 1..MAX */


void togglebits (map_t* m, int x, int b)
{
	(*m)[x-1] ^= b;
	(*m)[x] ^= b ^ (b<<1) ^ (b>>1);
	(*m)[x] &= ~ ( 1 | (1<<(sizey+1)) );
	(*m)[x+1] ^= b;
}

void togglexy (map_t* m, int x, int y)
{
	togglebits (m, x, 1<<y);
}

void onesol (map_t* m)
{
	int i, j, cnt = 0;
	for ( i = 1;  i <= sizex;  ++i )
		for ( j = 1;  j <= sizey;  ++j )
			if ( (*m)[i] & (1<<j) )
				++cnt;
	if ( cnt < best )  best = cnt;
}


int readmap (map_t* m)
{
	int i, j;
	char row [MAX<<1];

	if ( scanf ("%d%d\n", &sizex, &sizey) != 2 )  return 0;
	if ( !sizex || !sizey )  return 0;

	for ( i = 1;  i <= sizex;  ++i )
	{
		scanf ("%s\n", row);
		(*m)[i] = 0;
		for ( j = 1;  j <= sizey;  ++j )
			if ( row[sizey-j] == CH_ON )
				(*m)[i] |= (1<<j);
	}
	
	return sizex;
}


int main (void)
{
	map_t m, morig, sol;
	int x, first;

	while ( readmap (&morig) )
	{
		best = sizex * sizey + 1;
		for ( first = 0;  first < (1<<(sizey+1));  first += 2 )
		{
			memcpy (&m, &morig, sizeof (m));
			togglebits (&m, 1, sol[1] = first);
			for ( x = 2;  x <= sizex;  ++x )
			{
				togglebits (&m, x, sol[x] = m[x-1]);
			}
			if ( m[sizex] == 0 )
				onesol (&sol);
		}
		if ( best < sizex * sizey + 1 )
			printf ("You have to tap %d tiles.\n", best);
		else
			printf ("Damaged billboard.\n");
	}
	return 0;
}

