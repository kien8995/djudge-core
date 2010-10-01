/*
 * Sample solution to the Logic problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class logic
{
	public static final int MAXX = 500;
	public static final int MAXY = 500;

	public static final char CH_EMPTY = ' ';
	public static final char CH_END = '*';
	public static final char CH_HORIZ = '-';
	public static final char CH_VERT = '|';
	public static final char CH_PORT = '=';
	public static final char CH_CONN = '+';
	public static final char CH_CROSS = 'x';
	public static final char CH_NEG = 'o';
	public static final char CH_GATE = '#';

	public static final char CH_FALSE = '0';
	public static final char CH_TRUE = '1';
	
	public static final char CH_AND = '&';
	public static final char CH_OR = '1';
	public static final char CH_XOR = '=';

	int xcnt;
	char[][] map = new char[MAXX][MAXY];
	char[][] val = new char[MAXX][MAXY];
	char[] result = new char[26];

	char doFindValue(int i, int j, int di, int dj, char ch) {
		char x;
		switch (ch) {
			case CH_EMPTY:
				return CH_EMPTY;
			case CH_HORIZ:
				return (di != 0) ? CH_EMPTY : findValue(i, j+dj, 0, dj);
			case CH_VERT:
				return (dj != 0) ? CH_EMPTY : findValue(i+di, j, di, 0);
			case CH_CONN:
				x = (di > 0) ? CH_EMPTY : findValue(i-1, j, -1, 0);
				if (x != CH_EMPTY) return x;
				x = (di < 0) ? CH_EMPTY : findValue(i+1, j, +1, 0);
				if (x != CH_EMPTY) return x;
				x = (dj > 0) ? CH_EMPTY : findValue(i, j-1, 0, -1);
				if (x != CH_EMPTY) return x;
				x = (dj < 0) ? CH_EMPTY : findValue(i, j+1, 0, +1);
				return x;
			case CH_CROSS:
				return findValue(i+di, j+dj, di, dj);
			case CH_PORT:
				if (di != 0 || dj > 0) return CH_EMPTY;
				return findValue(i, j-1, 0, -1);
			case CH_NEG:
				if (di != 0 || dj > 0) return CH_EMPTY;
				x = findValue(i, j-1, 0, -1);
				if (x == CH_TRUE) return CH_FALSE;
				if (x == CH_FALSE) return CH_TRUE;
				return CH_EMPTY;
			case CH_FALSE:
			case CH_TRUE:
				return ch;
			case CH_GATE:
				return computeGate(i,j);
			default:
				return CH_EMPTY;
		}
	}

	char findValue(int i, int j, int di, int dj) {
		if (i < 0 || i >= xcnt || j < 0 || j >= MAXY) return CH_EMPTY;
		char ch = val[i][j];
		if (ch != CH_EMPTY) return ch;
		ch = map[i][j];
		map[i][j] = CH_EMPTY;
		char x = doFindValue(i, j, di, dj, ch);
		map[i][j] = ch;
		//System.out.println("  DBG> ["+i+","+j+"]["+di+","+dj+"]: " + x);
		if (x != CH_EMPTY && ch != CH_CROSS) val[i][j] = x;
		return x;
	}

	char computeGate(int i, int j) {
		int i1 = i, i2 = i, j2 = j, j1 = j;
		while (i1 > 0 && map[i1-1][j] == CH_GATE) --i1;
		while (i2 < xcnt-1 && map[i2+1][j] == CH_GATE) ++i2;
		while (j1 > 0 && map[i1][j1-1] == CH_GATE) --j1;
		char gt = CH_EMPTY;
		for (i = i1+1; i < i2; ++i)
			for (j = j1+1; j < j2; ++j)
				if (map[i][j] != CH_EMPTY) gt = map[i][j];
		//System.out.println("  GATE> ["+i1+"-"+i2+","+j1+"-"+j2+"]: " + i+","+j+" : " + gt);
		boolean res = (gt == CH_AND);
		char c;
		for (i = i1; i <= i2; ++i) {
			c = findValue(i, j1-1, 0, -1);
			if (c == CH_TRUE || c == CH_FALSE) {
				if (gt == CH_AND) res = res && (c == CH_TRUE);
				else if (gt == CH_OR) res = res || (c == CH_TRUE);
				else if (gt == CH_XOR && (c == CH_TRUE)) res = !res;
			}
		}
		c = res ? CH_TRUE : CH_FALSE;
		for (i = i1; i <= i2; ++i) {
			val[i][j2] = c;
		}
		return c;
	}

	void run() throws Exception {
		for (;;) {
			for (xcnt = 0; ; ++xcnt) {
				String s = input.readLine();
				if (s.length() > 0 && s.charAt(0) == CH_END) break;
				for (int j = 0; j < s.length(); ++j) {
					map[xcnt][j] = s.charAt(j);
					val[xcnt][j] = CH_EMPTY;
				}
				for (int j = s.length(); j < MAXY; ++j) {
					val[xcnt][j] = map[xcnt][j] = CH_EMPTY;
				}
			}
			if (xcnt == 0) break;
			for (char ch = 'A'; ch <= 'Z'; ++ch) {
				result[ch-'A'] = CH_EMPTY;
			}
			for (int i = 0; i < xcnt; ++i) {
				for (int j = 1; j < MAXY; ++j) {
					char ch = map[i][j];
					if (Character.isUpperCase(ch)) {
						result[ch-'A'] = findValue(i, j-1, 0, -1);
					}
				}
			}
			for (char ch = 'A'; ch <= 'Z'; ++ch) {
				char r = result[ch-'A'];
				if (r == CH_TRUE || r == CH_FALSE) {
					System.out.println(ch + "=" + r);
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		(new logic()).run();
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
