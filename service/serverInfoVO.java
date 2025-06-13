package egovframework.com.fivemlist.service;

import java.util.List;
import java.util.Map;

public class serverInfoVO {
    private boolean enhancedHostSupport;
    private String icon;
    private String requestSteamTicket;
    private List<String> resources;
    private String server;
    private Map<String, String> vars;
    private long version;

    // Getters & Setters
    public boolean isEnhancedHostSupport() {
        return enhancedHostSupport;
    }

    public void setEnhancedHostSupport(boolean enhancedHostSupport) {
        this.enhancedHostSupport = enhancedHostSupport;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}

