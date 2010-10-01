/*
 * Sample solution to the Numbers problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class numbers
{
	int radix, dec;
	String num;

	String convertTo() {
		int x;
		num = "";
		while (dec != 0) {
			if (dec > 0) {
				x = dec % radix;
				dec = - (dec / radix);
			} else {
				x = (-dec) % radix;
				if (x > 0) x = radix - x;
				dec = (-(dec - x)) / radix;
			}
			num = x + num;
		}
		return num.length() == 0 ? "0" : num;
	}

	int convertFrom() {
		int x = 0, r = 1;
		for (int i = num.length(); --i >= 0; ) {
			x += r * (num.charAt(i) - '0');
			r *= -radix;
		}
		return x;
	}

	void run() throws Exception {
		for (;;) {
			String s = nextToken();
			if (s.startsWith("to-")) {
				radix = Integer.parseInt(s.substring(3));
				dec = nextInt();
				System.out.println(convertTo());
			} else if (s.startsWith("from-")) {
				radix = Integer.parseInt(s.substring(5));
				num = nextToken();
				System.out.println(convertFrom());
			} else break;
		}
	}

	public static void main(String[] args) throws Exception {
		(new numbers()).run();
	}

	StringTokenizer st = new StringTokenizer("");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	String nextToken() throws Exception {
		while (!st.hasMoreTokens()) st = new StringTokenizer(input.readLine());
		return st.nextToken();
	}

	int nextInt() throws Exception {
		return Integer.parseInt(nextToken());
	}
}
