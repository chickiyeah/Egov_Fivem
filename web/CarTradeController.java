package egovframework.com.fivemlist.web;

import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.CarTradeService;
import egovframework.com.fivemlist.service.CarTradeVO;
import egovframework.com.fivemlist.service.InGameUserVO;
import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;
import egovframework.com.fivemlist.service.impl.CarTradeDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carTrade")
public class CarTradeController {

    @Autowired
    private CarTradeService carTradeService;

    @Autowired
    private CarTradeDAO carTradeDAO;
    
    @Autowired 
    private UserService userService;

    // 거래 목록 조회
    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    public String selectCarTradeListJsp(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
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

        String user_role = "";
        
		if (verify != "expired" && verify != "null") {
        	String type = JwtUtil.getTypeFromToken(verify);
        	model.addAttribute("u_type", type);
        	user_role = type;
        }

		if (!user_role.equals("admin") && !user_role.equals("leader")) {
			return "redirect:/main.do";
		}
        CarTradeVO carTradeVO = new CarTradeVO();
        
 

        try {
            List<CarTradeVO> list = carTradeService.selectCarTradeList(carTradeVO);
            result.put("status", "success");
            result.put("data", list);
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
        }

        return "frozen/cartrade/list";
    }
    
    @RequestMapping(value = "/alist.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectCarTradeList(HttpServletRequest request, HttpServletResponse response, @RequestBody CarTradeVO carTradeVO) {
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


        try {
            List<CarTradeVO> list = carTradeService.selectCarTradeList(carTradeVO);
            result.put("status", "success");
            result.put("data", list);
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
        }

        return result;
    }


    // 거래 등록
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertCarTrade(HttpServletRequest request, HttpServletResponse response, @RequestBody CarTradeVO carTradeVO) {
    	Map<String, Object> result = new HashMap<>();

    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
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

        try {
            boolean success = carTradeService.insertCarTrade(carTradeVO);
            if (success) {
                result.put("status", "success");
            } else {
                result.put("status", "fail");
                result.put("message", "등록 실패");
            }
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
        }

        return result;
    }
    
    @RequestMapping(value = "/get_in_user.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getInGameUser(HttpServletRequest request, HttpServletResponse response, @RequestBody InGameUserVO userVO) {
    	Map<String, Object> result = new HashMap<>();

    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
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

        
        try {
        	InGameUserVO user = userService.getUserByIngameId(userVO);
        	if (user == null) {
        		result.put("status", "fail");
                result.put("message", "User Not Found");
                return result;
        	} else {
        		result.put("status", "success");
        		result.put("faction", user.getFaction());
        		result.put("nick", user.getName());
        		result.put("id", user.getId());
        		return result;
        	}
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
            return result;
        }
    }
    
    @RequestMapping(value = "/get_in_me.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getInGameMe(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String, Object> result = new HashMap<>();

    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
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


        String u_in_id = JwtUtil.getInGameIdFromToken(verify);
        
        InGameUserVO userVO = new InGameUserVO();

        userVO.setId(Integer.parseInt(u_in_id));

        try {
        	InGameUserVO user = userService.getUserByIngameId(userVO);
        	if (user == null) {
        		result.put("status", "fail");
                result.put("message", "User Not Found");
                return result;
        	} else {
        		result.put("status", "success");
        		result.put("faction", user.getFaction());
        		result.put("nick", user.getName());
        		result.put("id", user.getId());
        		return result;
        	}
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
            return result;
        }
    }
    
    // 거래 수정
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateCarTrade(HttpServletRequest request, HttpServletResponse response, @RequestBody CarTradeVO carTradeVO) {
    	Map<String, Object> result = new HashMap<>();

    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
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


        try {
        	String userId = getUserIdFromToken(request);
            carTradeVO.setRequester(userId);
            boolean success = carTradeService.updateCarTrade(carTradeVO);
            if (success) {
                result.put("status", "success");
            } else {
                result.put("status", "fail");
                result.put("message", "수정 실패");
            }
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
        }

        return result;
    }

    // 거래 삭제
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteCarTrade(HttpServletRequest request,HttpServletResponse response,  @RequestBody CarTradeVO carTradeVO) {
    	Map<String, Object> result = new HashMap<>();

    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
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

        try {
            boolean success = carTradeService.deleteCarTrade(carTradeVO);
            if (success) {
                result.put("status", "success");
            } else {
                result.put("status", "fail");
                result.put("message", "삭제 실패");
            }
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", e.getMessage());
        }

        return result;
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
