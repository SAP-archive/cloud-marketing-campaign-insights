package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class UserDetails {

    @ElementName("UserID")
    @JsonProperty("UserID")
    private String userID;

    @ElementName("FirstName")
    @JsonProperty("FirstName")
    private String firstName;

    @ElementName("LastName")
    @JsonProperty("LastName")
    private String lastName;

    public void setUserId(String userID) {
	this.userID = userID;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }
}
