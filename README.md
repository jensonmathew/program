Introduction:
-------------
This repository contains a sample spring boot application. 

This project is automatically committed/merged to by our cycle time collector every hour to generate smoke updates to ensure our systems work end to end.

You can find the pipelines generating this here: https://gitlab.com/tmobile/cdp/IaC/gitlab-cycle-time/pipelines

Requirement:
-----------
Set the following environment variables:

```bash
export CI_PROJECT_NAME=helloworld-1
export APP_VERSION=1.2.3
export BUILD_NUMBER=999
```

Note: These env vars are placeholders for local build. Gitlab CI will use its own `CI_PROJECT_NAME` and pick `APP_VERSION` and `BUILD_NUMBER` from [.gitlab-ci.yml](https://gitlab.com/tmobile/templates_projects/helloworld-1/blob/tmo/master/.gitlab-ci.yml)


Compile:
-------
To compile the app please run the following command:

	./mvnw clean package
	
Note: `mvnw` [Maven wrapper](https://github.com/takari/maven-wrapper) auto-downloads Maven on a dev's machine who doesn't yet have it installed. 

Test Coverage Report Using Jacoco
==================================
	mvn -Djacoco.destFile=target/jacoco.exec clean jacoco:prepare-agent package

The XML and HTML reports will be located under:
projecthome/target/site/jacoco/index.html

projecthome/target/clover/site/jacoco.xml
	
Running the App:
---------------
    mvn spring-boot:run
    
OR

    java -jar target/helloworld-1.2.3-999.jar 

Testing the Application:
------------------------

	curl --user testuser1:password1 http://localhost
In Web Browser

    http://testuser1:password1@localhost
    
Deploying to Pivotal Cloud Foundry:
-----------------------------------
The project can be deployed to PCF with relative ease, there is manifest.yml file is created
for deployment purpose.

Simply login into PCF using
    
    cf login
and pushing the app using the following command

    cf push

qTest Integreation:
-------------------

The e2egating stage running the test cases in the Codeless test framework and uploading the test results to [qTest](https://qtest-training.t-mobile.com/p/39/portal/project#tab=testexecution&object=2&id=316)

How to run the tests on CTP and using Apigee API to upload the test results on the qTest [documentation](https://gitlab.com/tmobile/templates/-/blob/tmo/master/documentation/ctp-e2egating.md)

Feature Flags:
-------------------
Feature toggle(Flags) is used to enable or disable the features during runtime.

The feature flag maybe turned ON or OFF using the toggle switch at *Operations* -> *Feature Flags* for a particular project.

**How it works**

GitLab uses [Unleash](https://github.com/Unleash/unleash), a feature toggle service. It supports official client implementations for Java, Node.js, Go, Ruby, Python and .NET Core.

[Create feature flags](https://docs.gitlab.com/ee/operations/feature_flags.html#create-a-feature-flag) in GitLab and use the API from the application to get the list of feature flags and their statuses.

The application must be configured to communicate with GitLab, so it’s up to developers to use a compatible client library and integrate the feature flags in the app.

For this example project, pom.xml and Controller files were modified to incorporate Unleash libraries for achieving Feature Flag capability.

**Examples**

Click [here](./docs/examples.md) to see all samples.