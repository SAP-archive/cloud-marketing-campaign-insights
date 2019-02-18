package com.sap.cec.mkt.insight.dataprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cec.mkt.insight.util.Constants;
import com.sap.cec.mkt.insight.util.QueryUtil;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryResult;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;

/** Target Group data provider class **/
public class TargetGroupDataProvider {

    private TargetGroupDataProvider() {

    }

    /** Get Target Group members **/
    public static List<String> getTargetGroupMemberContactUuids(QueryRequest queryRequest, String targetGroupUuid)
	    throws ODataException {
	List<String> contactUuids = new ArrayList<>();

	String contextUrl = "TargetGroups(guid'" + targetGroupUuid + "')/TargetGroupInteractionContacts";

	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(queryRequest, Constants.TARGET_GROUP_SERVICE_PATH, contextUrl, null, null, true)
		.select("InteractionContactUUID");

	queryBuilder.build().execute(Constants.DESTINATION_NAME).forEach(resultElement -> {
	    if (resultElement.getAsObject().get("InteractionContactUUID") != null) {
		contactUuids.add(resultElement.getAsObject().get("InteractionContactUUID").asString());
	    }
	});

	return contactUuids;
    }

    /** Get Account Ids **/
    public static List<String> getInteractionContactIds(QueryRequest queryRequest, String targetGroupUuid,
	    String interactionContactUuid) throws ODataException {
	List<String> contactIds = new ArrayList<>();
	String contextUrl = "TargetGroups(guid'" + targetGroupUuid + "')/TargetGroupInteractionContacts";

	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(queryRequest, Constants.TARGET_GROUP_SERVICE_PATH, contextUrl, null, null, true)
		.select("InteractionContactId")
		.param("$filter", "(InteractionContactOrigin eq 'SAP_C4C_BUPA' and InteractionContactUUID eq guid'"
			+ interactionContactUuid + "')");

	queryBuilder.build().execute(Constants.DESTINATION_NAME).forEach(resultElement -> {
	    if (resultElement.getAsObject().get("InteractionContactId") != null) {
		contactIds.add(resultElement.getAsObject().get("InteractionContactId").asString());
	    }

	});

	return contactIds;
    }

    /** Get TargetGroup member count **/
    public static String getTargetGroupMemberCount(QueryRequest queryRequest, String targetGroupUuid)
	    throws ODataException {
	String contextUrl = "TargetGroups(guid'" + targetGroupUuid + "')";

	ODataQueryResult result = ODataQueryBuilder.withEntity(Constants.TARGET_GROUP_SERVICE_PATH, contextUrl).build()
		.execute(Constants.DESTINATION_NAME);

	Map<?, ?> map = result.as(Map.class);

	return map.get("TargetGroupMemberCount") != null ? map.get("TargetGroupMemberCount").toString() : null;
    }
}
