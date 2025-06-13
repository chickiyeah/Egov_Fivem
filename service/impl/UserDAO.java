package egovframework.com.fivemlist.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.com.fivemlist.service.InGameUserVO;
import egovframework.com.fivemlist.service.InGameVO;
import egovframework.com.fivemlist.service.UserVO;

@Repository("UserDAO")
public class UserDAO extends EgovAbstractMapper {

    // 기존 로그인 메서드
    public UserVO login(UserVO vo) {
        return (UserVO) selectOne("UserDAO.login", vo);
    }
    
    public List<String> selectIdsByEmail(String Email) {
    	HashMap<String, String> param = new HashMap<>();
    	param.put("email", Email);
    	return selectList("UserDAO.selectIdsByEmail", param);
    }
    
    public UserVO getFaction(UserVO vo) {
    	return (UserVO) selectOne("UserDAO.getFAction", vo);
    }

    // Refresh Token 저장 메서드
    public void saveRefreshToken(String userId, String refreshToken) {
        HashMap<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("refreshToken", refreshToken);
        Integer count = (Integer) selectOne("UserDAO.selectRefreshTokenByUserId", param);
        if (count > 0) {
        	update("UserDAO.updateRefreshToken", param);
        } else {
        	insert("UserDAO.saveRefreshToken", param); // SQL문은 나중에 설명
        }
    }
    
    public boolean verifyRefreshToken(String userId, String refreshToken) {
    	HashMap<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("refreshToken", refreshToken);
        Integer count = (Integer) selectOne("UserDAO.verityRefreshToken", param);
        return count > 0;
    }
    
    public void disableRefreshToken(String refreshToken) {
    	HashMap<String, String> param = new HashMap<>();
        param.put("refreshToken", refreshToken);
        update("UserDAO.disableRefreshToken", param);
    }
    
    public String getRefreshTokenByUserId(String userId) {
        return (String) selectOne("UserDAO.getRefreshTokenByUserId", userId);
    }
    
    // 이메일 중복 체크
    public boolean checkEmailExist(UserVO user) {
        Integer count = (Integer) selectOne("UserDAO.checkEmailExist", user);
        return count > 0;
    }
    
    // 아이디 중복 체크
    public boolean checkIdExist(UserVO user) {
        Integer count = (Integer) selectOne("UserDAO.checkIdExist", user);
        return count > 0;
    }

    // 회원가입 (사용자 정보 삽입)
    public void insertUser(UserVO user) {
        insert("UserDAO.insertUser", user);
    }
    
    public UserVO findUserByEmail(String email) {
        return selectOne("UserDAO.selectUserByEmail", email);
    }

    public boolean updatePassword(UserVO user) {
        return update("UserDAO.updatePassword", user) > 0;
    }
    
    public InGameUserVO getUserByIngameId(InGameUserVO user) {
        List<InGameUserVO> users = selectList("UserDAO.getUserByIngameId", user);
        
        for (InGameUserVO u : users) {
            String faction = u.getFaction();
            String r_faction = u.getFullaction();
            
            if (!r_faction.contains(faction + " 블랙리스트")) {
                return u; // 조건 맞는 u를 리턴
            }
        }
        
        return null; // 조건에 맞는 게 없으면 null
    }

    
    public List<UserVO> getAllUser() {
    	return selectList("UserDAO.getAllUser");
    }

	public boolean updatePermission(UserVO user) {
		Integer result = update("UserDAO.updatePermission", user);
		return result > 0;
	}
	
	public boolean IngameDo(InGameVO vo) {
		Integer result = insert("UserDAO.IngameDo", vo);
		return result > 0;
	}
	
	public boolean verifyKey(UserVO user) {
		Integer result = update("UserDAO.verifyKey", user);
		return result > 0;
	}
	
	public boolean checkKey(UserVO user) {
		Integer result = selectOne("UserDAO.checkKey", user);
		return result > 0;
	}

	public UserVO selectUserWithID(String userId) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", userId);
		System.out.println("dao");
		return selectOne("UserDAO.selectUserWithID", param);
	}
}
