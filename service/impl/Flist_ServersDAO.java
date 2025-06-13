package egovframework.com.fivemlist.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.com.fivemlist.service.ServerVO;

@Repository("Flist_ServersDAO")
public class Flist_ServersDAO  extends EgovAbstractMapper {
	public List<ServerVO> getServers() {
		return selectList("Flist_ServersDAO.getServers");
	}
}
