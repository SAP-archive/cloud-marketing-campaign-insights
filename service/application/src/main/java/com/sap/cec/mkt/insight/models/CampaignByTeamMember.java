package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class CampaignByTeamMember {

    @ElementName("CampaignUUID")
    @JsonProperty("CampaignUUID")
    private String campaignUUID;

    @ElementName("CampaignID")
    @JsonProperty("CampaignID")
    private String campaignID;

    @ElementName("CampaignName")
    @JsonProperty("CampaignName")
    private String campaignName;

    @ElementName("CampaignTeamMember")
    @JsonProperty("CampaignTeamMember")
    private String campaignTeamMember;

    @ElementName("CampaignExecutionFrqcyInterval")
    @JsonProperty("CampaignExecutionFrqcyInterval")
    private String campaignExecutionFrqcyInterval;

    @ElementName("CampaignLifecycleStatus")
    @JsonProperty("CampaignLifecycleStatus")
    private String campaignLifecycleStatus;

    @ElementName("CampaignLifecycleStatusName")
    @JsonProperty("CampaignLifecycleStatusName")
    private String campaignLifecycleStatusName;

    @ElementName("CampaignDescription")
    @JsonProperty("CampaignDescription")
    private String campaignDescription;

    @ElementName("CampaignStartDate")
    @JsonProperty("CampaignStartDate")
    private String campaignStartDate;

    @ElementName("CampaignEndDate")
    @JsonProperty("CampaignEndDate")
    private String campaignEndDate;

    @ElementName("CampaignOwner")
    @JsonProperty("CampaignOwner")
    private String campaignOwner;

    @ElementName("CampaignOwnerName")
    @JsonProperty("CampaignOwnerName")
    private String campaignOwnerName;

    @ElementName("CampaignCategory")
    @JsonProperty("CampaignCategory")
    private String campaignCategory;

    @ElementName("CampaignCategoryName")
    @JsonProperty("CampaignCategoryName")
    private String campaignCategoryName;

    @ElementName("MarketingAreaName")
    @JsonProperty("MarketingAreaName")
    private String marketingAreaName;

}
