package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class TeamMember {

    @ElementName("TeamMemberName")
    @JsonProperty("TeamMemberName")
    private String teamMemberName;

    @ElementName("CampaignUUID")
    @JsonProperty("CampaignUUID")
    private String campaignUUID;

    @ElementName("FullName")
    @JsonProperty("FullName")
    private String fullName;

    @ElementName("PhoneNumber")
    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @ElementName("EmailAddress")
    @JsonProperty("EmailAddress")
    private String emailAddress;

}
