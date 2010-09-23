/* $Id$ */

package djudge.dservice.interfaces;

import djudge.dservice.DServiceTask;


public interface DServiceNativeJudgeInterface extends DServiceCommonJudgeInterface
{
	public DServiceTask getTask(int judgeID);
}
