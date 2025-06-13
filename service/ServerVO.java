package egovframework.com.fivemlist.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import egovframework.com.fivemlist.service.serverdataVO;
public class ServerVO {
		@JsonProperty("EndPoint")
	    private String endpoint;
	    private int like;
	    private int sponser;
	    @JsonProperty("Data")
	    private serverdataVO data;
	    @JsonProperty("direct_address")
	    private String directAddress;
	    private String error;
	    private serverInfoVO info;

		public int getLike() {
			return like;
		}
		public void setLike(int like) {
			this.like = like;
		}
		public int getSponser() {
			return sponser;
		}
		public void setSponser(int sponser) {
			this.sponser = sponser;
		}
		public serverdataVO getData() {
			return data;
		}
		public void setData(serverdataVO data) {
			this.data = data;
		}
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}
		public String getDirectAddress() {
			return directAddress;
		}
		public void setDirectAddress(String directAddress) {
			this.directAddress = directAddress;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public serverInfoVO getInfo() {
			return info;
		}
		public void setInfo(serverInfoVO info) {
			this.info = info;
		}

	    // Getter / Setter
	



	



}
