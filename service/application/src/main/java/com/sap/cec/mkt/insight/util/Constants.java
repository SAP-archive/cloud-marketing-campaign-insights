package com.sap.cec.mkt.insight.util;

/** Constant class **/
public class Constants {

    public static final String DESTINATION_NAME = "mkt";
    public static final String DESTINATION_C4C = "c4c";
    public static final String DESTINATION_NAME_BUSER = "mkt-ba";

    public static final String C4C_CUSTOMER_PATH = "/sap/c4c/odata/v1/customer";
    public static final String C4C_EMPLOYEE_PATH = "/sap/c4c/odata/v1/employeeanduser";
    public static final String CAMPAIGN_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_CAMPAIGN_SRV";
    public static final String CAMPAIGN_MESSAGE_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_CAMPAIGN_MESSAGE_SRV";
    public static final String CONTACT_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_CONTACT_SRV;v=0002";
    public static final String INTERACTION_CONTACT_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_INTERACTION_CONTACT_SRV;v=0002";
    public static final String INTERACTION_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_INTERACTION_SRV";
    public static final String TARGET_GROUP_SERVICE_PATH = "/sap/opu/odata/sap/API_MKT_TARGET_GROUP_SRV";

    public static final String SALES_REP_ROLE_AEXE = "AEXE";

    public static enum InteractionType {
	HARD_BOUNCE, SOFT_BOUNCE, BOUNCE, SENT_MESSAGE, LIMIT_REACHED, MISSING_COMMUNICATION_DATA, MISSING_PERMISSION
    }

    public static enum ActivityType {
	APPOINTMENT, PHONECALL, TASK
    }
}
