import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Scanner;

public class Main
{
	StreamTokenizer in;
	Scanner inr;
    PrintWriter out;

    int nextInt() throws IOException
    {
    	in.nextToken();
    	return (int)in.nval;
    }
    
    void input() throws IOException
    {
    	inr = new Scanner(System.in);
    	in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    	out = new PrintWriter(new OutputStreamWriter(System.out));
    }
    
    int n, res;
    
    void output()
    {
    	out.println("" + res);
    	out.flush();
    }
    
    void solve()
    {
    	int div_count = 1, euler = (int)n;
    	int N = n;
    	int k = 2;
		if (n % k == 0)
		{
			int c = 0;
			euler /= k;
			euler *= k - 1;
			while (n % k == 0)
			{
				n /= k; c++;
			}
			div_count *= c + 1;
		}
    	for (k = 3; k * k <= n; k+=2)
    		if (n % k == 0)
    		{
    			int c = 0;
    			euler /= k;
    			euler *= k - 1;
    			while (n % k == 0)
    			{
    				n /= k; c++;
    			}
    			div_count *= c + 1;
    		}
    	if (n > 1)
    	{
    		div_count *= 2;
			euler /= n;
			euler *= n - 1;
    	}
    	//print("" + euler + "  " + div_count);
    	res = N - (euler + div_count - 1);
    }
    
    void run() throws Exception
    {
		input();
    	for (;;)
    	{
    		n = inr.nextInt();
    		if (n == 0) break;
    		solve();
    		output();
    	}
    }
    
	public static void main(String[] args) throws Exception
	{
		new Main().run(); 
	}
    void print(String s)
    {
    	System.out.println(s);
    }

}
