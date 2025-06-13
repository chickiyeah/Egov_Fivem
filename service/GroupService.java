package egovframework.com.fivemlist.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface GroupService {
    GroupVO getFaction(int no);
    List<GroupVO> getFactionListPaged(Map<String, Object> params);
    List<GroupVO> searchFactions(Map<String, Object> params);
    int getFactionCount(Map<String, Object> params);
    boolean insertFaction(GroupVO vo);
    boolean updateFaction(GroupVO vo);
    boolean deleteFaction(int no);
}
	