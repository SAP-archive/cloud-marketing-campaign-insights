package com.sap.cec.mkt.insight.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cec.mkt.insight.util.MetadataUtil;
import com.sap.cloud.sdk.service.prov.v4.rt.core.web.ODataServlet;

public class MarketingInsightODataServlet extends com.sap.cloud.sdk.service.prov.v4.rt.core.web.ODataServlet {
    private static final long serialVersionUID = 1L;

    static final Logger logger = LoggerFactory.getLogger(ODataServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	if (req.getPathInfo() != null && !req.getPathInfo().isEmpty() && req.getPathInfo().endsWith("$metadata")) {
	    MetadataUtil.addCustomFieldsToMetadata();
	}
	super.service(req, resp);
    }

}
