package com.sap.cec.mkt.insight.dataprovider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cec.mkt.insight.models.Campaign;
import com.sap.cec.mkt.insight.models.CampaignByTeamMember;
import com.sap.cec.mkt.insight.models.CampaignContent;
import com.sap.cec.mkt.insight.models.Interaction;
import com.sap.cec.mkt.insight.models.InteractionContact;
import com.sap.cec.mkt.insight.models.Metrics;
import com.sap.cec.mkt.insight.models.TeamMember;
import com.sap.cec.mkt.insight.util.Constants;
import com.sap.cec.mkt.insight.util.QueryUtil;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryResult;
import com.sap.cloud.sdk.result.ResultElement;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;

/** Campaign data provider class **/
public class CampaignDataProvider {

    private static final Logger log = LoggerFactory.getLogger(CampaignDataProvider.class);

    // private static final String filterQuery = "(CampaignTeamMember eq
    // 'CB9980000125')";

    private static final String sortquery = "CampaignLifecycleStatus asc,CampaignStartDate desc";

    private CampaignDataProvider() {

    }

    /** Get Campaigns of LoggedIn user **/
    public static List<CampaignByTeamMember> getCampaignsByTeamMember(QueryRequest queryRequest) throws ODataException {
	ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(queryRequest, Constants.CAMPAIGN_SERVICE_PATH,
		"CampaignsByTeamMember", null, sortquery, true);
	return queryBuilder.build().execute(Constants.DESTINATION_NAME).asList(CampaignByTeamMember.class);
    }

    /** Get Campaigns **/
    public static List<CampaignByTeamMember> getCampaigns(QueryRequest queryRequest) throws ODataException {
	ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(queryRequest, Constants.CAMPAIGN_SERVICE_PATH,
		"Campaigns", null, null, true);

	return queryBuilder.build().execute(Constants.DESTINATION_NAME).asList(CampaignByTeamMember.class);
    }

    /** Get Campaign by ID **/
    @SuppressWarnings("unchecked")
    public static Map<String, String> getCampaign(ReadRequest readRequest) throws ODataException {
	String campaignUuid = readRequest.getKeys().get("CampaignUUID").toString();
	log.debug("==> now execute read for CampaignUUID: " + campaignUuid);
	String contextUrl = "Campaigns(guid'" + campaignUuid + "')";
	ODataQueryResult result = ODataQueryBuilder.withEntity(Constants.CAMPAIGN_SERVICE_PATH, contextUrl).build()
		.execute(Constants.DESTINATION_NAME);

	log.debug("==> After calling backend OData V2 service: result: " + result);

	// Campaign campaign = result.as(Campaign.class);
	// Reading in a Map as custom fields are not part of pojo
	Map<String, String> campaign = result.as(Map.class);
	String targetGroupUuid = getTargetGroupUuid(campaignUuid);
	if (targetGroupUuid != null) {
	    String tgMemberCount = TargetGroupDataProvider.getTargetGroupMemberCount(null, targetGroupUuid);
	    campaign.put("AssignedTargetGroupMemberCount", tgMemberCount);
	}
	return campaign;
    }

    /** Get Team Members of a Campaign **/
    public static List<TeamMember> getCampaignTeamMembers(QueryRequest queryRequest) throws ODataException {
	String campaignUuid = queryRequest.getSourceKeys().get("CampaignUUID").toString();
	String contextUrl = "Campaigns(guid'" + campaignUuid + "')/CampaignAssignedTeamMembers";

	ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(queryRequest, Constants.CAMPAIGN_SERVICE_PATH,
		contextUrl, null, null, true);
	return queryBuilder.build().execute(Constants.DESTINATION_NAME).asList(TeamMember.class);
    }

    /** Get Contacts of a Campaign via Target Group **/
    public static List<InteractionContact> getCampaignContacts(QueryRequest queryRequest) throws ODataException {
	String campaignUuid = queryRequest.getSourceKeys().get("CampaignUUID").toString();

	String targetGroupUuid = getTargetGroupUuid(campaignUuid);

	return ContactDataProvider.getContacts(queryRequest, targetGroupUuid);
    }

    /** Get Campaign Metrics via Custom CDS view **/
    public static List<Metrics> getCampaignMetrics(QueryRequest queryRequest) throws ODataException {
	String campaignUuid = queryRequest.getSourceKeys().get("CampaignUUID").toString();
	String campaignId = getCampaignId(campaignUuid);
	Destination destination = DestinationAccessor.getDestination("mkt-ba");
	String customViewName = destination.getPropertiesByName().get("CustomViewName");
	String entity = customViewName.replace("_CDS", "");
	String startDate = destination.getPropertiesByName().get("P_StartDate");
	String endDate = destination.getPropertiesByName().get("P_EndDate");
	String path = "/sap/opu/odata/sap/" + customViewName;
	String contextUrl = entity + "(P_StartDate=datetime'" + startDate + "',P_EndDate=datetime'" + endDate
		+ "')/Results";

	ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(queryRequest, path, contextUrl, null, null, true)
		.select("CampaignID,NumberOfUniqueClicks,NumberOfOpenedMessages,NmbrOfMisgMarketingPermissions,NumberOfLimitReached,NumberOfDeliveredMessages,NumberOfDeliveredEmailMessages,NumberOfMissingCommData,NumberOfBounces,NumberOfHardBounces,NumberOfSoftBounces,NumberOfSentMessages,NumberOfUnopenedMessages")
		.param("$filter", "(CampaignID eq '" + campaignId + "')");
	List<Metrics> metricsList = queryBuilder.build().execute(Constants.DESTINATION_NAME_BUSER)
		.asList(Metrics.class);

	for (Iterator<Metrics> iterator = metricsList.iterator(); iterator.hasNext();) {
	    Metrics metrics = iterator.next();
	    metrics.setBounceRateInPercent(
		    getRate((metrics.getNumberOfHardBounces() + metrics.getNumberOfSoftBounces()),
			    metrics.getNumberOfSentMessages()));
	    metrics.setOpenedMessageRateInPercent(
		    getRate(metrics.getNumberOfOpenedMessages(), metrics.getNumberOfDeliveredMessages()));
	    metrics.setClickToOpenRateInPercent(
		    getRate(metrics.getNumberOfUniqueClicks(), metrics.getNumberOfOpenedMessages()));
	    metrics.setUniqueClickRateInPercent(
		    getRate(metrics.getNumberOfUniqueClicks(), metrics.getNumberOfDeliveredEmailMessages()));
	}
	return metricsList;
    }

    /** Get Campaign Content via Interactions **/
    public static List<CampaignContent> getCampaignContents(QueryRequest queryRequest) throws ODataException {
	String campaignUuid = queryRequest.getSourceKeys().get("CampaignUUID").toString();
	String campaignId = getCampaignId(campaignUuid);
	// Using interactions as Campaign entity don't have MessageID reference
	List<Interaction> interactions = getInteractions(campaignId);
	/*-if (interactions == null || interactions.isEmpty()) {
	    throw new ODataException(null, "Could not find Campaign Content for the CampaignID: " + campaignId, null);
	}*/
	List<CampaignContent> campaignContents = new ArrayList<>();
	Set<String> msgIdSet = new HashSet<String>(); // to maintain uniqueness
	for (Interaction interaction : interactions) {
	    String msgId = interaction.getCampaignContent();
	    if (!msgIdSet.contains(msgId)) {
		String messageUUID = getMessageUUID(msgId);
		String contextUrl = "Messages(guid'" + messageUUID + "')/MessageContents";
		ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(queryRequest,
			Constants.CAMPAIGN_MESSAGE_SERVICE_PATH, contextUrl, null, null, false);
		campaignContents
			.addAll(queryBuilder.build().execute(Constants.DESTINATION_NAME).asList(CampaignContent.class));
	    }
	    msgIdSet.add(msgId);
	}
	msgIdSet.clear();
	msgIdSet = null;

	return campaignContents;
    }

    private static List<Interaction> getInteractions(String campaignID) throws ODataException {
	String contextUrl = "Interactions";
	String campaignId = String.format("%010d", Integer.parseInt(campaignID));
	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(null, Constants.INTERACTION_SERVICE_PATH, contextUrl, null, null, true)
		.param("$top", 100)
		.param("$filter", "(CampaignID eq '" + campaignId + "'and InteractionType eq 'EMAIL_OUTBOUND')");

	List<Interaction> interactions = queryBuilder.build().execute(Constants.DESTINATION_NAME)
		.asList(Interaction.class);

	return interactions;
    }

    private static String getMessageUUID(String messageID) throws ODataException {
	if (messageID == null) {
	    throw new ODataException(null, "Could not find Campaign Content for MessageID: " + messageID, null);
	}
	String contextUrl = "Messages";
	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(null, Constants.CAMPAIGN_MESSAGE_SERVICE_PATH, contextUrl, null, null, true)
		.param("$top", 1).param("$filter", "(Message eq " + messageID + ")");

	List<CampaignContent> campaignContents = queryBuilder.build().execute(Constants.DESTINATION_NAME)
		.asList(CampaignContent.class);

	return campaignContents.get(0).getMessageUUID();
    }

    private static String getTargetGroupUuid(String campaignUuid) throws ODataException {
	String contextUrl = "Campaigns(guid'" + campaignUuid + "')/CampaignAssignedTargetGroups";

	ODataQueryBuilder queryBuilder = QueryUtil.createQueryBuilder(null, Constants.CAMPAIGN_SERVICE_PATH, contextUrl,
		null, null, true);

	ODataQueryResult result = queryBuilder.build().execute(Constants.DESTINATION_NAME);
	Iterator<ResultElement> resultIterator = result.getResultElements().iterator();
	if (resultIterator.hasNext()) {
	    ResultElement resultElement = resultIterator.next();
	    return resultElement.getAsObject().get("TargetGroupUUID").asString();
	}
	log.debug("Could not find TargetGroup for the CampaignUUID: " + campaignUuid);
	return null;
    }

    private static String getCampaignId(String campaignUuid) throws ODataException {
	String contextUrl = "Campaigns(guid'" + campaignUuid + "')";

	ODataQueryResult result = ODataQueryBuilder.withEntity(Constants.CAMPAIGN_SERVICE_PATH, contextUrl).build()
		.execute(Constants.DESTINATION_NAME);
	if (result.getStreamData() != null) {
	    Campaign cpg = result.as(Campaign.class);
	    return cpg.getCampaignID();
	}
	throw new ODataException(null, "Could not find Campaign for the CampaignUUID: " + campaignUuid, null);
    }

    private static double getRate(double arg1, double arg2) {
	double result = 0;
	if (arg2 > 0) {
	    result = new BigDecimal((arg1 / arg2) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	return result;
    }
}
