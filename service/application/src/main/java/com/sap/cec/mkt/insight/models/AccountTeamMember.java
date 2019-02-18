package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class AccountTeamMember {

    @ElementName("TeamMemberUUID")
    @JsonProperty("TeamMemberUUID")
    private String teamMemberUUID;

    @ElementName("ContactUUID")
    @JsonProperty("ContactUUID")
    private String contactUUID;

    @ElementName("TeamMemberID")
    @JsonProperty("TeamMemberID")
    private String teamMemberID;

    @ElementName("Role")
    @JsonProperty("Role")
    private String role;

    public String getTeamMemberUUID() {
	return teamMemberUUID;
    }

    public void setTeamMemberUUID(String teamMemberUUID) {
	this.teamMemberUUID = teamMemberUUID;
    }

    public String getContactUUID() {
	return contactUUID;
    }

    public void setContactUUID(String contactUUID) {
	this.contactUUID = contactUUID;
    }

    public String getTeamMemberID() {
	return teamMemberID;
    }

    public void setTeamMemberID(String teamMemberID) {
	this.teamMemberID = teamMemberID;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

}
