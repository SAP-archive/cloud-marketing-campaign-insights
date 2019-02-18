package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class InteractionContact {

    @ElementName("InteractionContactUUID")
    @JsonProperty("InteractionContactUUID")
    private String interactionContactUUID;

    @ElementName("InteractionContactId")
    @JsonProperty("InteractionContactId")
    private String interactionContactId;

    @ElementName("InteractionContactOrigin")
    @JsonProperty("InteractionContactOrigin")
    private String interactionContactOrigin;

    @ElementName("FullName")
    @JsonProperty("FullName")
    private String fullName;

    @ElementName("EmailAddress")
    @JsonProperty("EmailAddress")
    private String emailAddress;

    @ElementName("PhoneNumber")
    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @ElementName("SalesRepresentativeFullName")
    @JsonProperty("SalesRepresentativeFullName")
    private String salesRepresentativeFullName;

    public String getSalesRepresentativeFullName() {
	return salesRepresentativeFullName;
    }

    public void setSalesRepresentativeFullName(String salesRepresentativeFullName) {
	this.salesRepresentativeFullName = salesRepresentativeFullName;
    }

    public void setInteractionContactUUID(String interactionContactUUID) {
	this.interactionContactUUID = interactionContactUUID;
    }

    public String getInteractionContactUUID() {
	return interactionContactUUID;
    }

    public void setInteractionContactId(String interactionContactId) {
	this.interactionContactId = interactionContactId;
    }

    public String getInteractionContactId() {
	return interactionContactId;
    }

    public void setInteractionContactOrigin(String interactionContactOrigin) {
	this.interactionContactOrigin = interactionContactOrigin;
    }

    public String getInteractionContactOrigin() {
	return interactionContactOrigin;
    }
}
