package com.sap.cec.mkt.insight.service;

import com.sap.cec.mkt.insight.dataprovider.CampaignDataProvider;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.operations.Read;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;

/** Campaign service class **/
public class CampaignService extends AbstractService {

    @Query(entity = "CampaignsByTeamMember", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignsByTeamMember(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignsByTeamMember(queryRequest));
    }

    @Query(entity = "Campaigns", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaign(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaigns(queryRequest));
    }

    @Read(entity = "Campaigns", serviceName = "API_MKT_INSIGHT_SRV")
    public ReadResponse getCampaign(ReadRequest readRequest) {
	return super.getEntity(readRequest);
    }

    @Query(sourceEntity = "Campaigns", entity = "TeamMembers", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignTeamMembers(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignTeamMembers(queryRequest));
    }

    @Query(sourceEntity = "Campaigns", entity = "Contacts", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignContacts(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignContacts(queryRequest));
    }

    @Query(sourceEntity = "Campaigns", entity = "Metrics", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignMetrics(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignMetrics(queryRequest));
    }

    @Query(sourceEntity = "Campaigns", entity = "InteractionContacts", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignInteractionContacts(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignContacts(queryRequest));
    }

    @Query(sourceEntity = "Campaigns", entity = "CampaignContents", serviceName = "API_MKT_INSIGHT_SRV")
    public QueryResponse getCampaignContents(QueryRequest queryRequest) {
	return super.getEntitySet(queryRequest, () -> CampaignDataProvider.getCampaignContents(queryRequest));
    }
}
