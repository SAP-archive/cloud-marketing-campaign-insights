## SAML+OAUTH Configuration

Create a signing certificate to establish trust between SAP Marketing Cloud and SAP Cloud Platform.

Follow the [link](https://help.sap.com/viewer/233fb3f82e484f75ab3511fccd46d101/Cloud/en-US/a4f1d55c57b446fc8d66a9f59009225f.html#loioa4f1d55c57b446fc8d66a9f59009225f) to create the configurations. Provide the destination name as ‘mkt’.

By end of this step will have Communication User and Communication System, which will be using for SAML+OAUTH configuration.

**Note:** In case if you want to continue with Default IdP, follow the [link](https://help.sap.com/viewer/233fb3f82e484f75ab3511fccd46d101/Cloud/en-US/789a120a45e84d5d997c04e0ebbd0a05.html) till step 8 only and don’t forget to change the Configuration Type to Default after downloading the metdata file.

For more details you can see this [blog](https://blogs.sap.com/2018/02/05/deep-dive-8-with-sap-s4hana-cloud-sdk-leverage-principal-propagation-via-oauth-2-when-consuming-a-business-api-from-s4hana-cloud)

### Communication Settings for SAML+OAUTH:

Configure [Communication Arrangements](https://help.sap.com/viewer/9775a1fb912c487b961bd59cd6179c3a/latest/en-US/72add13288c6440ebd8641eb5d22582d.html) for the following Communication Scenarios and reuse the Communication User and Communication System created above. Select authentication method OAuth2.0 for Inbound Communication.

* SAP_COM_0204 ([Campaign API](https://api.sap.com/api/API_MKT_CAMPAIGN_SRV/resource))

* SAP_COM_0205 ([Target Group API](https://api.sap.com/api/API_MKT_TARGET_GROUP_SRV/resource))

* SAP_COM_0206 ([Interaction API](https://api.sap.com/api/API_MKT_INTERACTION_SRV/resource))

* SAP_COM_0207 ([Interaction Contact API](https://api.sap.com/api/API_MKT_INTERACTION_CONTACT_SRV/resource))

* SAP_COM_0208 ([Campaign Message API](https://api.sap.com/api/API_MKT_CAMPAIGN_MESSAGE_SRV/resource))

