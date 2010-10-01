/*
 * Sample solution to the Roshambo problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;


public class roshambo
{
	final static Object P = "_paper";
	final static Object R = "_rock";
	final static Object S = "_scissors";

	static final Map<String,Object> dictionary = new HashMap<String,Object>();
	static final Map<Object,Object> winOver = new HashMap<Object,Object>();
	static {
		winOver.put(R, S); winOver.put(S, P); winOver.put(P, R);

		dictionary.put("Kamen", R);
		dictionary.put("Rock", R);
		dictionary.put("Pierre", R);
		dictionary.put("Stein", R);
		dictionary.put("Ko", R);
		dictionary.put("Koe", R);
		dictionary.put("Sasso", R);
		dictionary.put("Roccia", R);
		dictionary.put("Guu", R);
		dictionary.put("Kamien", R);
		dictionary.put("Piedra", R);

		dictionary.put("Nuzky", S);
		dictionary.put("Scissors", S);
		dictionary.put("Ciseaux", S);
		dictionary.put("Schere", S);
		dictionary.put("Ollo", S);
		dictionary.put("Olloo", S);
		dictionary.put("Forbice", S);
		dictionary.put("Choki", S);
		dictionary.put("Nozyce", S);
		dictionary.put("Tijera", S);

		dictionary.put("Papir", P);
		dictionary.put("Paper", P);
		dictionary.put("Feuille", P);
		dictionary.put("Papier", P);
		dictionary.put("Carta", P);
		dictionary.put("Rete", P);
		dictionary.put("Paa", P);
		dictionary.put("Papel", P);
	}

	String name1, name2;
	int pts1, pts2;
	int game = 0;

	void run() throws Exception {
		boolean go = true;
		while (go) {
			nextToken(); name1 = nextToken();
			nextToken(); name2 = nextToken();
			pts1 = pts2 = 0;
			for (;;) {
				String w = nextToken();
				if (w.charAt(0) == '-') break;
				if (w.charAt(0) == '.') { go = false; break; }
				Object x1 = dictionary.get(w);
				Object x2 = dictionary.get(nextToken());
				if (x1 == null || x2 == null) {
					System.out.println("ERROR!");
					break;
				}
				if (winOver.get(x1) == x2) ++pts1;
				if (winOver.get(x2) == x1) ++pts2;
			}
			System.out.println("Game #" + (++game) + ":");
			System.out.println(name1 + ": " + pts1 + ((pts1==1) ? " point" : " points"));
			System.out.println(name2 + ": " + pts2 + ((pts2==1) ? " point" : " points"));
			if (pts1 > pts2) System.out.println("WINNER: " + name1);
			else if (pts1 < pts2) System.out.println("WINNER: " + name2);
			else System.out.println("TIED GAME");
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		(new roshambo()).run();
	}

	StringTokenizer st = new StringTokenizer("");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	String nextToken() throws Exception {
		while (!st.hasMoreTokens()) st = new StringTokenizer(input.readLine());
		return st.nextToken();
	}
}
