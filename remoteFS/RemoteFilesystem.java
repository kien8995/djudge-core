package remoteFS;

public interface RemoteFilesystem
{
	public boolean readRemoteFile(String localFilename, String remoteFilename);
	public boolean writeRemoteFilr(String remoteFilename, String localFilename);
}
