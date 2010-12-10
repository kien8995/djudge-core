package djudge.judge.executor;

// taken from runner.cpp
public interface ExecutorLinuxExitCodes
{
	public final static int EXIT_OK	= 50;
	public final static int EXIT_TLE = 51;
	public final static int EXIT_MLE = 52;
	public final static int EXIT_OLE = 53;
	public final static int EXIT_RE = 54;
	public final static int EXIT_IE	= 55;

	public final static int EXIT_UNKNOWN = 60;

	// TLE
	public static final int EXIT_SIGNAL_SIGKILL = 137;
}
