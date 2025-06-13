package egovframework.com.fivemlist.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.Flist_ServersService;
import egovframework.com.fivemlist.service.ServerVO;

@Controller
public class MainController {
	
	@Resource(name = "Flist_ServersService")
	private Flist_ServersService flistser;
	 
    private String getUserIdFromToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return JwtUtil.validateAndGetUserId(cookie.getValue());
                }
            }
        }
        return null;
    }
    
    
	@RequestMapping(value = "/main.do")
	public String frozenhello(HttpServletRequest request, HttpServletResponse response,  Model model) throws Exception {
		
        List<ServerVO> servers = flistser.getServers();
    	Map<String, Object> result = new HashMap<>();
    	String verify = JwtUtil.verifyToken(request);
        if (verify == "expired") {
       	 // refresh_token을 이용해 새로운 access_token 발급 (서비스에서 구현)
	        Map<String, String> tokens = JwtUtil.refreshAccessToken(request);
	        
	        if (tokens == null) {
	            // refresh_token이 유효하지 않거나 만료된 경우
	        	result.put("status", "fail");
	            result.put("message", "Permission Denied");
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
        
        
        
        if (verify != "null") {
	        String type = JwtUtil.getTypeFromToken(verify);
	        model.addAttribute("u_type", type);     
        }
        
        model.addAttribute("servers", servers);
        
        
		
		return "frozen/index";
	}
}
