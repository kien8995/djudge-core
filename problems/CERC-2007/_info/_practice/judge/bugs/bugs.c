#include <stdio.h>
#include <string.h>

char line[128];

int main(void)
{
  char * ptr, * hlpptr1, * hlpptr2;
  int ch;

  line[0] = line[1] = ' ';
  for (;;)
  {
    while (1)
    {
      ptr = line + 2;
      while ((ch = getchar()) != '\n') {
		  if (ch == EOF) return 0;
		  *ptr++ = ch;
	  }
      *ptr = '\0';
      ptr = line + 2;
      while ((ptr = strstr(ptr, "BUG")) != NULL)
      {
        hlpptr1 = ptr;
        hlpptr2 = ptr + 3;
        while (*hlpptr2 != '\0')
          *hlpptr1++ = *hlpptr2++;
        *hlpptr1 = '\0';
        ptr -= 2;
      }
      printf("%s\n", line + 2);
    }
    printf("\n");
  }
  return 0;
}
