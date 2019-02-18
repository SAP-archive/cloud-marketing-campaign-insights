package com.sap.cec.mkt.insight.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.core.edm.provider.EdmxProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.service.prov.v4.rt.cdx.CDXEdmProvider;
import com.sap.cloud.sdk.service.prov.v4.rt.cdx.ServiceRepository;

/** Metadata utility class **/
public class MetadataUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataUtil.class);

    /** Add custom fields to metadata **/
    public static void addCustomFieldsToMetadata() {

	CDXEdmProvider edmxMetadataProvider = ServiceRepository.getInstance()
		.getEdmxMetadataProvider("API_MKT_INSIGHT_SRV");
	try {
	    List<CsdlSchema> schemas = edmxMetadataProvider.getSchemas();
	    if (!schemas.isEmpty()) {
		List<CsdlEntityType> CsdlEntityList = schemas.get(0).getEntityTypes();
		CsdlEntityType campaignEntityType = null;
		for (CsdlEntityType csdlEntityType : CsdlEntityList) {
		    if (csdlEntityType.getName().equals("CampaignEntityType")) {
			campaignEntityType = csdlEntityType;
			break;
		    }
		}
		List<String> customFieldList = getCampaignCustomFields();
		LOG.debug("Number of custom field in metadata '{}' ", customFieldList.size());
		for (String customField : customFieldList) {
		    CsdlProperty property = new CsdlProperty();
		    property.setName(customField);
		    property.setType("Edm.String"); // API_MKT_INSIGHT_SRV.CustomCT
		    if (campaignEntityType != null) {
			campaignEntityType.getProperties().add(property);
		    }
		}
	    }
	} catch (org.apache.olingo.commons.api.ex.ODataException e) {
	    LOG.error(" Exception while adding custom fields to metadata {}", e);
	}
    }

    private static EdmProvider readMetadata(final InputStream inputStream, final boolean validate)
	    throws EntityProviderException {

	return new EdmxProvider().parse(inputStream, validate);
    }

    /** Get Custom fields from Campaign metadata **/
    private static List<String> getCampaignCustomFields() {
	InputStream inputStream = null;
	List<String> customFields = new ArrayList<>();
	try {
	    inputStream = getMetadata(Constants.DESTINATION_NAME, Constants.CAMPAIGN_SERVICE_PATH).getEntity()
		    .getContent();
	    EdmProvider provider = readMetadata(inputStream, false);
	    if (provider.getSchemas().isEmpty() || provider.getSchemas().get(0).getEntityTypes().isEmpty()) {
		return customFields;
	    }
	    EntityType entity = provider.getSchemas().get(0).getEntityTypes().get(0);
	    List<Property> properties = entity.getProperties();
	    for (Property property : properties) {
		List<AnnotationAttribute> attributeList = property.getAnnotationAttributes();
		AnnotationAttribute customFieldAttribute = null;
		for (AnnotationAttribute annotationAttribute : attributeList) {
		    if (annotationAttribute.getName().equals("is-extension-field")) {
			customFieldAttribute = annotationAttribute;
			break;
		    }
		}
		if (customFieldAttribute != null) {
		    String bool = customFieldAttribute.getText();
		    if (bool != null && bool.equals("true")) {
			customFields.add(property.getName());
		    }
		}
	    }
	} catch (ODataException | UnsupportedOperationException | IOException e) {
	    LOG.error(" Exception while getting custom fields {}", e);
	} finally {
	    try {
		if (inputStream != null) {
		    inputStream.close();
		}
	    } catch (IOException e) {
		LOG.debug("getCampaignCustomFields, error in closing stream");
	    }
	}
	return customFields;
    }

    /**
     * Get metadata
     * 
     * @param destinationName
     * @param path
     * @return HttpResponse @throws
     */
    private static HttpResponse getMetadata(String destinationName, String path) {
	HttpResponse httpResponse = null;
	try {
	    Destination destination = DestinationAccessor.getDestination(destinationName);
	    HttpClient httpClient = HttpClientAccessor.getHttpClient(destinationName);
	    String uri = destination.getUri().toString();
	    String url = uri + path + "/$metadata";

	    LOG.debug("Get Metadata URL: {} ", url);

	    HttpGet httpGet = new HttpGet(url);
	    httpResponse = httpClient.execute(httpGet);

	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    LOG.debug("Get Metadata StatusCode: {} ", statusCode);
	} catch (IOException e) {
	    LOG.error(" Exception while getting metadata {}", e);
	}
	return httpResponse;
    }

}
