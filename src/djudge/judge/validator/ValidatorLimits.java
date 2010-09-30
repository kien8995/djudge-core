/* $Id$ */

package djudge.judge.validator;

public interface ValidatorLimits
{
	// 60 sec
	final int VALIDATOR_MAX_RUNNING_TIME = 60000;
	
	// 256 MB
	final int VALIDATOR_MAX_CONSUMED_MEMORY = 1024 * 1024 * 256;
	
	// 20 MB
	final int VALIDATOR_MAX_OUTPUT_SIZE = 20 * 1024 * 1024;
}
