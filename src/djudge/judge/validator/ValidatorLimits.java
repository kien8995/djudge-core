/* $Id$ */

package djudge.judge.validator;

public interface ValidatorLimits
{
	final int MAX_VALIDATOR_RUNNING_TIME = 60000;
	final int MAX_VALIDATOR_CONSUMED_MEMORY = 1024 * 1024 * 256;
	final int MAX_VALIDATOR_OUTPUT_SIZE = 1024 * 1024;
}
