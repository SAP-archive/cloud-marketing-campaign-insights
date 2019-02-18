
## Deploying the Java application

Open the SAP Cloud Platform cockpit and select the region in which your subaccount is hosted. Select the global account that contains your subaccount, and then choose your subaccount tile. For more information about regions, see [Regions and Hosts](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/350356d1dc314d3199dca15bd2ab9b0e.html).

Complete the following steps to deploy the Java Application:

1. In the left panel, click on Applications > Java Applications.

2. Click “Deploy Application”.

3. A pop-up opens. Provide the following details:

   * WAR File Location: Select the WAR file built during step [Build Service](/docs/build-service.md).

   * Application Name: Enter a name.

   * Runtime Name: Select “Java EE 7 Web Profile TomEE 7”.

   * Runtime Version: Select the latest version.

   * Compute Unit Size: Select an appropriate unit size.

   * JVM version: JRE 8.

   * Click “Deploy” button. Once the upload in completed, click “Done” button.

   * Start the application.

To run the java application in your local environment, download the Java EE 7 Web Profile TomEE7 [SAP Cloud SDK](https://tools.hana.ondemand.com/#cloud).
