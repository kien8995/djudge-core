import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Uzh2010_F
{
	Scanner in = new Scanner(System.in);
	PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

	class Point implements Comparable<Point>
	{
		long x, y;
		
		public Point(long x, long y)
		{
			long g = gcd(abs(x), abs(y));
			this.x = x / g;
			this.y = y / g;
		}

		@Override
		public int compareTo(Point p)
		{
			if (p.x != x)
				return x < p.x ? -1 : 1;
			return y < p.y ? -1 : (y > p.y ? 1 : 0);
		}
	}
	
	long abs(long n)
	{
		return n > 0 ? n : -n;
	}
	
	long gcd(long a, long b)
	{
		while (a != 0 && b != 0)
			if (a > b)
				a %= b;
			else
				b %= a;
		return a + b;
	}
	
	int dc[] = new int[10];
	
	void run() throws Exception
	{
		// IO
		String s = in.next();
		assert !in.hasNext();
		assert s.charAt(0) != '0';
		int n = Integer.parseInt(s);
		assert n >= 1 && n <= 999999;
		// sol
		int res = 0;
		int tn = n;
		// digits of n
		while (tn > 0)
		{
			dc[tn%10]++;
			tn /= 10;
		}
		for (int i = n + 1; i <= 999999; i++)
		{
			int[] tc = new int[10];
			int ti = i;
			int f = 1;
			while (ti > 0)
			{
				int d = ti % 10;
				tc[d]++;
				if (tc[d] > dc[d])
				{
					f = 0; break;
				}
				ti /= 10;
			}
			for (int j = 0; j < 10; j++)
				if (tc[j] != dc[j])
				{
					f = 0;
					break;
				}
			if (f != 0)
			{
				res = i;
				break;
			}
		}
		out.println(res);
		in.close();
		out.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		new Uzh2010_F().run();
	}

}

