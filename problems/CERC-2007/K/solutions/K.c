/*
 * Sample solution to the Keys problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

#include <stdio.h>

#define MAX  (127+1)

char map[MAX][MAX];
int sizex, sizey;

int queue[(MAX*MAX)<<4], dist[(MAX*MAX)<<4], qb, qe;

int enc(int x, int y, int k)
{
	return ((x*sizey + y)<<4) | (k&0xF);
}

int getx(int c)
{
	return (c>>4) / sizey;
}

int gety(int c)
{
	return (c>>4) % sizey;
}

int color(char ch)
{
	switch (ch) {
		case 'y': return 1;
		case 'r': return 2;
		case 'g': return 4;
		case 'b': return 8;
		default: return 0;
	}
}

void enqueue(int x, int y, int k, int d)
{
	int c;
	if (x < 0 || x >= sizex || y < 0 || y >= sizey) return;
	if (map[x][y] == '#') return;
	if (islower(map[x][y])) k |= color(map[x][y]);
	/* printf ("enqueue: x=%d, y=%d, k=%d, d=%d\n", x, y, k&0xF, d); */
	if (isupper(map[x][y]) && map[x][y] != 'X') {
		if (!(k & color(tolower(map[x][y])))) return;
	}
	c = enc(x, y, k);
	if (dist[c] >= 0) return;
	dist[c] = d;
	queue[qe++] = c;
}


int main (void)
{
	int i, j;
	int x, y;
	int d;
	for ( ; ; ) {
		scanf ("%d%d\n", &sizex, &sizey);
		if (!sizex) break;
		for (i = 0; i < sizex; ++i) {
			scanf("%s\n", map[i]);
			for (j = 0; j < sizey; ++j) {
				if (map[i][j] == '*') {
					x=i;
					y=j;
					map[i][j] = '.';
				}
			}
		}
		memset(&dist, -1, sizeof(dist));
		qb = qe = 0;
		enqueue(x, y, 0, 0);
		while (qb < qe) {
			int c = queue[qb++];
			d = dist[c];
			x = getx(c);
			y = gety(c);
			if (map[x][y] == 'X') break;
			enqueue(x+1, y, c, ++d);
			enqueue(x-1, y, c, d);
			enqueue(x, y+1, c, d);
			enqueue(x, y-1, c, d);
		}
		if (map[x][y] == 'X')
			printf("Escape possible in %d steps.\n", d);
		else
			printf("The poor student is trapped!\n");
	}
	
	return 0;
}

