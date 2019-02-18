package com.sap.cec.mkt.insight.service;

import java.util.ArrayList;
import java.util.List;

import com.sap.cec.mkt.insight.models.UserDetails;
import com.sap.cec.mkt.insight.util.UserUtil;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;

/** User service class **/
public class UserService {

    @Query(entity = "LoggedInUserDetails", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getLoggedInUserDetails(QueryRequest queryRequest) {
	List<UserDetails> pojo = new ArrayList<>();
	pojo.add(UserUtil.getLoggedInUserDetails());
	QueryResponse queryResponse = QueryResponse.setSuccess().setData(pojo).response();

	return queryResponse;
    }
}
