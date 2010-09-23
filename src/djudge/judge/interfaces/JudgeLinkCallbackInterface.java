package djudge.judge.interfaces;

import djudge.dservice.DServiceTask;
import djudge.judge.JudgeTaskResult;

public interface JudgeLinkCallbackInterface
{
	public void reportError(int judgeId, String error);
	
	public void reportConnectionLost(int judgeId, String error);
	
	public void reportConnectionRecovered(int judgeId, String error);
	
	public void reportSubmissionReceived(int judgeId, DServiceTask task);
	
	public void reportSubmissionJudged(int judgeId, JudgeTaskResult res);
	
	public void reportSubmissionReportSent(int judgeId, JudgeTaskResult res);
}
