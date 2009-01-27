import java.math.BigInteger;
import java.util.Collection;
import java.util.Scanner;
import java.util.TreeSet;


public class Test
{
	static BigInteger Mod = new BigInteger("100000000000000000000");
	static BigInteger TWO = new BigInteger("2");
	
	public static boolean check(BigInteger num, int k)
	{
		String s = num.toString();
		if (s.length() < k) return false;
		for (int i = s.length() - 1; i >= s.length() - k; i--)
			if (s.charAt(i) != '1' && s.charAt(i) != '2')
				return false;
		return true;
	}
	
	static BigInteger power(BigInteger a, long n)
	{
		BigInteger b = BigInteger.ONE;
		while (n > 0)
		{
			if (n % 2 == 0)
			{
				n /= 2;
				a = a.multiply(a);
				a = a.mod(Mod);
			}
			else
			{
				n--;
				b = b.multiply(a);
				b = b.mod(Mod);
			}
		}
		return b;
	}
	
	public static long expand(long pwr, int n)
	{
		for (int i = 0; i < p25.length; i++)
		{
			long power = pwr + p25[i];
			BigInteger t = power(TWO, power);
			if (check(t, n)) return power;
			
		}
		return 0;
	}
	
	public static Long p25[];
	
	public static void main(String[] args)
	{
		Collection<Long> ttt = new TreeSet<Long>();
		ttt.add(new Long(0));
		BigInteger mmax = new BigInteger("" + Long.MAX_VALUE);
		for (long p2 = 1; p2 < Long.MAX_VALUE / 2; p2 *= 2)
			for (long p5 = 1; p5 < Long.MAX_VALUE / 5; p5 *= 5)
			{
				BigInteger t2 = new BigInteger("" + p2);
				BigInteger t5 = new BigInteger("" + p5);
				BigInteger tt = t2.multiply(t5);
				if (mmax.compareTo(tt) > 0)
				{
					ttt.add(new Long(tt.toString()));
				}
			}
		
		p25 = ttt.toArray(new Long[0]);

		long res[] = new long[32];
		res[1] = 1;
		long power = 1;
		int n = 1;
		for (n = 2; n <= 20; n++)
		{
			power = expand(power, n);
			res[n] = power;
			//System.out.println("" + n + " = " + power);
			//System.out.println("r[" + n + "]=" + power+"LL;");	
		}
		
		Scanner sc = new Scanner(System.in);
		long nt = sc.nextLong();
		for (int it = 1; it <= nt; it++)
		{
			int nn = sc.nextInt();
			System.out.println("" + it + " " + nn + " " + res[nn]);
		}
		
	}
}
