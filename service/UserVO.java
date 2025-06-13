package egovframework.com.fivemlist.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserVO implements Serializable {

    private int no; // 계정 번호

    @JsonProperty("id")
    private String id; // 계정 아이디

    @JsonProperty("pwd")
    private String pwd; // 계정 비밀번호

    private String hashPwd; // 암호화된 비밀번호

    @JsonProperty("email")
    private String email; // 이메일

    @JsonProperty("nickname")
    private String nickname; // 닉네임

    @JsonProperty("ingame_num")
    private String ingameNum; // 고유번호

    @JsonProperty("faction")
    private Integer faction; // 소속 팩션

    private String profileImage; // 프로필 이미지

    private String userType; // 유저 타입
    
    private String verifyKey; // 고유번호 인증코드

    // Getter & Setter
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHashPwd() {
        return hashPwd;
    }

    public void setHashPwd(String hashPwd) {
        this.hashPwd = hashPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIngameNum() {
        return ingameNum;
    }

    public void setIngameNum(String ingameNum) {
        this.ingameNum = ingameNum;
    }

    public Integer getFaction() {
        return faction;
    }

    public void setFaction(Integer faction) {
        this.faction = faction;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public String getVerifyKey() {
		return verifyKey;
	}

	public void setVerifyKey(String verifyKey) {
		this.verifyKey = verifyKey;
	}
}
