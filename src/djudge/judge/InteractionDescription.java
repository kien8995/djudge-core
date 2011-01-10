package djudge.judge;

import org.w3c.dom.Element;

import djudge.common.Deployment;

public class InteractionDescription
{
	private InteractionType interactionType;

	@SuppressWarnings("unused")
	private String interactorExecuteble;
	
	public InteractionDescription()
	{
		interactionType = InteractionType.NONE;
	}
	
	public InteractionDescription(Element elem)
	{
		interactionType = InteractionType.NEERC_INTERACTOR;
	}

	public void setInteractionType(InteractionType interactionType)
	{
		this.interactionType = interactionType;
	}

	public InteractionType getInteractionType()
	{
		return interactionType;
	}

	public void setInteractorExe(String interactorExecuteble)
	{
		this.interactorExecuteble = interactorExecuteble;
	}

	public String getInteractorExe()
	{
		return "interact" + (Deployment.isOSWinNT() ? ".exe" : ".o");
	}
}
