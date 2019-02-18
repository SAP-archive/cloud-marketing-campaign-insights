package com.sap.cec.mkt.insight.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cec.mkt.insight.dataprovider.CampaignDataProvider;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;

/** Abstract service class **/
public class AbstractService {

    private static final Logger log = LoggerFactory.getLogger(AbstractService.class);

    protected QueryResponse getEntitySet(QueryRequest queryRequest, DataProvider dataProvider) {
	QueryResponse queryResponse = null;
	try {
	    queryResponse = QueryResponse.setSuccess().setData(dataProvider.getData()).response();
	} catch (ODataException e) {
	    log.error("==> Exception calling backend OData V2 service for Query of EnitySet {}: ", e.getMessage());

	    ErrorResponse errorResponse = ErrorResponse.getBuilder()
		    .setMessage("There is an error, check the logs for more details: " + e.getMessage())
		    .setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).setCause(e).response();
	    queryResponse = QueryResponse.setError(errorResponse);
	}
	return queryResponse;
    }

    protected ReadResponse getEntity(ReadRequest readRequest) {
	log.debug("==> now call backend OData V2 service");
	ReadResponse readResponse = null;
	try {
	    readResponse = ReadResponse.setSuccess().setData(CampaignDataProvider.getCampaign(readRequest)).response();
	} catch (ODataException e) {
	    log.error("==> Eexception calling backend OData V2 service for Read of a Product {}: ", e.getMessage());
	    ErrorResponse errorResponse = ErrorResponse.getBuilder()
		    .setMessage("There is an error, check the logs for details").setStatusCode(500).setCause(e)
		    .response();
	    readResponse = ReadResponse.setError(errorResponse);
	}
	return readResponse;
    }

    @FunctionalInterface
    interface DataProvider {
	public List<?> getData() throws ODataException;
    }
}
