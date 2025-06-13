package egovframework.com.fivemlist.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import egovframework.com.fivemlist.service.UserService;
import egovframework.com.fivemlist.service.UserVO;

public class JwtUtil {
    private static final String SECRET = "vT7#kP9@xM2dL6$gZf8qRw!eN1sJtC0B\r\n"+ ""; // 최소 32바이트
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long ACCESS_EXPIRATION = 1000L * 60 * 10; // 10분
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7일
    
    @Resource(name = "userService") 
	private static UserService service;

    public static String generateAccessToken(UserVO user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("ingame_id", user.getIngameNum())
                .claim("profile_image", user.getProfileImage())
                .claim("nickname", user.getNickname())
                .claim("email", user.getEmail())
                .claim("faction", user.getFaction().toString())
                .claim("type", user.getUserType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static Key getKey() {
        return key;
    }

    /**
     * 🔄 refresh_token을 통해 새로운 access_token 발급
     */
    public static Map<String, String> refreshAccessToken(HttpServletRequest request) {
        try {
        	Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            String baccessToken = null;
            
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh_token".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                    }
                    
                    if ("access_token".equals(cookie.getName())) {
                        baccessToken = cookie.getValue();
                    }
                }
            }
            Claims acclaims = null;
            // 토큰 검증 및 파싱
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                                            .setSigningKey(key)
                                            .build()
                                            .parseClaimsJws(baccessToken);
                acclaims = claimsJws.getBody();
                // 정상 토큰 처리
            } catch (ExpiredJwtException e) {
            	acclaims = e.getClaims();
                // 만료된 토큰에서 정보 꺼내기
                // 예를 들어 새로운 access token 재발급할 때 사용
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 만료 확인 (추가 확인 생략 가능: parseClaimsJws에서 이미 만료되면 예외 발생)
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
            	System.out.println("isnull");
                return null; // 만료됨
            }
            
            
            System.out.println(acclaims.get("type", String.class));
            
            String userId = claims.getSubject();
            // access_token만 새로 생성 (refresh_token은 그대로)
            UserVO dummyUser = new UserVO(); // 최소 정보만 채움 (원래는 DB에서 조회하는 게 안전)
            dummyUser.setId(userId);
            dummyUser.setEmail(acclaims.get("email", String.class));
            dummyUser.setIngameNum(acclaims.get("ingame_id", String.class));
            dummyUser.setProfileImage(acclaims.get("profile_image", String.class));
            dummyUser.setNickname(acclaims.get("nickname", String.class));
            dummyUser.setFaction(Integer.parseInt(acclaims.get("faction", String.class)));
            dummyUser.setUserType(acclaims.get("type", String.class));

            
            System.out.println("arefresh_token : "+refreshToken);
            String newAccessToken = generateAccessToken(dummyUser);
            System.out.println("brefresh_token : "+refreshToken +"accessToken : "+newAccessToken);

            Map<String, String> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("status", "success");
            return result;

        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return null; // 잘못된 토큰
        }
    }
    
    /**
     * 토큰을 검증하고 유효하면 사용자 ID를 반환, 아니면 null 반환
     */
    public static String verifyToken(HttpServletRequest request) {
    	Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        String baccessToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
                
                if ("access_token".equals(cookie.getName())) {
                    baccessToken = cookie.getValue();
                }
            }
        } 
        if (baccessToken == null) {
        	return "null";
        }
        
       try { Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(baccessToken);
       return baccessToken;
        
    } catch (ExpiredJwtException e) {
    	return "expired";
        // 만료된 토큰에서 정보 꺼내기
        // 예를 들어 새로운 access token 재발급할 때 사용
    }
    }
    
    public static String validateAndGetUserId(String token) {
        try {
        	Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 토큰 만료 검사 (옵션)
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                return null;
            }

            return claims.getSubject(); // 일반적으로 사용자 ID가 subject에 들어감
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
    
	// 토큰에서 팩션 정보를 추출하는 메소드 예시
	public static String getFactionFromToken(HttpServletRequest request) throws Exception {
	    String token = getAccessTokenFromRequest(request); // 엑세스 토큰을 추출하는 메소드
	    if (token != null) {
	        // 토큰에서 필요한 정보를 디코딩하거나 파싱하는 로직을 추가
	        // 예시로, JWT 토큰을 디코딩하여 팩션 정보를 추출할 수 있습니다.
	        // 아래는 JWT에서 팩션 정보를 추출하는 예시입니다.
	        try {
	        	Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(key)
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	            return claims.get("faction", String.class); // 토큰에서 'faction' 정보 가져오기
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	public static String getTypeFromToken(String token) throws Exception { // 엑세스 토큰을 추출하는 메소드
	    if (token != null) {
	        // 토큰에서 필요한 정보를 디코딩하거나 파싱하는 로직을 추가
	        // 예시로, JWT 토큰을 디코딩하여 팩션 정보를 추출할 수 있습니다.
	        // 아래는 JWT에서 팩션 정보를 추출하는 예시입니다.
	        try {
	        	Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(key)
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	            return claims.get("type", String.class); // 토큰에서 'faction' 정보 가져오기
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	public static String getInGameIdFromToken(String token) throws Exception { // 엑세스 토큰을 추출하는 메소드
	    if (token != null) {
	        // 토큰에서 필요한 정보를 디코딩하거나 파싱하는 로직을 추가
	        // 예시로, JWT 토큰을 디코딩하여 팩션 정보를 추출할 수 있습니다.
	        // 아래는 JWT에서 팩션 정보를 추출하는 예시입니다.
	        try {
	        	Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(key)
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	            return claims.get("ingame_id", String.class); // 토큰에서 'faction' 정보 가져오기
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	private static String getAccessTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
}
