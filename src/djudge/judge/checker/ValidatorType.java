/* $Id$ */

package djudge.judge.checker;

/* Enumeration for possible types of validators */
public enum ValidatorType
{
	/**
	 * Line-by-line comparing (whitespace is counted)
	 */
	InternalExact,
	
	/**
	 * Token-by-token comparing  
	 */
	InternalToken,

	InternalSortedToken,
	
	/**
	 * Same as 'InternalToken', but each token must present valid 32-bit signed integer value
	 */
	InternalInt32,
	
	/**
	 * Same as 'InternalToken', but each token must present valid 64-bit signed integer value
	 */
	InternalInt64,

	InternalFloatAbs,
	InternalFloatRel,
	InternalFloatAbsRel,
	InternalFloatOther,
	ExternalTestLib,
	ExternalTestLibJava,
	ExternalPC2,
	ExternalCustom,
	ExternalExitCode,
	ExternalExitCodeExtended,
	Unknown;
	
	public static ValidatorType parse(String str)
	{
		ValidatorType res = ValidatorType.Unknown;
		for (ValidatorType curr : ValidatorType.values())
		{
			if (curr.toString().equals(str))
			{
				res = curr;
			}
		}
		return res;
	}
	
	public boolean isExternal()
	{
		return this.toString().startsWith("External");
	}

	public boolean isParametrized()
	{
		return this == InternalFloatAbs || this == InternalFloatAbsRel || this == InternalFloatRel;
	}	
}
