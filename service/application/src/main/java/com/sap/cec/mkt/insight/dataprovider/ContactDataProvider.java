package com.sap.cec.mkt.insight.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cec.mkt.insight.models.InteractionContact;
import com.sap.cec.mkt.insight.util.Constants;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryResult;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;

/** Contact data provider class **/
public class ContactDataProvider {

    private static final Logger log = LoggerFactory.getLogger(ContactDataProvider.class);
    private static String[] selectQuery = new String[] { "InteractionContactUUID", "FullName", "EmailAddress",
	    "PhoneNumber" };

    private ContactDataProvider() {

    }

    /** Get Contacts of a target groups **/
    public static List<InteractionContact> getContacts(QueryRequest queryRequest, String targetGroupUuid)
	    throws ODataException {
	log.debug("==> now execute getContacts of target group for targetGroupUuid: " + targetGroupUuid);

	List<String> targetGroupMemberContactUuids = TargetGroupDataProvider
		.getTargetGroupMemberContactUuids(queryRequest, targetGroupUuid);
	List<InteractionContact> contacts = new ArrayList<>();

	if (targetGroupMemberContactUuids != null && !targetGroupMemberContactUuids.isEmpty()) {
	    for (String tgMemberContactUuid : targetGroupMemberContactUuids) {
		contacts.add(getContact(queryRequest, targetGroupUuid, tgMemberContactUuid));
	    }
	}

	return contacts;
    }

    /** Get Interaction Contact by ID **/
    private static InteractionContact getContact(QueryRequest queryRequest, String tgUuid, String contactUuid)
	    throws ODataException {
	log.debug("==> now execute read for contactUuid: " + contactUuid);

	String contextUrl = "InteractionContacts(guid'" + contactUuid + "')";
	ODataQueryResult result = ODataQueryBuilder.withEntity(Constants.INTERACTION_CONTACT_SERVICE_PATH, contextUrl)
		.select(selectQuery).build().execute(Constants.DESTINATION_NAME);

	InteractionContact contact = result.as(InteractionContact.class);

	List<String> targetGroupMemberContactIds = TargetGroupDataProvider.getInteractionContactIds(queryRequest,
		tgUuid, contactUuid);
	if (targetGroupMemberContactIds != null && !targetGroupMemberContactIds.isEmpty()) {
	    for (String tgMemberContactId : targetGroupMemberContactIds) {
		String owner = AccountDataProvider.getAccountTeamOwner(queryRequest, tgMemberContactId);
		log.debug("Account owner of contact: " + owner);
		contact.setSalesRepresentativeFullName(owner);
	    }
	}

	return contact;
    }

    /** Get Interaction Contact by ID **/
    public static InteractionContact getContact(String contactUuid) throws ODataException {
	log.debug("==> now execute read for contactUuid: " + contactUuid);

	String contextUrl = "InteractionContacts(guid'" + contactUuid + "')";
	ODataQueryResult result = ODataQueryBuilder.withEntity(Constants.INTERACTION_CONTACT_SERVICE_PATH, contextUrl)
		.select(selectQuery).build().execute(Constants.DESTINATION_NAME);

	return result.as(InteractionContact.class);
    }
}
