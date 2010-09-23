/* $Id$ */

package djudge.dservice.interfaces;

public interface DServiceCommonClientInterface
{
	int submitSolution(String uid, String contestId, String problemId, String languageId, String source, String clientData, String params);
}
