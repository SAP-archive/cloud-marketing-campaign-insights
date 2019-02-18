## Build Service

Service folder contains a Maven project. Complete the following steps to build a project:

* Open command line interface/terminal and navigate to the [Service](/service) folder.

* Run the “mvn clean install” command. This creates a “target” folder under ../service/application, which contains “marketing-insight-application.war” file. This WAR file must be used for deploying the service on SAP Cloud Platform. 

Service metadata can be found [here](/service/application/src/main/webapp/WEB-INF/classes/edmx/API_MKT_INSIGHT_SRV.xml).

[Deploy the Service on SAP Cloud Platform](/docs/deploy-service.md).
