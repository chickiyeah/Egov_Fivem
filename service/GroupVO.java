package egovframework.com.fivemlist.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupVO {

    @JsonProperty("no")
    private Integer No;

    @JsonProperty("s_name")
    private String S_Name;

    @JsonProperty("f_name")
    private String F_Name;

    // DB에서 직접 매핑될 필드
    private String Members;

    private String Mgrs;

    private List<Integer> MembersList;

    private List<Integer> MgrsList;

    @JsonProperty("owner")
    private Integer Owner;

    @JsonProperty("type")
    private String Type;
    
    private String limit;
    private String offset;

    @JsonProperty("description")
    private String Description;
    
    private String image;

    // --- Getter / Setter ---
    public Integer getNo() {
        return No;
    }

    public void setNo(Integer no) {
        this.No = no;
    }

    public String getS_Name() {
        return S_Name;
    }

    public void setS_Name(String s_Name) {
        this.S_Name = s_Name;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        this.F_Name = f_Name;
    }

    public String getMembers() {
        return Members;
    }

    public void setMembers(String members) {
        this.Members = members;
        this.MembersList = parseList(members);
    }

    public List<Integer> getMembersList() {
        return MembersList;
    }

    public void setMembersList(List<Integer> membersList) {
        this.MembersList = membersList;
        this.Members = toStringValue(membersList);
    }

    public String getMgrs() {
        return Mgrs;
    }

    public void setMgrs(String mgrs) {
        this.Mgrs = mgrs;
        this.MgrsList = parseList(mgrs);
    }

    public List<Integer> getMgrsList() {
        return MgrsList;
    }

    public void setMgrsList(List<Integer> mgrsList) {
        this.MgrsList = mgrsList;
        this.Mgrs = toStringValue(mgrsList);
    }

    public Integer getOwner() {
        return Owner;
    }

    public void setOwner(Integer owner) {
        this.Owner = owner;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    // --- 변환 유틸 ---
    private List<Integer> parseList(String csv) {
        if (csv == null || csv.isEmpty()) return new ArrayList<>();
        return Arrays.stream(csv.split(","))
                     .map(String::trim)
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    private String toStringValue(List<Integer> list) {
        if (list == null || list.isEmpty()) return "";
        return list.stream()
                   .map(String::valueOf)
                   .collect(Collectors.joining(","));
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
}
