## Destination Configurations

Open the SAP Cloud Platform cockpit, and select the region in which your subaccount is hosted. Select the global account that contains your subaccount, and then choose your subaccount tile. For more information about regions, see [Regions and Hosts](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/350356d1dc314d3199dca15bd2ab9b0e.html).

#### Destination to Connect SAP UI5 Application to Java Application:

1. Navigate to Connectivity > Destinations.

2. Click “New Destination”.

3. Fill all the fields as below:

   * Name: marketingMashup

   * URL: Java application URL

   * Proxy Type: Internet

   * Authentication: AppToAppSSO

#### Destination to Access Custom CDS View OData API:

1. Navigate to Connectivity > Destinations.

2. Click “New Destination”.

3. Fill all the fields as below:

   * Name: mkt-ba

   * URL: SAP Marketing Cloud API Host Name, eg.: https://myxxxxxx-api.s4hana.ondemand.com

   * Proxy Type: Internet

   * Authentication: BasicAuthentication

   * User: Username of Communication User created

   * Password: Password of Communication User created

   * Additional Properties

     * CustomViewName: Provide the Custom CDS OData Service Name

     *  P_StartDate: Provide the start date from when the data should be considered by Custom CDS View, eg.: 2017-01-01T00:00:00

     * P_EndDate: Provide the end date till when the data should be considered by Custom CDS View, eg.: 2019-01-01T00:00:00

     * TrustAll: True

#### Destination to Access SAP Cloud for Sales APIs:

1. Navigate to Connectivity > Destinations.

2. Click “New Destination”.

3. Fill all the fields as below:

   * Name: c4c

   * URL: Cloud for Sales OData API URL

   * Proxy Type: Internet

   * Authentication: BasicAuthentication

   * User: Username to connect to SAP Cloud for Sales OData API

   * Password: Password to connect to SAP Cloud for Sales OData API
