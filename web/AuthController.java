package egovframework.com.fivemlist.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;
import egovframework.com.fivemlist.web.encrypt;

import javax.annotation.Resource;





@Controller
public class AuthController {
	
	
	@Resource(name = "userService") 
	private UserService service;
	
	
	@RequestMapping(value = "/auth/login.do")
	public String login_p(HttpServletRequest request) throws Exception {
		return "frozen/auth/login";
	}
	
	@RequestMapping(value = "/auth/register.do")
	public String register_p(HttpServletRequest request) throws Exception {
		return "frozen/auth/register";
	}
	
	private void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // 쿠키 만료
        cookie.setPath("/"); // 경로 지정 (토큰 생성 경로와 동일하게)
        cookie.setHttpOnly(true); // 필요 시 설정
        response.addCookie(cookie);
    }
	
	@RequestMapping("/api/user/logout.do")
	@ResponseBody
    public Map<String, Object>  logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        // 쿠키 삭제
		service.disableRefreshToken(refreshToken);
        deleteCookie("access_token", response);
        deleteCookie("refresh_token", response);

        // 세션 무효화 (필요 시)
        request.getSession().invalidate();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");

        // 로그아웃 후 이동할 페이지
        return responseMap;
    }
	
	@PostMapping(value = "/api/user/login.do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> login(@RequestBody Map<String, String> data, HttpServletResponse response) throws Exception {
	    UserVO user = new UserVO();
	    user.setId(data.get("id"));
	    user.setPwd(encrypt.doencrypt(data.get("password")));

	    @SuppressWarnings("unchecked")
		Map<String, String> tokens = (Map<String, String>) service.login(user);

	    Map<String, Object> responseMap = new HashMap<>();
	    if (tokens == null) {
	        responseMap.put("status", "fail");
	        responseMap.put("message", "Invalid ID or Password");
	    } else {
	        // 로그인 성공
	        responseMap.put("status", "success");

	        // Access Token과 Refresh Token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");
	        String refreshToken = tokens.get("refreshToken");

	        // Access Token 쿠키 설정
	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(true); // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 1시간 (예시)

	        // Refresh Token 쿠키 설정
	        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
	        refreshTokenCookie.setHttpOnly(true);
	        refreshTokenCookie.setSecure(true);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 (예시)
	        
	        // 쿠키를 응답에 추가
	        response.addCookie(accessTokenCookie);
	        response.addCookie(refreshTokenCookie);
	    }

	    return responseMap;
	}
	
	@RequestMapping(value = {"/api/user/verify-token.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> verifyToken(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
        String accessToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                	accessToken = cookie.getValue();
                    break;
                }
            }
        }
        
        if (accessToken == null) {
        	 return Map.of("status", "fail", "message", "Access token is missing");
        }
        
        String uid = JwtUtil.validateAndGetUserId(accessToken);
        
        if (uid == null) {

              	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
       	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);

       	        if (tokens == null) {
       	            // refresh_token이 유효하지 않거나 만료된 경우
       	        	return Map.of("status", "expired", "message", "Access token is Expired");
       	        }

       	        // 새로운 access_token을 HttpOnly 쿠키에 설정
       	        String maccessToken = tokens.get("accessToken");

       	        Cookie accessTokenCookie = new Cookie("access_token", maccessToken);
       	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
       	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
       	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
       	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 1시간
       	        
       	     response.addCookie(accessTokenCookie);
       	  return Map.of("status", "success", "message", uid);

        	
        } else {
        	return Map.of("status", "success", "message", uid);
        }
	}
	
	@RequestMapping(value = {"/api/user/refresh-token.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
	        // 클라이언트에서 전달받은 refresh_token을 쿠키에서 찾음
	        Cookie[] cookies = request.getCookies();
	        String refreshToken = null;
	        
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if ("refresh_token".equals(cookie.getName())) {
	                    refreshToken = cookie.getValue();
	                    break;
	                }
	            }
	        }
	        
	        if (refreshToken == null) {
	            // refresh_token이 없으면 자동 로그인 실패
	            return Map.of("status", "fail", "message", "Refresh token is missing");
	        }

	        // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);

	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	            return Map.of("status", "fail", "message", "Invalid refresh token");
	        }

	        // 새로운 access_token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");

	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 1시간

	        response.addCookie(accessTokenCookie);

	        return Map.of("status", "success", "accessToken", accessToken);
	    }
	
	// 아이디 중복 확인 API
    @RequestMapping(value = {"/api/user/checkId.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkId(@RequestBody UserVO user) {
    	System.out.println(user.getId());
        boolean isIdExist = service.checkIdExist(user);

        if (isIdExist) {
            return "{\"exists\": \"True\"}";
        } else {
            return "{\"exists\": \"False\"}";
        }
    }
    
 // 아이디 중복 확인 API
    @RequestMapping(value = {"/api/user/checkEmail.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkEmail(@RequestBody UserVO user) {
    	System.out.println(user.getEmail());
        boolean isIdExist = service.checkEmailExist(user);

        if (isIdExist) {
            return "{\"exists\": \"True\"}";
        } else {
            return "{\"exists\": \"False\"}";
        }
    }
    @RequestMapping(value = {"/api/user/checkKey.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> checkKey(@RequestBody UserVO user) {
        boolean result = service.checkKey(user);
        Map<String, Object> res = new HashMap<String, Object>();

        if (result) {
        	res.put("status", "success");
            return res;
        } else {
        	res.put("status", "fail");
        	res.put("message", "notfound");
            return res;
        }
    }
    
    @RequestMapping(value = {"/api/user/verifyKey.do"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> verifyKey(@RequestBody UserVO user) {
        boolean result = service.verifyKey(user);
        Map<String, Object> res = new HashMap<String, Object>();

        if (result) {
        	res.put("status", "success");
            return res;
        } else {
        	res.put("status", "fail");
        	res.put("message", "notfound");
            return res;
        }
    }
	
	@RequestMapping(value = "/api/user/register.do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String registerUser(@RequestBody UserVO user) throws Exception {
        // 회원가입 처리
		System.out.println(user.getProfileImage());
		user.setProfileImage("/assets"+user.getProfileImage().split("/assets")[1]);
		System.out.println("here?");
		boolean isIdExist = service.checkIdExist(user);
		boolean isEmailExist = service.checkEmailExist(user);
		
		
		if (isIdExist) {
			return "{\"detail\": \"ER011\"}";
		}
		
		if (isEmailExist) {
			return "{\"detail\": \"ER010\"}";
		}
		
		
		boolean success = service.registerUser(user);

        if (success) {
            return "{\"detail\": \"SUC\",\"message\": \"Registration successful.\"}";
        } else {
            return "{\"message\": \"Email already exists.\"}";
        }
    }
	
	
	 	@ResponseBody
	    @PostMapping("/api/user/findId.do")
	    public Map<String, Object> findId(@RequestParam("email") String email) {
	        List<String> ids = service.findIdsByEmail(email);
	        Map<String, Object> result = new HashMap<>();
	        result.put("count", ids.size());
	        //result.put("ids", ids);
	        return result;
	    }

	    @ResponseBody
	    @PostMapping("/api/user/resetPw.do")
	    public Map<String, Object> resetPassword(@RequestParam("email") String email) throws Exception {
	        boolean success = service.resetPassword(email);
	        Map<String, Object> result = new HashMap<>();
	        result.put("success", success);
	        return result;
	    }
	    
	    @GetMapping("/auth/findaccount.do")
	    public String findaccount_p(HttpServletRequest request) throws Exception {
			return "frozen/auth/findaccount";
		}
	    
}
