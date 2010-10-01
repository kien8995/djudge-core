/*
 * Sample solution to the Sort problem.
 * Central Europe Regional Contest 2007.
 *
 * Zdenek Dvorak, 2007
 */

#include <stdio.h>
#include <stdlib.h>

#define MAXN 200000
static int n, input[MAXN], ipermut[MAXN], permut[MAXN];

struct node
{
  int reverse, size, present;
  struct node *l, *r, *f;
};

static struct node path_nodes[MAXN];


static void
dump_tree (struct node *t, int reverse)
{
  struct node *l, *r;

  reverse ^= t->reverse;

  l = reverse ? t->r : t->l;
  r = reverse ? t->l : t->r;

  if (l)
    dump_tree (l, reverse);

  if (t->present)
    printf ("%d ", (int) (t - path_nodes));

  if (r)
    dump_tree (r, reverse);
}

static void
dump_path (void)
{
  int i;

  printf ("\n");
  for (i = 0; i < n; i++)
    if (!path_nodes[i].f)
      {
	printf ("--- ");
	dump_tree (&path_nodes[i], 0);
      }

  printf (" ---\n");
}

static void
clean_reverse (struct node *tree)
{
  struct node *t;

  if (!tree->reverse)
    return;

  t = tree->l; tree->l = tree->r; tree->r = t;
  if (tree->l)
    tree->l->reverse = !tree->l->reverse;
  if (tree->r)
    tree->r->reverse = !tree->r->reverse;
  tree->reverse = 0;
}

static void
fix_size (struct node *t)
{
  int lsize = t->l ? t->l->size : 0;
  int rsize = t->r ? t->r->size : 0;

  t->size = lsize + rsize + t->present;
}

static void
rotate (struct node *t, struct node *f)
{
  struct node **ftlink, **alink, *a;

  if (f->r == t)
    {
      ftlink = &f->r;
      alink = &t->l;
    }
  else
    {
      ftlink = &f->l;
      alink = &t->r;
    }

  t->f = f->f;
  if (f->f)
    {
      if (f->f->l == f)
	f->f->l = t;
      else
	f->f->r = t;
    }

  a = *alink;
  *ftlink = a;
  if (a)
    a->f = f;

  *alink = f;
  f->f = t;

  fix_size (f);
  fix_size (t);
}

static void
zigzig (struct node *t, struct node *f, struct node *gf)
{
  struct node **gfflink, **ftlink, **blink, **clink, *b, *c;
  if (gf->r == f)
    {
      gfflink = &gf->r;
      ftlink = &f->r;
      blink = &f->l;
      clink = &t->l;
    }
  else
    {
      gfflink = &gf->l;
      ftlink = &f->l;
      blink = &f->r;
      clink = &t->r;
    }

  b = *blink;
  c = *clink;

  t->f = gf->f;
  if (gf->f)
    {
      if (gf->f->l == gf)
	gf->f->l = t;
      else
	gf->f->r = t;
    }

  *clink = f;
  f->f = t;

  *blink = gf;
  gf->f = f;

  *gfflink = b;
  if (b)
    b->f = gf;

  *ftlink = c;
  if (c)
    c->f = f;

  fix_size (gf);
  fix_size (f);
  fix_size (t);
}

static void
zigzag (struct node *t, struct node *f, struct node *gf)
{
  struct node **gfflink, **ftlink, **blink, **clink, *b, *c;
  if (gf->l == f)
    {
      gfflink = &gf->l;
      ftlink = &f->r;
      blink = &t->l;
      clink = &t->r;
    }
  else
    {
      gfflink = &gf->r;
      ftlink = &f->l;
      blink = &t->r;
      clink = &t->l;
    }

  b = *blink;
  c = *clink;

  t->f = gf->f;
  if (gf->f)
    {
      if (gf->f->l == gf)
	gf->f->l = t;
      else
	gf->f->r = t;
    }

  *clink = gf;
  gf->f = t;

  *blink = f;
  f->f = t;

  *gfflink = c;
  if (c)
    c->f = gf;

  *ftlink = b;
  if (b)
    b->f = f;

  fix_size (gf);
  fix_size (f);
  fix_size (t);
}

static void
double_rotate (struct node *t, struct node *f, struct node *gf)
{
  if ((gf->r == f) == (f->r == t))
    zigzig (t, f, gf);
  else
    zigzag (t, f, gf);
}

static void
splay (struct node *t)
{
  struct node *f, *gf;

  while (t->f)
    {
      f = t->f;

      gf = f->f;
      if (!gf)
	{
	  clean_reverse (f);
	  clean_reverse (t);
	  rotate (t, f);
	  return;
	}
      clean_reverse (gf);
      clean_reverse (f);
      clean_reverse (t);

      double_rotate (t, f, gf);
    }

  clean_reverse (t);
}

static int
rotfirst_and_delete (struct node *t)
{
  int pos;

  splay (t);
  /*printf ("After splay: ");
  dump_path();
  printf ("\n");*/
  if (t->l)
    {
      t->l->reverse = !t->l->reverse;
      pos = t->l->size;
    }
  else
    pos = 0;

  t->present = 0;
  t->size--;

  return pos;
}

static int
cmp_input (const void *a, const void *b)
{
  int va = *(int *) a;
  int vb = *(int *) b;

  if (input[va] != input[vb])
    return input[va] - input[vb];

  return va - vb;
}

int
main (void)
{
  int i;

  while (1)
    {
      scanf ("%d", &n);
      if (!n)
	return 0;

      for (i = 0; i < n; i++)
	scanf ("%d", &input[i]);

      for (i = 0; i < n; i++)
	ipermut[i] = i;

      qsort (ipermut, n, sizeof (int), cmp_input);

      for (i = 0; i < n; i++)
	permut[ipermut[i]] = i;

/*      for (i = 0; i < n; i++)
	printf ("%d ", permut[i]);
      printf ("\n");*/
      for (i = 0; i < n; i++)
	{
	  path_nodes[permut[i]].reverse = 0;
	  path_nodes[permut[i]].size = i + 1;
	  path_nodes[permut[i]].present = 1;
	  path_nodes[permut[i]].l = i > 0 ? &path_nodes[permut[i-1]] : NULL;
	  path_nodes[permut[i]].r = NULL;
	  path_nodes[permut[i]].f = i < n - 1 ? &path_nodes[permut[i+1]] : NULL;
	}

      for (i = 0; i < n; i++)
	{
	  /*dump_path ();*/
	  printf ("%d%c", rotfirst_and_delete (&path_nodes[i]) + i + 1, i+1 < n ? ' ' : '\n');
	}
    }
}

