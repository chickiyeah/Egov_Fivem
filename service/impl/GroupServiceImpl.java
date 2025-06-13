package egovframework.com.fivemlist.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.GroupService;
import egovframework.com.fivemlist.service.GroupVO;
import egovframework.com.fivemlist.service.impl.GroupDAO;

@Service("GroupService")
public class GroupServiceImpl implements GroupService {

    @Resource(name = "GroupDAO")
    private GroupDAO groupDAO;

    @Override
    public GroupVO getFaction(int no) {
        return groupDAO.getFaction(no);
    }

    @Override
    public List<GroupVO> getFactionListPaged(Map<String, Object> params) {
        return groupDAO.getFactionListPaged(params);
    }

    @Override
    public List<GroupVO> searchFactions(Map<String, Object> params) {
        return groupDAO.searchFactions(params);
    }

    @Override
    public int getFactionCount(Map<String, Object> params) {
        return groupDAO.getFactionCount(params);
    }

    @Override
    public boolean insertFaction(GroupVO vo) {
        return groupDAO.insertFaction(vo);
    }

    @Override
    public boolean updateFaction(GroupVO vo) {
        return groupDAO.updateFaction(vo);
    }

    @Override
    public boolean deleteFaction(int no) {
        return groupDAO.deleteFaction(no);
    }
    
    



}
