package egovframework.com.fivemlist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.let.utl.sim.service.EgovFileScrty;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import egovframework.com.fivemlist.exception.InvalidTokenException;
import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.InGameUserVO;
import egovframework.com.fivemlist.service.InGameVO;
import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;
import egovframework.com.fivemlist.util.MailService;
import egovframework.com.fivemlist.web.encrypt;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "UserDAO")
    private UserDAO userDAO;
    
    @Autowired
    private UserService service;
    
    @Autowired
    private MailService mailService;

    @Override
    public boolean resetPassword(String email) throws Exception {
        // 이메일로 사용자 조회
        UserVO user = userDAO.findUserByEmail(email);

        if (user == null) {
            return false; // 이메일로 사용자 없음
        }

        // 임시 비밀번호 생성
        String tempPwd = generateTempPassword();
        String encryptedPwd = encrypt.doencrypt(tempPwd);

        // DB에 업데이트
        user.setPwd(encryptedPwd);
        userDAO.updatePassword(user);

        // 이메일 전송
        String subject = "임시 비밀번호 안내";
        String body = "<h3>임시 비밀번호: " + tempPwd + "</h3><p>로그인 후 꼭 변경해 주세요.</p>";
        mailService.sendHtmlEmail(email, subject, body);
        System.out.println("📨 임시 비밀번호 이베일 발송");

        return true;
    }
    
    @Override
    public boolean updatePassword(Integer no,String pwd) throws Exception {
    	UserVO user = new UserVO();
    	user.setNo(no);
    	String encryptedPwd = encrypt.doencrypt(pwd);
    	user.setPwd(encryptedPwd);
    	return userDAO.updatePassword(user);
    }
    
    @Override
    public boolean updatePermission(Integer no,String per) throws Exception {
    	UserVO user = new UserVO();
    	user.setNo(no);
    	user.setUserType(per);
    	return userDAO.updatePermission(user);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    // 기존 로그인 메서드
    @Override
    public Map<String, String> login(UserVO user) {
        UserVO result = userDAO.login(user);
        System.out.println(result);
        if (result == null || result.getId() == null) {
            return null; // 로그인 실패
        } else {

	        // 로그인 성공 시 토큰 생성
	        String accessToken = JwtUtil.generateAccessToken(result);
	        String refreshToken = JwtUtil.generateRefreshToken(result.getId());
	
	        // Refresh Token DB에 저장
	        userDAO.saveRefreshToken(result.getId(), refreshToken);
	
	        Map<String, String> tokenMap = new HashMap<>();
	        tokenMap.put("accessToken", accessToken);
	        tokenMap.put("refreshToken", refreshToken);
	
	        return tokenMap;
        }
    }
    
    @Override
    public boolean verifyRefreshToken(String userId, String refreshToken) {
    	return userDAO.verifyRefreshToken(userId, refreshToken);
    }
    
    @Override
    public void disableRefreshToken(String refreshToken) {
    	userDAO.disableRefreshToken(refreshToken);
    }
    
    // Refresh Token을 사용해 Access Token 재발급
    public String refreshAccessToken(String userId, String refreshToken) {
        // Refresh Token 만료 검증
        if (isRefreshTokenExpired(refreshToken)) {
            throw new InvalidTokenException("Refresh Token has expired");
        }

        // DB에서 해당 사용자에 대한 Refresh Token 확인
        String storedRefreshToken = userDAO.getRefreshTokenByUserId(userId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new InvalidTokenException("Invalid Refresh Token");
        }

        // Refresh Token 유효성 검증 후 새로운 Access Token 발급
        UserVO user = new UserVO();
        user.setId(userId);

        return JwtUtil.generateAccessToken(user); // 새로운 Access Token 발급
    }

    // Refresh Token 만료 확인
    private boolean isRefreshTokenExpired(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(JwtUtil.getKey()).build()
                    .parseClaimsJws(refreshToken);  // 토큰을 파싱하여 유효성 검사
            return false; // 만약 예외가 발생하지 않으면 유효
        } catch (JwtException e) {
            return true; // 유효하지 않거나 만료된 경우
        }
    }

    @Override
    public boolean checkEmailExist(UserVO user) {
        return userDAO.checkEmailExist(user);
    }
    
    @Override
    public InGameUserVO getUserByIngameId(InGameUserVO user) {
    	return userDAO.getUserByIngameId(user);
    }
    
    @Override
    public List<UserVO> getAllUser() {
    	return userDAO.getAllUser();
    }
    
    @Override
    public boolean checkIdExist(UserVO user) {
        return userDAO.checkIdExist(user);  // 아이디 중복 체크 로직 호출
    }

    @Transactional
    @Override
    public boolean registerUser(UserVO user) throws Exception {
        // 이메일 중복 체크
        if (checkEmailExist(user)) {
            return false;  // 이메일이 이미 존재하면 회원가입 실패
        }

        // 비밀번호 암호화
        String encryptedPassword = encrypt.doencrypt(user.getPwd());  // 암호화된 비밀번호로 변경
        user.setPwd(encryptedPassword);  // 암호화된 비밀번호를 설정

        // 회원 정보를 DB에 저장
        userDAO.insertUser(user);

        return true;  // 회원가입 성공
    }
    
    @Override
    public List<String> findIdsByEmail(String email) {
    	List<String> res = userDAO.selectIdsByEmail(email);
    	if (res.size() > 0) {
    		res.toString();
    		// 이메일 전송
            String subject = "아이디 찾기 안내";
            String body = "<h3>아이디목록: " + res.toString() + "</h3>";
            mailService.sendHtmlEmail(email, subject, body);
            System.out.println("📨 아이디 찾기 이베일 발송");
    	}
    	return res;
         
    }

    @Override
    public boolean resetPasswordByEmail(String email) throws Exception {
        String tempPw = generateTempPassword();
        String encodedPw = encrypt.doencrypt(tempPw); // 비밀번호 암호화

        int updated = service.updateTempPassword(email, encodedPw);
        if (updated > 0) {
            // 이메일 전송 로직 여기에 구현하면 됨 (예: JavaMailSender)
            return true;
        }
        return false;
    }


	@Override
	public int updateTempPassword(String email, String newPassword) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean IngameDo(InGameVO vo) {
		return userDAO.IngameDo(vo);
	}
	
	@Override
	public boolean verifyKey(UserVO user) {
		return userDAO.verifyKey(user);
	}
	
	@Override
	public boolean checkKey(UserVO user) {
		return userDAO.checkKey(user);
	}

	@Override
	public UserVO selectUserWithID(String userId) {
		// TODO Auto-generated method stub
		System.out.println("impl");
		return userDAO.selectUserWithID(userId);
	}
}
