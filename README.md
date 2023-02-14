Introduction:
-------------
helloworld is intended to be an aggregate of the best known patterns within T-Mobile working end to end through the entire delivery life cycle. All new transformations start here.

Workflow: Continuous Delivery and Continuous Deployments
-----------
- All environments deploy upon trunk (including production).
- Epehemeral Enviroments tracks the life cycle of each non-trunk branch. If a branch goes idle for 14 days or merge request changes status to merged or closed, the environment is "stop" - which means purged.
- A sanity test is run after each deployment to ensure the endpoint yields the expected results.

The helm chart values include the deployment annotation `rollme: "{{ randAlphaNum 5 | quote }}"` to ensure that a new redeployment is being triggered for every deploy (even if it's the same code/objects). See: [Helm Documentation - Automatically Roll Deployments](https://v3.helm.sh/docs/howto/charts_tips_and_tricks/#automatically-roll-deployments)

Deployment Flags:
-----------

`DEPLOY_ALL` - this means "deploy to all environments" and is set currently until ephemeral environoments for merge requests are setup.

Local Builds:
-----------
Set the following environment variables:

```bash
    brew install maven
    export CI_PROJECT_NAME=helloworld
    export APP_VERSION=1.2.3
    export BUILD_NUMBER=999
    mvn -N io.takari:maven:wrapper
    ./mvnw spring-boot:run
```

Note: These env vars are placeholders for local build. Gitlab CI will use its own `CI_PROJECT_NAME` and pick `APP_VERSION` and `BUILD_NUMBER` from [.gitlab-ci.yml](https://gitlab.com/tmobile/templates_projects/helloworld-1/blob/tmo/master/.gitlab-ci.yml)

Note: `mvnw` [Maven wrapper](https://github.com/takari/maven-wrapper) auto-downloads Maven on a dev's machine who doesn't yet have it installed. 

Windows:

```bash
    wget https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd
```

If you want to include the private maven package registry, you will need to have a default settings.xml, download latest with the following commands:

```bash
export GITLAB_PAT=$(cat ~/glpat)
export TEMPLATES_PROJECT_ID=11160640
export TEMPLATES_TRUNK_BRANCH=tmo/master
curl --location --header "PRIVATE-TOKEN: ${GITLAB_PAT}" "https://gitlab.com/api/v4/projects/${TEMPLATES_PROJECT_ID}/jobs/artifacts/${TEMPLATES_TRUNK_BRANCH}/raw/files/maven/settings.xml?job=artifact-build" -o settings.xml

```

Test Coverage Report Using Jacoco
==================================

```bash
    mvn -Djacoco.destFile=target/jacoco.exec clean jacoco:prepare-agent package
```

The XML and HTML reports will be located under:

- projecthome/target/site/jacoco/index.html
- projecthome/target/clover/site/jacoco.xml

Running the App:
---------------

```bash
    mvn spring-boot:run
```

OR

```bash
    java -jar target/helloworld-1.2.3-999.jar 
```

Sanity verify:
------------------------
Each deployment tier (prod, npe, review) has it's own static child pipeline demonstrating trunk based development.

Each individual deployment has a subsequent sanity job which verifies the results of the deployment just before it.

The verifications found in `src/test/helloworld.postman_collection.json` are basic in nature but intended to be as configurable as necessary. The basic checks include inspecting the returned json to ensure it matches what we would expect.

Below are equivalent curl examples for verifying one of the production endpoints. Replace the host with the environment you want to check.

```console
curl https://helloworld.apps.px-cde02.cf.t-mobile.com/actuator/info
curl https://helloworld.apps.px-cde02.cf.t-mobile.com/actuator/health

curl --user testuser1:password1 https://helloworld.apps.px-cde02.cf.t-mobile.com
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

Helloworld - Kubernetes Blue/Green and Canary Deployments:
-----------------------------------------------------------

The helloworld deployments to the Conducktor platform follows the [Flagger](https://docs.flagger.app/) based Kubernetes [BLUE/GREEN](https://docs.flagger.app/usage/deployment-strategies#blue-green-deployments) deployment strategy in [Staging](.gitlab-ci/deploy/helloworld/k8s-conducktor/npe-stg.yaml) and the Kubernetes [CANARY](https://docs.flagger.app/usage/deployment-strategies#canary-release) deployment strategy in [Production](.gitlab-ci/deploy/helloworld/k8s-conducktor/prod.yaml) environments. 

For achieving the same the helloworld project makes use of (`extends`) the [helm deployment community template](https://gitlab.com/tmobile/templates/-/blob/tmo/master/gitlab-ci/.tmo.function.helm-deploy.gitlab-ci.yml) jobs, `.helm_deploy`, for deployment and `.k8s_promote_app`, for application promotion post validation. The detailed documentation on how to leverage the helm deployment community template for achieving the Kubernetes CANARY or BLUE/GREEN deployment for an application is available [here](https://gitlab.com/tmobile/templates/-/blob/tmo/master/documentation/K8s-HELM.md#canaryblue_green-deployments).

**How to validate/test helloworld Blue/Green or Canary Deployments?**

The [scheduled pipeline](https://gitlab.com/tmobile/templates_projects/helloworld/-/pipelines/631097690) that runs for the helloworld project `tmo/main` branch has validation jobs for stage and prod pipelines. The jobs validate both blue/green and canary deployments as part of the pipeline and auto trigger the respective promotion jobs, once the validation is successful. For other `tmo/main` branch pipeline runs (manual/merged) the promotion jobs are kept manual and hence can be leveraged for manual validation using any of the below helloworld app endpoints.

- [PROD](.gitlab-ci/deploy/helloworld/k8s-conducktor/prod.yaml) - (K8S_DEPLOY_STRATEGY: CANARY"):
  - [API - /api/deploy/info](https://helloworld.duck-plb-w2.kube.t-mobile.com/api/deploy/info)
  - [WEB - /deploy/info](https://helloworld.duck-plb-w2.kube.t-mobile.com/deploy/info)

- [STAGE](.gitlab-ci/deploy/helloworld/k8s-conducktor/npe-stg.yaml) - (K8S_DEPLOY_STRATEGY: "BLUE_GREEN"):
   - [API - /api/deploy/info](http://helloworld.duck-stg-w2.kube.t-mobile.com/api/deploy/info)
   - [WEB - /deploy/info](http://helloworld.duck-stg-w2.kube.t-mobile.com/deploy/info)

`PS:` The above application endpoints have enough details pertaining to a specific code deployment and the deployment pipeline, that will help identify whether the application response is being served from the canary version(`N+1`) or the primary version(`N`) of the application.

**Post Deployment:** 

The below pipeline is a merged pipeline(triggered on MR merge to `tmo/main`) for the helloworld project. The promotion jobs here need manual trigger to promote the `N+1` version of the application.

- [Stage](https://gitlab.com/tmobile/templates_projects/helloworld/-/pipelines/631044366)
- [Prod](https://gitlab.com/tmobile/templates_projects/helloworld/-/pipelines/631044364)

So at this stage if we hit any of the above endpoints, we can validate the canary version(`N+1`) based on the deploy strategy. For instance,

- **In Stage:** The deploy strategy followed is [K8S_DEPLOY_STRATEGY: "BLUE_GREEN"](https://gitlab.com/tmobile/templates_projects/helloworld/-/blob/tmo/main/.gitlab-ci/deploy/helloworld/k8s-conducktor/npe-stg.yaml#L12) and so to validate the canary application post the deployment and before promotion, we would need to hit the application endpoint with a cookie or header value in the request. This will bring up the version `N+1` that is deployed as part of the latest pipeline. `PS:` In staging and prod deployments if the commit sha is not changing as part of your deployment, then the difference in response for the primary and canary version can be seen based on the `pipelineId` (for api) and the field, `GitLab CD Pipeline` (for web) for the respective deployments.

   - Default/Primary version, (`N`) of the application that is taking traffic.
    ```shell
    curl https://helloworld.duck-stg-w2.kube.t-mobile.com/api/deploy/info
    ```
   - Header value based routing to canary version, (`N+1`)
    ```shell
    curl -H "canary:always" https://helloworld.duck-stg-w2.kube.t-mobile.com/api/deploy/info
    ```
    - Cookie based routing to canary version, (`N+1`)
    ```shell
    curl -b "canary=always" https://helloworld.duck-stg-w2.kube.t-mobile.com/api/deploy/info
    ```

In the case of browser based validation, user can make use of the respective Web endpoints and to see the canary version,(`N+1`) set the cookie, `canary` to `always` in the browser.`Note:` A browser based extension can be utilized for setting a cookie for the specific site, OR the user can leverage the `ADD COOKIE` option seen at the bottom of the application web page to see the canary response for the same endpoint. Please be aware that the cookie addition behaves similar to how we add a cookie to the browser page manually and avoids the need for the user to utilize a specific extension for the same. Once the cookie is available, then the option changes to `REMOVE COOKIE` for removal of the same, so that user can go back to the version `N` of the helloworld application that is currently taking traffic.

- **In Prod:** The deploy strategy followed is [K8S_DEPLOY_STRATEGY: "CANARY"](https://gitlab.com/tmobile/templates_projects/helloworld/-/blob/tmo/main/.gitlab-ci/deploy/helloworld/k8s-conducktor/prod.yaml#L12) and so to validate the canary application post the deployment and before promotion, user would need to repeatedly hit the endpoint and intermittenty user will be redirected to the canary version(`N+1`) of the application in between the primary/prod version `N` that is taking traffic. The frequency with  which we see the canary responses is solely based on the canary percentage weightage mentioned for the application. The hellworld project makes use of the default weightages for canary that is set in the [default canary yaml](https://gitlab.com/tmobile/templates/-/blob/tmo/master/files/k8s/flagger/canary.yaml.j2) sourced via the templates. 

**Post Promotion:** 

Post the manual promotion of the application, if we hit the respective web and api endpoints the version served will always be the latest, `N+1` and the version `N` will no longer be accessible. The cookie/header values won't have any significance post promotion of the application and irrespective of their presence the application will always respond with the version, `N+1`.

**Debugging Helm and jinja2**

```bash
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
pip install j2cli
rm -rf k8s/values.yaml k8s/Chart.yaml
j2 k8s/values.yaml.j2 -o k8s/values.yaml
j2 k8s/Chart.yaml.j2 -o k8s/Chart.yaml
helm template --debug k8s -f k8s/values.yaml
```
