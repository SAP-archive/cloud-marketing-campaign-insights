package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class Interaction {

    @ElementName("CampaignID")
    @JsonProperty("CampaignID")
    private String campaignID;

    @ElementName("CampaignContent")
    @JsonProperty("CampaignContent")
    private String campaignContent;

    public void setCampaignContent(String campaignContent) {
	this.campaignContent = campaignContent;
    }

    public String getCampaignContent() {
	return campaignContent;
    }
}
