package egovframework.com.fivemlist.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.com.fivemlist.service.GroupVO;

@Repository("GroupDAO")
public class GroupDAO extends EgovAbstractMapper {

    public GroupVO getFaction(int no) {
        return selectOne("GroupDAO.getFaction", no);
    }

    public List<GroupVO> getFactionListPaged(Map<String, Object> params) {
        return selectList("GroupDAO.getFactionListPaged", params);
    }

    public List<GroupVO> searchFactions(Map<String, Object> params) {
        return selectList("GroupDAO.searchFactions", params);
    }

    public int getFactionCount(Map<String, Object> params) {
        return selectOne("GroupDAO.getFactionCount", params);
    }

    public boolean insertFaction(GroupVO vo) {
        int result = insert("GroupDAO.insertFaction", vo);
        return result > 0;
    }

    public boolean updateFaction(GroupVO vo) {
        int result = update("GroupDAO.updateFaction", vo);
        return result > 0;
    }

    public boolean deleteFaction(int no) {
        int result = delete("GroupDAO.deleteFaction", no);
        return result > 0;
    }
}
