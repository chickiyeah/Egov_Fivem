package egovframework.com.fivemlist.service;

import java.util.List;

import egovframework.com.fivemlist.service.UserVO;

public interface UserService {
    // 로그인
    Object login(UserVO user);

    // 이메일 중복 체크
    boolean checkEmailExist(UserVO user);

    // 회원가입
    boolean registerUser(UserVO user) throws Exception;
    
    // 아이디 중복 체크
    boolean checkIdExist(UserVO user);
    
    //비밀번호 초기화
    boolean resetPassword(String email) throws Exception;
    
    boolean verifyRefreshToken(String userId, String refreshToken);
    
    void disableRefreshToken(String refreshToken);
    List<String> findIdsByEmail(String email);
    
    boolean resetPasswordByEmail(String email) throws Exception;
    
    int updateTempPassword(String email, String newPassword);

    InGameUserVO getUserByIngameId(InGameUserVO user);

	List<UserVO> getAllUser();

	boolean updatePassword(Integer no, String pwd) throws Exception;

	boolean updatePermission(Integer no, String per) throws Exception;

	boolean IngameDo(InGameVO vo);

	boolean verifyKey(UserVO user);

	boolean checkKey(UserVO user);

	UserVO selectUserWithID(String userId);
}
