package com.sap.cec.mkt.insight.dataprovider;

import java.util.ArrayList;
import java.util.List;

import com.sap.cec.mkt.insight.util.Constants;
import com.sap.cec.mkt.insight.util.QueryUtil;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;

/** AccountDataProvider data provider class- C4C **/
public class AccountDataProvider {

    private AccountDataProvider() {

    }

    /** Get Account Team Owner **/
    public static String getAccountTeamOwner(QueryRequest queryRequest, String interactionContactId)
	    throws ODataException {
	String employeeId = getAccountTeamMember(queryRequest, interactionContactId);
	if (employeeId == null) {
	    return null;
	}
	List<String> employees = new ArrayList<>();
	String contextUrl = "EmployeeCollection";

	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(queryRequest, Constants.C4C_EMPLOYEE_PATH, contextUrl, null, null, true)
		.param("$filter", "(EmployeeID eq '" + employeeId + "')").select("FirstName", "LastName");

	queryBuilder.build().execute(Constants.DESTINATION_C4C)
		.forEach(resultElement -> employees.add(resultElement.getAsObject().get("FirstName").asString() + " "
			+ resultElement.getAsObject().get("LastName").asString()));

	return !employees.isEmpty() ? employees.get(0) : null;
    }

    /** Get Account Team Members **/
    private static String getAccountTeamMember(QueryRequest queryRequest, String interactionContactId)
	    throws ODataException {
	String objectId = getAccountDetails(queryRequest, interactionContactId);
	if (objectId == null) {
	    return null;
	}
	List<String> teamMembers = new ArrayList<>();
	String contextUrl = "CorporateAccountCollection('" + objectId + "')/CorporateAccountTeam";

	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(queryRequest, Constants.C4C_CUSTOMER_PATH, contextUrl, null, null, true)
		.select("EmployeeID");

	queryBuilder.build().execute(Constants.DESTINATION_C4C)
		.forEach(resultElement -> teamMembers.add(resultElement.getAsObject().get("EmployeeID").asString()));

	return !teamMembers.isEmpty() ? teamMembers.get(0) : null;
    }

    /** Get Account details **/
    private static String getAccountDetails(QueryRequest queryRequest, String interactionContactId)
	    throws ODataException {
	List<String> accDetails = new ArrayList<>();
	String contextUrl = "CorporateAccountCollection";

	ODataQueryBuilder queryBuilder = QueryUtil
		.createQueryBuilder(queryRequest, Constants.C4C_CUSTOMER_PATH, contextUrl, null, null, true)
		.param("$filter", "(AccountID eq '" + interactionContactId + "')").select("ObjectID");

	queryBuilder.build().execute(Constants.DESTINATION_C4C)
		.forEach(resultElement -> accDetails.add(resultElement.getAsObject().get("ObjectID").asString()));

	return !accDetails.isEmpty() ? accDetails.get(0) : null;
    }
}
