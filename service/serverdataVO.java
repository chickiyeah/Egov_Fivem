package egovframework.com.fivemlist.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import egovframework.com.fivemlist.service.serverPlayerVO;

public class serverdataVO {

    private int clients;
    private int selfReportedClients;
    private String gametype;
    private String hostname;
    private String mapname;
    private int sv_maxclients;
    private int svMaxclients;
    private boolean enhancedHostSupport;
    private String requestSteamTicket;
    private List<String> resources;
    private String server;
    private Map<String, String> vars;
    private List<serverPlayerVO> players;

    private long ownerID;
    private boolean fallback;
    private boolean privateServer;

    private List<String> connectEndPoints;
    private int upvotePower;
    private int burstPower;
    private String support_status;

    private String ownerName;
    private String ownerProfile;
    private String suspendedTill;
    private String ownerAvatar;
    private String lastSeen;
    private long iconVersion;

    // Custom key mapping
    @JsonProperty("private")
    public boolean isPrivateServer() {
        return privateServer;
    }

    @JsonProperty("private")
    public void setPrivateServer(boolean privateServer) {
        this.privateServer = privateServer;
    }

	public int getClients() {
		return clients;
	}

	public void setClients(int clients) {
		this.clients = clients;
	}

	public int getSelfReportedClients() {
		return selfReportedClients;
	}

	public void setSelfReportedClients(int selfReportedClients) {
		this.selfReportedClients = selfReportedClients;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public int getSvMaxclients() {
		return svMaxclients;
	}

	public void setSvMaxclients(int svMaxclients) {
		this.svMaxclients = svMaxclients;
	}

	public boolean isEnhancedHostSupport() {
		return enhancedHostSupport;
	}

	public void setEnhancedHostSupport(boolean enhancedHostSupport) {
		this.enhancedHostSupport = enhancedHostSupport;
	}

	public String getRequestSteamTicket() {
		return requestSteamTicket;
	}

	public void setRequestSteamTicket(String requestSteamTicket) {
		this.requestSteamTicket = requestSteamTicket;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Map<String, String> getVars() {
		return vars;
	}

	public void setVars(Map<String, String> vars) {
		this.vars = vars;
	}

	public List<serverPlayerVO> getPlayers() {
		return players;
	}

	public void setPlayers(List<serverPlayerVO> players) {
		this.players = players;
	}

	public long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(long ownerID) {
		this.ownerID = ownerID;
	}

	public boolean isFallback() {
		return fallback;
	}

	public void setFallback(boolean fallback) {
		this.fallback = fallback;
	}

	public List<String> getConnectEndPoints() {
		return connectEndPoints;
	}

	public void setConnectEndPoints(List<String> connectEndPoints) {
		this.connectEndPoints = connectEndPoints;
	}

	public int getUpvotePower() {
		return upvotePower;
	}

	public void setUpvotePower(int upvotePower) {
		this.upvotePower = upvotePower;
	}

	public int getBurstPower() {
		return burstPower;
	}

	public void setBurstPower(int burstPower) {
		this.burstPower = burstPower;
	}

	public String getSupport_status() {
		return support_status;
	}

	public void setSupport_status(String support_status) {
		this.support_status = support_status;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerProfile() {
		return ownerProfile;
	}

	public void setOwnerProfile(String ownerProfile) {
		this.ownerProfile = ownerProfile;
	}

	public String getSuspendedTill() {
		return suspendedTill;
	}

	public void setSuspendedTill(String suspendedTill) {
		this.suspendedTill = suspendedTill;
	}

	public String getOwnerAvatar() {
		return ownerAvatar;
	}

	public void setOwnerAvatar(String ownerAvatar) {
		this.ownerAvatar = ownerAvatar;
	}

	public String getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(String lastSeen) {
		this.lastSeen = lastSeen;
	}

	public long getIconVersion() {
		return iconVersion;
	}

	public void setIconVersion(long iconVersion) {
		this.iconVersion = iconVersion;
	}

	public int getSv_maxclients() {
		return sv_maxclients;
	}

	public void setSv_maxclients(int sv_maxclients) {
		this.sv_maxclients = sv_maxclients;
	}


	    // Getter / Setter
	
}
