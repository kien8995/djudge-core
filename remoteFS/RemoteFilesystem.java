package remoteFS;

public interface RemoteFilesystem
{
	public boolean copyRemoteFile(String localFilename, String remoteFilename);
}
