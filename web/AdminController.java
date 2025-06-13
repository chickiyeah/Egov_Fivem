package egovframework.com.fivemlist.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
import egovframework.com.fivemlist.service.CarTradeVO;
import egovframework.com.fivemlist.service.InGameVO;
import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;
import egovframework.com.fivemlist.web.encrypt;

import javax.annotation.Resource;





@Controller
public class AdminController {
	
	@Resource(name = "userService") 
	private UserService service;
	
	
	
	@RequestMapping("/admin/userlist.do")
	public String userlist_p(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> result = new HashMap<>();

        String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
        	System.out.println("trying refreshtoken");
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
	        
	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	        	result.put("status", "fail");
	            result.put("message", "Permission Denied");
	            return "redirect:/auth/login.do";
	        }

	        // 새로운 access_token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");
	        
	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
	        verify = accessToken;
	        response.addCookie(accessTokenCookie);
       }
	        
	        String user_role = "";
	        if (verify != "null") {
	        	String type = JwtUtil.getTypeFromToken(verify);
	        	model.addAttribute("u_type", type);
	        	user_role = type;
	        }
			
			if (!user_role.equals("admin")) {
				return "redirect:/main.do";
			}
			
			List<UserVO> users = service.getAllUser();
			model.addAttribute("users",users);
		return "frozen/admin/list";
	}
	
	@RequestMapping("/admin/ingame_manage.do")
	public String ingamemanage_p(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		 Map<String, Object> result = new HashMap<>();

	        String verify = JwtUtil.verifyToken(request);
	        if (verify == "expired") {
	       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
		        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
		        
		        if (tokens == null) {
		            // refresh_token이 유효하지 않거나 만료된 경우
		        	result.put("status", "fail");
		            result.put("message", "Permission Denied");
		            return "redirect:/auth/login.do";
		        }

		        // 새로운 access_token을 HttpOnly 쿠키에 설정
		        String accessToken = tokens.get("accessToken");
		        
		        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
		        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
		        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
		        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
		        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		        verify = accessToken;
		        response.addCookie(accessTokenCookie);
	       }
	        
	        if (verify == "null") {return "redirect:/auth/login.do";}
	        
	        String user_role = "";
	        String type = JwtUtil.getTypeFromToken(verify);
	        model.addAttribute("u_type", type);
	        user_role = type;
	        
			
			if (!user_role.equals("admin")) {
				return "redirect:/main.do";
			}
			
		return "frozen/admin/ingame_manage";
	}
	
	@RequestMapping(value = "/admin/updateRole.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateRole(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVO userVO) throws Exception {
		Map<String, Object> result = new HashMap<>();

        String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
        	System.out.println("trying refreshtoken");
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
	        
	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	        	result.put("status", "fail");
	            result.put("message", "Permission Denied");
	            return result;
	        }

	        // 새로운 access_token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");
	        
	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
	        verify = accessToken;
	        response.addCookie(accessTokenCookie);
       }
        
        String user_role = "";
        

        String type = JwtUtil.getTypeFromToken(verify);
        user_role = type;
        
		
		if (!user_role.equals("admin")) {
			result.put("status", "fail");
        	result.put("message", "Permission Denied");
        	return result;
		}
		
		boolean res = service.updatePermission(userVO.getNo(), userVO.getUserType());
		if (res) {
			result.put("status", "success");
			return result;
		} else {
			result.put("status", "fail");
        	result.put("message", "UnKnown Error");
        	return result;
		}
	}
	
	@RequestMapping(value = "/admin/resetPwd.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resetPwd(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVO userVO) throws Exception {
		Map<String, Object> result = new HashMap<>();

        String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
        	System.out.println("trying refreshtoken");
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
	        
	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	        	result.put("status", "fail");
	            result.put("message", "Permission Denied");
	            return result;
	        }

	        // 새로운 access_token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");
	        
	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
	        verify = accessToken;
	        response.addCookie(accessTokenCookie);
       }
        
        String user_role = "";
        
        String type = JwtUtil.getTypeFromToken(verify);
        user_role = type;
        
		
		if (!user_role.equals("admin")) {
			result.put("status", "fail");
        	result.put("message", "Permission Denied");
        	return result;
		}
		
		boolean res = service.updatePassword(userVO.getNo(), "userpwd123!");
		if (res) {
			result.put("status", "success");
			return result;
		} else {
			result.put("status", "fail");
        	result.put("message", "UnKnown Error");
        	return result;
		}
	}
	
	@RequestMapping(value = "/admin/ingameDo.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ingameDo(HttpServletRequest request, HttpServletResponse response, @RequestBody InGameVO ingameVO) throws Exception {
		Map<String, Object> result = new HashMap<>();

        String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
        	System.out.println("trying refreshtoken");
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
	        
	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	        	result.put("status", "fail");
	            result.put("message", "Permission Denied");
	            return result;
	        }

	        // 새로운 access_token을 HttpOnly 쿠키에 설정
	        String accessToken = tokens.get("accessToken");
	        
	        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
	        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
	        accessTokenCookie.setSecure(false);   // HTTPS 환경에서만 전송
	        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 접근 가능
	        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
	        verify = accessToken;
	        response.addCookie(accessTokenCookie);
       }
        
        String user_role = "";
        
        String type = JwtUtil.getTypeFromToken(verify);
        user_role = type;
        
		
		if (!user_role.equals("admin")) {
			result.put("status", "fail");
        	result.put("message", "Permission Denied");
        	return result;
		}
		
		boolean res = service.IngameDo(ingameVO);
		if (res) {
			result.put("status", "success");
			return result;
		} else {
			result.put("status", "fail");
        	result.put("message", "UnKnown Error");
        	return result;
		}
	}
	
	
    // --- 공통 메서드 ---
    private String getUserIdFromToken(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) return null;

        return JwtUtil.validateAndGetUserId(token);
    }
}
