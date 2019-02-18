##  SAP Marketing Cloud Configuration

#### Custom CDS View:

In order to display the individual KPIs for each campaign, the application relies on the concept of [Custom CDS View](https://help.sap.com/viewer/b4367b1cec3243c4989f0ff3d727c4ab/7.51.0/en-US/1d77c396e7fc43bc94c46947305f5f71.html).

To create a Custom CDS View, proceed as follows:
1. On the SAP Marketing Cloud Fiori Launchpad, choose the Custom CDS Views app tile.

2. Create a new Custom CDS view. Enter the name and the description of the new view. For example, MktInsightMetrics (displayed as YY1_MarketingInsightMetrics_CDS)

3. Select an appropriate option such that publishing the view publishes the OData. This Odata is used to fetch Campaign Success Metrics.

4. Select "I_MKT_CampaignSuccessCube" as the primary data source.

5. Navigate to Field Selection tab, and select the fields that must be used in the new custom CDS view. For example:
Interaction(Key), Campaign, CampaignID, NumberOfUniqueClicks, NumberOfSentMessages, NumberOfOpenedMessages, NumberOfUnopenedMessages, NumberOfDeliveredMessages, NumberOfLimitReached, NumberOfMissingCommData, NmbrOfMisgMarketingPermissions, NumberOfBounces, NumberOfHardBounces, NumberOfSoftBounces

6. Navigate to Field Properties tab, and select Aggregation 'SUM' for following field properties:
NumberOfUniqueClicks, NumberOfSentMessages, NumberOfOpenedMessages, NumberOfUnopenedMessages, NumberOfDeliveredMessages, NumberOfLimitReached, NumberOfMissingCommData, NmbrOfMisgMarketingPermissions, NumberOfBounces, NumberOfHardBounces, NumberOfSoftBounces

7. Navigate to Filters tab, and add a filter with formula CampaignID != ' '.

8. Save and Publish the view.

#### Communication Settings for Custom CDS View:

1. Create a Custom Communication Scenario. See [Custom Communication Scenario](https://help.sap.com/viewer/841f379acd104dbf8685b3ad26e66af3/SHIP/en-US/f7cec0c6aa51485cbb3cec9bff107707.html)

2. Create a Communication User. See [Maintain Communication Users](https://help.sap.com/viewer/0f9408e4921e4ba3bb4a7a1f75f837a7/latest/en-US/4b2fbdce7d4445c288500152f3d2192e.html)

3. Create a Communication System. See [Maintain Communication Systems](https://help.sap.com/viewer/841f379acd104dbf8685b3ad26e66af3/SHIP/en-US/40de29e876414ed6ace28acffac38a92.html)

4. Create a Communication Arrangement and select the Custom Communication Ccenario created before. See [Maintain Communication Arrangements](https://help.sap.com/viewer/841f379acd104dbf8685b3ad26e66af3/SHIP/en-US/b08210aa5eb84cc783f77d8792a54abe.html)
