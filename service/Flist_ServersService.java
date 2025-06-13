package egovframework.com.fivemlist.service;

import java.util.List;

public interface Flist_ServersService {
	public List<ServerVO> getServers();

	ServerVO getServerInfo(String endpoint);
}
