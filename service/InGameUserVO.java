package egovframework.com.fivemlist.service;

import java.util.Map;

public class InGameUserVO {

    private int id;
    private String name;
    private String lastLogin;
    private String lastLoginIp;
    private String lastLoginTime;
    private int whitelisted;
    private int banned;
    private String reason;
    private String dvalue; // JSON 데이터 (String으로 저장)
    private String faction;
    private String fullaction;
    private Map<String, Boolean> groups; // groups는 Map 형태로 저장됨

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getWhitelisted() {
        return whitelisted;
    }

    public void setWhitelisted(int whitelisted) {
        this.whitelisted = whitelisted;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", whitelisted=" + whitelisted +
                ", banned=" + banned +
                ", reason='" + reason + '\'' +
                ", dvalue='" + dvalue + '\'' +
                ", faction='" + faction + '\'' +
                ", groups=" + groups +
                '}';
    }

	public String getFullaction() {
		return fullaction;
	}

	public void setFullaction(String fullaction) {
		this.fullaction = fullaction;
	}
}
