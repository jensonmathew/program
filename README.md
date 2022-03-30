Introduction:
-------------
helloworld is intended to be an aggregate of the best known patterns within T-Mobile working end to end through the entire delivery life cycle. All new transformations start here.

Workflow: Continuous Delivery and Continuous Deployments
-----------
- All environments deploy upon trunk (including production).
- Epehemeral Enviroments tracks the life cycle of each non-trunk branch. If a branch goes idle for 14 days or merge request changes status to merged or closed, the environment is "stop" - which means purged.
- A sanity test is run after each deployment to ensure the endpoint yields the expected results.

Deployment Flags:
-----------

`DEPLOY_ALL` - this means "deploy to all environments" and is set currently until ephemeral environoments for merge requests are setup.

Local Builds:
-----------
Set the following environment variables:

```bash
export CI_PROJECT_NAME=helloworld
export APP_VERSION=1.2.3
export BUILD_NUMBER=999
```

Note: These env vars are placeholders for local build. Gitlab CI will use its own `CI_PROJECT_NAME` and pick `APP_VERSION` and `BUILD_NUMBER` from [.gitlab-ci.yml](https://gitlab.com/tmobile/templates_projects/helloworld-1/blob/tmo/master/.gitlab-ci.yml)


Compile:
-------
To compile the app please run the following command:
    wget https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw && chmod +x mvnw
	./mvnw clean package
	
Note: `mvnw` [Maven wrapper](https://github.com/takari/maven-wrapper) auto-downloads Maven on a dev's machine who doesn't yet have it installed. 

Windows:
    wget https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd

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

Sanity verify:
------------------------
Each deployment tier (prod, npe, review) has it's own static child pipeline demonstrating trunk based development.

Each individual deployment has a subsequent sanity job which verifies the results of the deployment just before it.

The verifications found in `src/test/helloworld.postman_collection.json` are basic in nature but intended to be as configurable as necessary. The basic checks include inspecting the returned json to ensure it matches what we would expect.

Below are equivalent curl examples for verifying one of the production endpoints. Replace the host with the environment you want to check.

```console
curl https://helloworld.apps.px-cde02.cf.t-mobile.com/actuator/info
curl https://helloworld.apps.px-cde02.cf.t-mobile.com/actuator/health

curl --user testuser1:password1 curl https://helloworld.apps.px-cde02.cf.t-mobile.com
```

qTest Integration:
-------------------

The e2egating stage running the test cases in the Codeless test framework and uploading the test results to [qTest](https://qtest-training.t-mobile.com/p/39/portal/project#tab=testexecution&object=2&id=316)

How to run the tests on CTP and using Apigee API to upload the test results on the qTest [documentation](https://gitlab.com/tmobile/templates/-/blob/tmo/master/documentation/ctp-e2egating.md)

Feature Flags:
-------------------
Feature Flags have been set up for different languages: french, german, and dutch. English is default language if all language flags are turned off or if no environment scope matches. First language is used if multiple feature flags are turned on for a specific environment.

The environment scope and toggle can be changed for demonstrative purposes here - https://gitlab.com/tmobile/templates_projects/helloworld/-/feature_flags.

**How it works**

GitLab recommends using the [Unleash](https://github.com/Unleash/unleash) client to consume their feature flag endpoints. Unleash supports Java, Node.js, Go, Ruby, Python and .NET Core.

Understanding Kubernetes Deployment
===================================

This app is deployed to Kubernetes using Helm. While covering Helm at any meaningful depth is outside the scope of this project, you can [get a good overview of Helm here](https://www.baeldung.com/ops/kubernetes-helm).

Some key configuration callouts have to do with `readiness` and `liveness` probes, which are used to tell Kubernetes when your application is ready to accept traffic and actively able to process requests, respectively.

Documented comments in [application.properties](src/main/resources/application.properties) help explain how these endpoints are configured.

If you have other endpoints you would rather use instead of these built-in Springboot ones, you can change the paths of `livenessProbe.path` and `readinessProbe.path` in [values.yaml.j2](k8s/values.yaml.j2). However, it is recommended to stick with the [Springboot ones](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints), as they are comprehensive [and extensible](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health.writing-custom-health-indicators). 
