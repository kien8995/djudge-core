/* $Id$ */

package djudge.dservice.interfaces;

import java.util.HashMap;

public interface DServiceXmlRpcJudgeInterface extends DServiceCommonJudgeInterface
{
	@SuppressWarnings("unchecked")
	public HashMap getTask(int judgeID);
}
