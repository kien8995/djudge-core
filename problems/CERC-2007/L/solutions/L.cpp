/*
 * Sample solution to the Logic problem.
 * Central Europe Regional Contest 2007.
 *
 * Zdenek Dvorak, 2007
 */

#include <stdio.h>

#define N 500
#define HIDBASE 1000
#define MAX_HRADEL (N * N / 9)

#define D(X) do {} while (0)

static int obvod[N+2][N+2];
static int n, m;
static char values[26];
static int dirs[4][2] = {{1,0},{0,-1},{-1,0},{0,1}};
static int rev[4] = {2,3,0,1};
static char dds[4] = {'|','-','|','-'};

static struct generator
{
  int x, y, dir;
  char value;
  struct generator *next;
} generators[N * N];

static int n_generators;
static struct generator *gen_queue;

static struct hradlo
{
  char op, value;
  int n_entries;
  struct generator *exits;
  struct hradlo *next;
} hradla[MAX_HRADEL];

static int all_hradel;
static struct hradlo *hradla_queue;

static void
activate (struct hradlo *h)
{
  D(printf ("Activated gate %d\n", (int) (h - hradla)));
  h->next = hradla_queue;
  hradla_queue = h;
}

static void
mark_hradlo (int y, int x)
{
  int hno = all_hradel++;
  int my, mx, ax, ay;

  hradla[hno].n_entries = 0;
  hradla[hno].exits = NULL;
  for (my = y + 1; obvod[my][x + 1] != '#'; my++)
    continue;
  for (mx = x + 1; obvod[y+1][mx] != '#'; mx++)
    continue;

  for (ay = y; ay <= my; ay++)
    if (obvod[ay][x-1] == '=')
      hradla[hno].n_entries++;

  for (ay = y; ay <= my; ay++)
    for (ax = x; ax <= mx; ax++)
      {
	if (obvod[ay][ax] != '#'
	    && obvod[ay][ax] != ' ')
	  hradla[hno].op = obvod[ay][ax];
	obvod[ay][ax] = hno + HIDBASE;
      }

  hradla[hno].value = hradla[hno].op == '&';
  if (!hradla[hno].n_entries)
    activate (&hradla[hno]);
}

static void
find_hradla (void)
{
  int x, y;

  for (y = 1; y < n; y++)
    for (x = 1; x < m; x++)
      if (obvod[y][x] == '#')
	mark_hradlo (y, x);
}

static void
mark_generator (int y, int x)
{
  int gno = n_generators++, hno;
  struct generator *g = &generators[gno];

  g->x = x;
  g->y = y;
  g->dir = 3;

  D(printf ("Marking generator at %d, %d\n", x,y));
  if (obvod[y][x-1] == '0' || obvod[y][x-1] == '1')
    {
      g->value = obvod[y][x-1] - '0';
      g->next = gen_queue;
      gen_queue = g;
      D(printf ("  value %d\n", g->value));
    }
  else
    {
      if (obvod[y][x-1] == 'o')
	{
	  x--;
	  g->value = 1;
	}
      else
	g->value = 0;

      hno = obvod[y][x-1] - HIDBASE;
      g->next = hradla[hno].exits;
      hradla[hno].exits = g;
    }
}

static void
find_generators (void)
{
  int y, x;

  for (y = 1; y < n; y++)
    for (x = 1; x < m; x++)
      if (obvod[y][x] == '=' &&
	  (obvod[y][x-1] == '0' || obvod[y][x-1] == '1' || obvod[y][x-1] >= HIDBASE || obvod[y][x-1] == 'o'))
	mark_generator (y, x);
}

static void
trigger (struct hradlo *h)
{
  struct generator *exit, *last = NULL;

  D(printf ("Triggered gate %d\n", (int) (h - hradla)));

  for (exit = h->exits; exit; last = exit, exit = exit->next)
    {
      exit->value ^= h->value;
      D(printf ("  set value of exit %d to %d\n", (int) (exit - generators), exit->value));
    }

  if (last)
    {
      last->next = gen_queue;
      gen_queue = h->exits;
    }
  h->exits = NULL;
}

static void
add_input (struct hradlo *h, char value)
{
  D(printf ("Added value %d to gate %d\n", value, (int) (h - hradla)));

  if (h->op == '&')
    h->value &= value;
  else if (h->op == '1')
    h->value |= value;
  else
    h->value ^= value;

  h->n_entries--;
  if (h->n_entries == 0)
    activate (h);
}

static void
run (struct generator *g)
{
  int x = g->x, y = g->y, dir = g->dir, hno, d, nx, ny;
  struct generator *ng;

  D(printf ("Run (%d, %d) direction (%d, %d) value %d\n", x,y, dirs[dir][1],  dirs[dir][0], g->value));

  while (obvod[y][x] == dds[dir]
	 || obvod[y][x] == 'x'
	 || (obvod[y][x] == '=' && dir == 3))
    {
      if (obvod[y][x] != 'x')
	obvod[y][x] = 'v';
      y += dirs[dir][0];
      x += dirs[dir][1];
    }

  if (dir == 3 && (obvod[y][x] >= 'A' && obvod[y][x] <= 'Z'))
    values[obvod[y][x] - 'A'] = g->value;
  else if (dir == 3 && obvod[y][x] >= HIDBASE)
    {
      hno = obvod[y][x] - HIDBASE;
      add_input (&hradla[hno], g->value);
    }
  else if (obvod[y][x] == '+')
    {
      for (d = 0; d <= 3; d++)
	{
	  if (d == rev[dir])
	    continue;

	  ny = y + dirs[d][0];
	  nx = x + dirs[d][1];

	  if (obvod[ny][nx] != dds[d]
	      && obvod[ny][nx] != '+'
	      && obvod[ny][nx] != 'x'
	      && !(d == 3 && obvod[ny][nx] == '='))
	    continue;

	  ng = &generators[n_generators++];
	  ng->value = g->value;
	  ng->x = nx;
	  ng->y = ny;
	  ng->dir = d;
	  ng->next = gen_queue;
	  gen_queue = ng;
	}
    }
  obvod[y][x] = 'v';
}

static void
evaluate (void)
{
  struct generator *g;
  struct hradlo *h;

  n_generators = 0;
  all_hradel = 0;
  hradla_queue = NULL;
  gen_queue = 0;

  find_hradla ();
  find_generators ();

  while (gen_queue || hradla_queue)
    {
      h = hradla_queue;
      if (h)
	{
	  hradla_queue = hradla_queue->next;
	  trigger (h);
	}

      g = gen_queue;
      if (g)
	{
	  gen_queue = g->next;
	  run (g);
	}
    }
}

int
main (void)
{
  int i, j;
  char buf[N+1];

  while (1)
    {
      obvod[0][0] = 0;
      m = 0;
      for (n = 0; ; n++)
	{
	  gets (buf);
	  if (buf[0] == '*')
	    break;
	  for (i = 0; buf[i]; i++)
	    obvod[n + 1][i+1] = buf[i];
	  obvod[n+1][0] = ' ';
	  obvod[n+1][i+1] = 0;
	  if (i > m)
	    m = i;
	}

      if (!n)
	return 0;

      n++; m++;
      obvod[n][0] = 0;

      for (i = 0; i <= n; i++)
	{
	  for (j = 0; ; j++)
	    if (!obvod[i][j])
	      break;
	  for (; j <= m; j++)
	    obvod[i][j] = ' ';
	}

      for (i = 0; i < 26; i++)
	values[i] = -1;

      evaluate ();

      for (i = 0; i < 26; i++)
	if (values[i] >= 0)
	  printf ("%c=%d\n", i + 'A', values[i]);
      printf ("\n");
    }
}

