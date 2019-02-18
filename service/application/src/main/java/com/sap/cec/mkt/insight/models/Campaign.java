package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity;

public class Campaign extends VdmEntity<Campaign> {

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

    @ElementName("AssignedTargetGroupMemberCount")
    @JsonProperty("AssignedTargetGroupMemberCount")
    private String assignedTargetGroupMemberCount;

    public String getCampaignID() {
	return campaignID;
    }

    public void setCampaignID(String campaignID) {
	this.campaignID = campaignID;
    }

    public String getAssignedTargetGroupMemberCount() {
	return assignedTargetGroupMemberCount;
    }

    public void setAssignedTargetGroupMemberCount(String assignedTargetGroupMemberCount) {
	this.assignedTargetGroupMemberCount = assignedTargetGroupMemberCount;
    }

    @Override
    public Class<Campaign> getType() {
	return Campaign.class;
    }

    @Override
    protected String getEndpointUrl() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getEntityCollection() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected void setErpConfigContext(ErpConfigContext arg0) {
	// TODO Auto-generated method stub

    }

}
