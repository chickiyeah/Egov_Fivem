package egovframework.com.fivemlist.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarTradeVO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("requestFaction")
    private String requestFaction;

    @JsonProperty("requester")
    private String requester;

    @JsonProperty("receiveFaction")
    private String receiveFaction;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("car")
    private String car;

    @JsonProperty("createdAt")
    private String createdAt;

    // ====== 기본 생성자 ======
    public CarTradeVO() {
    }

    // ====== 전체 필드 생성자 ======
    public CarTradeVO(int id, String requestFaction, String requester, String receiveFaction, String receiver, String car, String createdAt) {
        this.id = id;
        this.requestFaction = requestFaction;
        this.requester = requester;
        this.receiveFaction = receiveFaction;
        this.receiver = receiver;
        this.car = car;
        this.createdAt = createdAt;
    }

    // ====== Getter & Setter ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestFaction() {
        return requestFaction;
    }

    public void setRequestFaction(String requestFaction) {
        this.requestFaction = requestFaction;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getReceiveFaction() {
        return receiveFaction;
    }

    public void setReceiveFaction(String receiveFaction) {
        this.receiveFaction = receiveFaction;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
