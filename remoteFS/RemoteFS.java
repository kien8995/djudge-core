package remoteFS;

public interface RemoteFS
{
	public boolean readRemoteFile(String localFilename, String remoteFilename);
	public boolean writeRemoteFilr(String remoteFilename, String localFilename);
}
