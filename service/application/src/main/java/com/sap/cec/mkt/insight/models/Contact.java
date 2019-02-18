package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class Contact {

    @ElementName("InteractionContactUUID")
    @JsonProperty("InteractionContactUUID")
    private String interactionContactUUID;

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
}
