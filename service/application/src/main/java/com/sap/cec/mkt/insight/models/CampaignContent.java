package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class CampaignContent {

    @ElementName("MessageUUID")
    @JsonProperty("MessageUUID")
    private String messageUUID;

    @ElementName("LanguageCode")
    @JsonProperty("LanguageCode")
    private String languageCode;

    @ElementName("LanguageName")
    @JsonProperty("LanguageName")
    private String languageName;

    @ElementName("MessageContentHTMLString")
    @JsonProperty("MessageContentHTMLString")
    private String messageContentHTMLString;

    public void setMessageUUID(String messageUUID) {
	this.messageUUID = messageUUID;
    }

    public String getMessageUUID() {
	return messageUUID;
    }
}
