/* $Id$ */

package djudge.judge.checker;

/* Enumeration for possible types of checkers */
public enum CheckerTypeEnum
{
	/**
	 * Line-by-line comparing (whitespace is counted)
	 */
	InternalExact,
	
	InternalSortedExact,
	
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
	
	public static CheckerTypeEnum parse(String str)
	{
		CheckerTypeEnum res = CheckerTypeEnum.Unknown;
		for (CheckerTypeEnum curr : CheckerTypeEnum.values())
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
