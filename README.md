Introduction:
-------------
This repository contains a sample spring boot Hello World example

Requirement:
-----------

Compile:
-------
To compile the app please run the following commad:

	mvn clean package

Test Coverage Report Using Clover:
==================================
	mvn clean clover:setup test clover:aggregate clover:clover

The PDF and HTML reports will be located under:
projecthome/target/clover/site/index.html
projecthome/target/clover/site/clover.pdf
	
Running the App:
---------------
    mvn spring-boot:run
    
OR

    java -jar target/edp-helloworld-rest-0.1.0.jar 

Testing the Application:
------------------------

	curl http://localhost:19000/greeting?name=Moto

In Web Browser

    http://localhost:19000/greeting?name=Moto
    
Deploying to Pivotal Cloud Foundry:
-----------------------------------
The project can be deployed to PCF with relative ease, there is manifest.yml file is created
for deployment purpose.

Simply login into PCF using
    
    cf login
and pushing the app using the following command

    cf push
    
    
