/*
 * Sample solution to the Water problem.
 * Central Europe Regional Contest 2007.
 *
 * Martin Kacer, 2007
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class water
{
	static final double CHECK_MAX = 100.0;
	static final double CHECK_MIN = 0.1;

	static final double DENSITY = 2.5;
	static final double EPS = 1E-10;
	static final double PREC = 0.000025;
	static final DecimalFormat OUTFORM = new DecimalFormat("0.000");

	abstract class Node {
		Node left, right;
		abstract double compute();
	}
	class Multiply extends Node { double compute() { return left.compute() * right.compute(); }}
	class Divide extends Node { double compute() { return left.compute() / right.compute(); }}
	class Add extends Node { double compute() { return left.compute() + right.compute(); }}
	class Subtract extends Node { double compute() { return left.compute() - right.compute(); }}
	class Param extends Node { double compute() { return x; }}
	class Constant extends Node { double val; double compute() { return val; }}

	String inp;
	int pos;
	char peek() { return (pos < inp.length()) ? inp.charAt(pos) : ' '; }
	char next() { return inp.charAt(pos++); }

	Node parseExpr() {
		Node n1 = parseTerm();
		char c = peek();
		while (c == '+' || c == '-') {
			next();
			Node n2 = parseTerm();
			Node nn = (c == '+') ? (Node)new Add() : (Node)new Subtract();
			nn.left = n1; nn.right = n2; n1 = nn;
			c = peek();
		}
		return n1;
	}

	Node parseTerm() {
		Node n1 = parseFact();
		char c = peek();
		while (c == '*' || c == '/') {
			next();
			Node n2 = parseFact();
			Node nn = (c == '*') ? (Node)new Multiply() : (Node)new Divide();
			nn.left = n1; nn.right = n2; n1 = nn;
			c = peek();
		}
		return n1;
	}

	Node parseFact() {
		char c = peek();
		if (c == '(') {
			next();
			Node n = parseExpr();
			if (next() != ')') System.exit(1);
			return n;
		}
		if (c == 'x') { next(); return new Param(); }
		String s = "";
		while (Character.isDigit(c) || c == '.') { s += c; next(); c = peek(); }
		Constant n = new Constant();
		n.val = Double.parseDouble(s);
		return n;
	}

	double x;
	Node rr, tt;

	private void run() throws Exception {
		for (;;) {
			double h = nextDouble();
			if (h < EPS) break;
			double b = nextDouble();
			if (b+EPS >= h || h > CHECK_MAX) System.exit(1);
			inp = nextToken(); pos = 0; rr = parseExpr();
			inp = nextToken(); pos = 0; tt = parseExpr();
			double r, t, z;
			double sum = 0.0, tot = 0.0;
			for (x = 0; x <= h+EPS; x += PREC) {
				r = rr.compute();
				t = (x < b+EPS) ? r : tt.compute();
				if (r < CHECK_MIN || t < CHECK_MIN || (t+EPS >= r && x > b+EPS) || r > CHECK_MAX) System.exit(2);
				z = (r * r - (r-t) * (r-t)) * DENSITY;
				sum += z;
				tot += x * z;
			}
			double cupsum = sum, cuptot = tot;
			sum = tot = 0.0;
			double volume = 0.0;
			for (x = b; x <= h+EPS; x += PREC) {
				r = rr.compute();
				t = tt.compute();
				r -= t;
				z = r * r;
				volume += Math.PI * z * PREC;
				sum += z;
				tot += x * z;
				z = (tot + cuptot) / (sum + cupsum);
				if (z < x + EPS) break;
			}
			System.out.println("Pour " + OUTFORM.format(volume/1000)
					+ " litres / " + OUTFORM.format(x-b) + " cm of water.");
		}
	}

	StringTokenizer st = new StringTokenizer("");
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String[] args) throws Exception {
		new water().run();
	}
	String nextToken() throws Exception {
		while (!st.hasMoreTokens()) st = new StringTokenizer(input.readLine());
		return st.nextToken();
	}
	double nextDouble() throws Exception {
		return Double.parseDouble(nextToken());
	}
}
