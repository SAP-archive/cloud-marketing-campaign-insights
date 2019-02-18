package com.sap.cec.mkt.insight.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.cloud.sdk.result.ElementName;

public class Metrics {

    @ElementName("Campaign")
    @JsonProperty("CampaignUUID")
    private String campaignUUID;

    @ElementName("CampaignID")
    @JsonProperty("CampaignID")
    private String campaignID;

    @ElementName("NmbrOfMisgMarketingPermissions")
    @JsonProperty("NumberOfMissingMarketingPermissions")
    private double numberOfMissingMarketingPermissions;

    @ElementName("NumberOfMissingCommData")
    @JsonProperty("NumberOfMissingCommData")
    private double numberOfMissingCommData;

    @ElementName("NumberOfLimitReached")
    @JsonProperty("NumberOfCommLimitReached")
    private double numberOfCommLimitReached;

    @ElementName("NumberOfSentMessages")
    @JsonProperty("NumberOfSentMessages")
    private double numberOfSentMessages;

    @ElementName("NumberOfBounces")
    @JsonProperty("NumberOfBounces")
    private double numberOfBounces;

    @ElementName("NumberOfHardBounces")
    @JsonProperty("NumberOfHardBounces")
    private double numberOfHardBounces;

    @ElementName("NumberOfSoftBounces")
    @JsonProperty("NumberOfSoftBounces")
    private double numberOfSoftBounces;

    @ElementName("NumberOfUnopenedMessages")
    @JsonProperty("NumberOfUnopenedMessages")
    private double numberOfUnopenedMessages;

    @ElementName("NumberOfOpenedMessages")
    @JsonProperty("NumberOfOpenedMessages")
    private double numberOfOpenedMessages;

    @ElementName("NumberOfDeliveredMessages")
    @JsonProperty("NumberOfDeliveredMessages")
    private double numberOfDeliveredMessages;

    @ElementName("NumberOfDeliveredEmailMessages")
    @JsonProperty("NumberOfDeliveredEmailMessages")
    private double numberOfDeliveredEmailMessages;

    @ElementName("NumberOfUniqueClicks")
    @JsonProperty("NumberOfUniqueClicks")
    private double numberOfUniqueClicks;

    @ElementName("BounceRateInPercent")
    @JsonProperty("BounceRateInPercent")
    private double bounceRateInPercent;

    @ElementName("OpenedMessageRateInPercent")
    @JsonProperty("OpenedMessageRateInPercent")
    private double openedMessageRateInPercent;

    @ElementName("ClickToOpenRateInPercent")
    @JsonProperty("ClickToOpenRateInPercent")
    private double clickToOpenRateInPercent;

    @ElementName("UniqueClickRateInPercent")
    @JsonProperty("UniqueClickRateInPercent")
    private double uniqueClickRateInPercent;

    public void setNumberOfOpenedMessages(double numberOfOpenedMessages) {
	this.numberOfOpenedMessages = numberOfOpenedMessages;
    }

    public double getNumberOfOpenedMessages() {
	return numberOfOpenedMessages;
    }

    public void setNumberOfSentMessages(double numberOfSentMessages) {
	this.numberOfSentMessages = numberOfSentMessages;
    }

    public double getNumberOfSentMessages() {
	return numberOfSentMessages;
    }

    public void setNumberOfDeliveredMessages(double numberOfDeliveredMessages) {
	this.numberOfDeliveredMessages = numberOfDeliveredMessages;
    }

    public double getNumberOfDeliveredMessages() {
	return numberOfDeliveredMessages;
    }

    public void setNumberOfDeliveredEmailMessages(double numberOfDeliveredEmailMessages) {
	this.numberOfDeliveredEmailMessages = numberOfDeliveredEmailMessages;
    }

    public double getNumberOfDeliveredEmailMessages() {
	return numberOfDeliveredEmailMessages;
    }

    public void setNumberOfSoftBounces(double numberOfSoftBounces) {
	this.numberOfSoftBounces = numberOfSoftBounces;
    }

    public double getNumberOfSoftBounces() {
	return numberOfSoftBounces;
    }

    public void setNumberOfHardBounces(double numberOfHardBounces) {
	this.numberOfHardBounces = numberOfHardBounces;
    }

    public double getNumberOfHardBounces() {
	return numberOfHardBounces;
    }

    public void setNumberOfUniqueClicks(double numberOfUniqueClicks) {
	this.numberOfUniqueClicks = numberOfUniqueClicks;
    }

    public double getNumberOfUniqueClicks() {
	return numberOfUniqueClicks;
    }

    public void setBounceRateInPercent(double bounceRateInPercent) {
	this.bounceRateInPercent = bounceRateInPercent;
    }

    public double getBounceRateInPercent() {
	return bounceRateInPercent;
    }

    public void setOpenedMessageRateInPercent(double openedMessageRateInPercent) {
	this.openedMessageRateInPercent = openedMessageRateInPercent;
    }

    public double getOpenedMessageRateInPercent() {
	return openedMessageRateInPercent;
    }

    public void setClickToOpenRateInPercent(double clickToOpenRateInPercent) {
	this.clickToOpenRateInPercent = clickToOpenRateInPercent;
    }

    public double getClickToOpenRateInPercent() {
	return clickToOpenRateInPercent;
    }

    public void setUniqueClickRateInPercent(double uniqueClickRateInPercent) {
	this.uniqueClickRateInPercent = uniqueClickRateInPercent;
    }

    public double getUniqueClickRateInPercent() {
	return uniqueClickRateInPercent;
    }
}
