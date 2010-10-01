/*
 * Sample solution to the Water problem.
 * Central Europe Regional Contest 2007.
 *
 * Zdenek Dvorak, 2007
 */

#include <stdio.h>
#include <stdlib.h>

#define WATER_DENSITY 1.0
#define GLASS_DENSITY 2.5
#define PRECISION 10000
#define HGT_PRECISION 0.00001
#define MAXL 1000
#define PI 3.14159265358979323846

static double both, toth;

struct expr
{
  char op;
  double val;
  struct expr *l, *r;
};

static struct expr *outw, *thck, *innw;

static char line[MAXL], *pos;

static char
peek (void)
{
  while (*pos && *pos == ' ')
    pos++;

  return *pos;
}

static char
next (void)
{
  char ch = peek ();

  if (ch)
    pos++;
  return ch;
}

static struct expr *
new_expr (char op, struct expr *l, struct expr *r)
{
  struct expr *ret = (expr*)malloc (sizeof (struct expr));

  ret->op = op;
  ret->val = 0;
  ret->l = l;
  ret->r = r;
  return ret;
}

static struct expr *
number (void)
{
  struct expr *ret = new_expr (0, NULL, NULL);
  double val = 0, wght = 0.1;
  char ch;
  int ddig = 0;

  while (1)
    {
      ch = peek ();

      if ('0' <= ch && ch <= '9')
	{
	  if (ddig)
	    {
	      val += (ch - '0') * wght;
	      wght *= 0.1;
	    }
	  else
	    val = 10 * val + (ch - '0');
	}
      else if (ch == '.')
	ddig = 1;
      else
	break;

      next ();
    }

  ret->val = val;
  return ret;
}

static struct expr *parse (void);

static struct expr *
parse_op (void)
{
  int negate = 0;
  struct expr *ret;
  char token;

  while (1)
    {
      token = peek ();

      if (token == '+')
	{
	  next ();
	  continue;
	}
      if (token == '-')
	{
	  negate = !negate;
	  next ();
	  continue;
	}
      if (token == '(')
	{
	  next ();
	  ret = parse ();
	  break;
	}
      if (token == 'x')
	{
	  next ();
	  ret = new_expr ('x', NULL, NULL);
	  break;
	}

      ret = number ();
      if (negate)
	ret->val = -ret->val;
      return ret;
    }

  if (negate)
    ret = new_expr ('~', ret, NULL);

  return ret;
}

static struct expr *
parse_mul (void)
{
  struct expr *l, *r;
  char token;

  l = parse_op ();

  while (1)
    {
      token = peek ();

      if (token != '*'
	  && token != '/')
	return l;
      next ();

      r = parse_op ();
      l = new_expr (token, l, r);
    }
}

static struct expr *
parse (void)
{
  struct expr *l, *r;
  char token;

  l = parse_mul ();

  while (1)
    {
      token = next ();

      if (token != '+'
	  && token != '-')
	return l;

      r = parse_mul ();
      l = new_expr (token, l, r);
    }
}

static struct expr *
parse_line (void)
{
  pos = line;
  return parse ();
}

static void
free_expr (struct expr *e)
{
  if (!e)
    return;

  free_expr (e->l);
  free_expr (e->r);
  free (e);
}

static double
evaluate (struct expr *f, double x)
{
  double tmp;

  switch (f->op)
    {
    case 0:
      return f->val;

    case '+':
      return evaluate (f->l, x) + evaluate (f->r, x);
    case '-':
      return evaluate (f->l, x) - evaluate (f->r, x);
    case '*':
      return evaluate (f->l, x) * evaluate (f->r, x);
    case '/':
      return evaluate (f->l, x) / evaluate (f->r, x);
    case 'x':
      return x;
    case '~':
      return -evaluate (f->l, x);
    case '2':
      tmp = evaluate (f->l, x);
      return tmp * tmp;

    default:
      printf ("Error!\n");
      return 0;
    }
}

static double
integrate (struct expr *f, double a, double b, int precision)
{
  int n = 2 * precision, i;
  double delta = (b-a) / n, x;
  double val = evaluate (f, a) + evaluate (f, b);

  for (i = 1; i < precision; i++)
    {
      x = a + (2 * i - 1) * delta;
      val += 4 * evaluate (f, x) + 2 * evaluate (f, x + delta);
    }
  val += 4 * evaluate (f, b - delta);

  return delta * val / 3;
}

static struct expr *
copy_expr (struct expr *e)
{
  struct expr *c;

  if (!e)
    return NULL;

  c = (expr*)malloc (sizeof (struct expr));

  c->op = e->op;
  c->val = e->val;

  c->l = copy_expr (e->l);
  c->r = copy_expr (e->r);

  return c;
}

static double
mass_center (struct expr *f, double a, double b, double *wt)
{
  struct expr x = {'x', 0, NULL, NULL}, fx = {'*', 0, &x, f};

  *wt = integrate (f, a, b, PRECISION);

  return integrate (&fx, a, b, PRECISION) / *wt;
}

static double
combined_mass (double t0, double w0, struct expr *inn_square, double a, double x)
{
  double t, w;

  t = mass_center (inn_square, a, x, &w);
  w *= PI * WATER_DENSITY;

  return (t0 * w0 + t * w) / (w0 + w);
}

static double
minimize_mass (double t0, double w0, struct expr *inn_square, double a, double b)
{
  double t, m, aorig = a;

  if (t0 < a)
    return a;

  t = combined_mass (t0, w0, inn_square, a, b);
  if (t > b)
    return b;

  while (b - a > HGT_PRECISION)
    {
      m = (a+b)/2;

      t = combined_mass (t0, w0, inn_square, aorig, m);

      if (m < t)
	a = m;
      else
	b = m;
    }

  return (a+b)/2;
}

int
main (void)
{
  double full_wght, air_wght, glass_wght;
  double full_mass, air_mass, glass_mass, water_hgt;
  struct expr *inn_square, *out_square;

  while (1)
    {
      gets (line);
      sscanf (line, "%lf%lf", &toth, &both);
      if (toth == 0)
	return 0;

      gets (line);
      outw = parse_line ();
      gets (line);
      thck = parse_line ();
      innw = new_expr ('-', copy_expr (outw), copy_expr (thck));

      inn_square = new_expr ('2', copy_expr (innw), NULL);
      out_square = new_expr ('2', copy_expr (outw), NULL);

      full_mass = mass_center (out_square, 0, toth, &full_wght);
      air_mass = mass_center (inn_square, both, toth, &air_wght);
      full_wght *= PI * GLASS_DENSITY;
      air_wght *= PI * GLASS_DENSITY;
      glass_wght = full_wght - air_wght;

      glass_mass = (full_mass * full_wght - air_mass * air_wght) / glass_wght;

      water_hgt = minimize_mass (glass_mass, glass_wght, inn_square, both, toth);

      /* printf ("Vaha skla %lf, teziste skla %lf, vyska vody %lf\n", glass_wght, glass_mass, water_hgt); */

      printf ("Pour %.3lf litres / %.3lf cm of water.\n",
	      PI * WATER_DENSITY * integrate (inn_square, both, water_hgt, PRECISION)/1000,
	      water_hgt - both);

      free_expr (innw);
      free_expr (thck);
      free_expr (outw);
      free_expr (inn_square);
      free_expr (out_square);
    }
  return 0;
}

