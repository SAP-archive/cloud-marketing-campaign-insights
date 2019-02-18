package com.sap.cec.mkt.insight.util;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;

import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.v4.request.impl.QueryRequestV4Impl;

/** Query utility class **/
public class QueryUtil {

    /** Build OData query **/
    public static ODataQueryBuilder createQueryBuilder(QueryRequest queryRequest, String servicePath, String entity,
	    String addlFilterQuery, String sortQuery, boolean addPagination) {
	ODataQueryBuilder queryBuilder = ODataQueryBuilder.withEntity(servicePath, entity);

	if (queryRequest != null) {
	    String filterString = buildFilterString(queryRequest, addlFilterQuery);
	    if (StringUtils.isNotBlank(filterString)) {
		queryBuilder.param("$filter", filterString);
	    }

	    if (StringUtils.isNotBlank(sortQuery)) {
		OrderByOption orderByOption = ((QueryRequestV4Impl) queryRequest).getUriInfo().getOrderByOption();
		if (orderByOption != null) {
		    queryBuilder.param("$orderby", orderByOption.getText());
		}
	    }
	    if (!queryRequest.getSelectProperties().isEmpty()) {
		queryBuilder.select(queryRequest.getSelectProperties().toArray(new String[0]));
	    }
	    if (addPagination) {
		queryBuilder.skip(queryRequest.getSkipOptionValue() > 0 ? queryRequest.getSkipOptionValue() : 0)
			.top(queryRequest.getTopOptionValue() > 0 ? queryRequest.getTopOptionValue() : 100);
	    }
	}

	return queryBuilder;
    }

    private static String buildFilterString(QueryRequest queryRequest, String addlFilterQuery) {
	String filterString = null;
	if (queryRequest != null) {
	    FilterOption filterOption = ((QueryRequestV4Impl) queryRequest).getUriInfo().getFilterOption();
	    if (filterOption != null) {
		String filterQuery = ((QueryRequestV4Impl) queryRequest).getUriInfo().getFilterOption().getText();
		if (filterQuery != null) {
		    if (filterQuery.contains("contains(")) {
			filterQuery = getV2SearchFilterQuery(filterQuery);
		    }
		    filterString = filterQuery.replaceAll("\\{", "(").replaceAll("\\}", ")").replace(" EQ ", " eq ")
			    .replace(" AND ", " and ").replace(" OR ", " or ");
		}
	    }
	}

	if (StringUtils.isNotBlank(addlFilterQuery)) {
	    if (StringUtils.isNotBlank(filterString)) {
		filterString = filterString.concat(" and ").concat(addlFilterQuery);
	    } else {
		filterString = addlFilterQuery;
	    }
	}
	return filterString;
    }

    private static String getV2SearchFilterQuery(String filterQuery) {
	String searchfilterQuery = filterQuery;
	String v4Query = filterQuery.substring(filterQuery.indexOf("contains("), filterQuery.indexOf(")") + 1);
	String qString = v4Query.substring(v4Query.indexOf("contains(") + 9, v4Query.indexOf(")"));
	String[] parts = qString.split(",");
	String field = parts[0];
	String value = parts[1];
	String v2Query = "substringof(" + value + "," + field + ") eq true";
	searchfilterQuery = filterQuery.replace(v4Query, v2Query);

	return searchfilterQuery;
    }
}
