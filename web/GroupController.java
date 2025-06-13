package egovframework.com.fivemlist.web;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import egovframework.com.fivemlist.jwt.JwtUtil;
import egovframework.com.fivemlist.service.GroupService;
import egovframework.com.fivemlist.service.GroupVO;
import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;

@Controller
public class GroupController {

    @Resource(name = "GroupService")
    private GroupService groupService;
    
    @RequestMapping(value = "/faction/list.do")
    public String factionlist(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        // 1. 사용자 인증 및 팩션 정보 추출
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
        
        String utype = JwtUtil.getTypeFromToken(verify);
        model.addAttribute("u_type", utype);
        


        // 토큰에서 팩션 정보 추출 (엑세스 토큰에 팩션 정보가 있을 경우)
        String factionData = JwtUtil.getFactionFromToken(request); // 토큰에서 팩션 정보 추출하는 메소드
        List<Integer> joinedIds = new ArrayList<>();
        if (factionData != null && !factionData.isEmpty()) {
            // 팩션 정보 파싱
            joinedIds = Arrays.stream(factionData.split(","))
                              .map(String::trim)
                              .map(Integer::parseInt)
                              .collect(Collectors.toList());
        }

        // 2. 파라미터 처리
        String type = request.getParameter("type");
        String userId = JwtUtil.getUserIdFromToken(verify);
        if (type == null || (!type.equals("public") && !type.equals("private") && !type.equals("joined") && !type.equals("owned"))) {
            type = "Public";
        }

        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr != null && pageStr.matches("\\d+")) {
            page = Integer.parseInt(pageStr);
        }

        int limit = 5;
        int offset = (page - 1) * limit;

        // 3. 리스트 조회
        Map<String, Object> param = new HashMap<>();
        param.put("limit", limit);
        param.put("offset", offset);
        param.put("Type", type);

        if (type.equals("joined")) {
            // joined 타입은 유저가 가입한 팩션에 맞춰 필터링
            param.put("joinedIds", joinedIds);
            List<GroupVO> joinedList = groupService.getFactionListPaged(param);
            model.addAttribute("joinedList", joinedList);
        } else if (type.equals("owned")) {
            // owned 타입은 유저가 소유한 팩션을 조회
            param.put("ownerId", userId);
            List<GroupVO> ownedList = groupService.getFactionListPaged(param);
            model.addAttribute("ownedList", ownedList);
        } else {
            // public/private 타입
             // public / private
            List<GroupVO> publicList = groupService.getFactionListPaged(param);
            model.addAttribute("publicList", publicList);
        }

        // 페이징 관련 정보도 모델에 추가
        model.addAttribute("currentPage", page);
        model.addAttribute("type", type);

        return "frozen/group/list";
    }




	
	@RequestMapping(value = "/faction/create.do")
	public String createfaction(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
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

        String type = JwtUtil.getTypeFromToken(verify);
        model.addAttribute("u_type", type);
        
		return "frozen/group/create";
	}
	
	public static boolean isNumber(String number) {

		boolean flag = true;
		if (number == null || "".equals(number)) {
			return false;
		}

		int size = number.length();
		int st_no = 0;

		// 45(-)음수여부 확인, 음수이면 시작위치를 1부터 시작
		if (number.charAt(0) == 45) {
			st_no = 1;
		}

		// 48(0)~57(9)가 아니면 false
		for (int i = st_no; i < size; ++i) {
			if (!(48 <= ((int) number.charAt(i)) && 57 >= ((int) number.charAt(i)))) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	
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

    private boolean hasPermission(String userId, GroupVO group) {
        if (group == null) return false;
        if (group.getOwner().equals(userId)) return true;

        List<String> mgrs = Arrays.asList(group.getMgrs().split(","));
        List<String> members = Arrays.asList(group.getMembers().split(","));
        return mgrs.contains(userId) || members.contains(userId);
    }

    @PostMapping("/api/faction/create.do")
    @ResponseBody
    public Object createFaction(@RequestBody GroupVO vo, HttpServletRequest request) {
        String uid = getUserIdFromToken(request);
        
        if (uid == null) {
        	System.out.println("a_ fail");
            return Map.of("status", "fail", "code", "NOTLOGIN", "message", "Not have permission");
        }
        System.out.println("b_ fail");
        vo.setOwner(Integer.parseInt(uid));
        boolean result = groupService.insertFaction(vo);
        return Map.of("status", result ? "success" : "fail");
    }

    @PostMapping("/api/faction/update.do")
    @ResponseBody
    public Object updateFaction(@RequestBody GroupVO vo, HttpServletRequest request) {
        String uid = getUserIdFromToken(request);
        GroupVO origin = groupService.getFaction(vo.getNo());
        if (uid == null || !hasPermission(uid, origin)) {
            return Map.of("status", "fail", "message", "Not have permission");
        }
        boolean result = groupService.updateFaction(vo);
        return Map.of("status", result ? "success" : "fail");
    }

    @PostMapping("/api/faction/delete.do")
    @ResponseBody
    public Object deleteFaction(@RequestBody Map<String, Integer> data, HttpServletRequest request) {
        String uid = getUserIdFromToken(request);
        int no = data.get("no");
        GroupVO group = groupService.getFaction(no);
        if (uid == null || !hasPermission(uid, group)) {
            return Map.of("status", "fail", "message", "Not have permission");
        }
        boolean result = groupService.deleteFaction(no);
        return Map.of("status", result ? "success" : "fail");
    }

    @PostMapping("/api/faction/list.do")
    @ResponseBody
    public List<GroupVO> listFactions(@RequestBody Map<String, Object> params) {
        return groupService.getFactionListPaged(params);
    }

    @PostMapping("/api/faction/search.do")
    @ResponseBody
    public List<GroupVO> searchFactions(@RequestBody Map<String, Object> params) {
        return groupService.searchFactions(params);
    }

    @PostMapping("/api/faction/count.do")
    @ResponseBody
    public int getFactionCount(@RequestBody Map<String, Object> params) {
        return groupService.getFactionCount(params);
    }

    @PostMapping("/api/faction/get.do")
    @ResponseBody
    public GroupVO getFaction(@RequestBody Map<String, Integer> data) {
        return groupService.getFaction(data.get("no"));
    }
}
