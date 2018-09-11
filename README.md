# Development Challenge

The code provided implementes required micro-services:
	
	1.- Review Service: includes CRUD operation for resource /review/{product_id}
	2.- Product Service: exposes resource /product/{product_id} and returns product details 
	retrieved from Adidas API along with the review details obtained from Review Service

### Prerequisites

Following software and libraries have been used to develop, build, test and run this code:

* Eclipse
* Maven
* SpringBoot
* Swagger


### Installing and running the app

To build the project, we can use maven build command. All dependencies are managed by 
maven which is responsible of downloading them from remote repositories if needed.

Command used to build the solution is:

    mvn build

Each service can be launched independiently by running:

    mvn spring-boot:run
	
Review service is published in http://localhost:9002/review/{product_id}
Product service is published in http://localhost:9001/product/{product_id}

Swagger allows to check API doc and also to test the methods by accessing via:

http://localhost:9002/swagger-ui.html for Review service and
http://localhost:9001/swagger-ui.html for Product service

## Running the tests

Unit tests can be easily run with same command as it was used to build the code:

    mvn test

## Improvements before going live

* DataBase engine used to persist product reviews should be replace, as for the challenge H2 in memory database have been used. 
* User management should be include to improve Authentication on write methods. This would also allow to know the user which posted the review.
* Continuous integration tools such as Jenkins or TeamCity must be configured. 
		The flow would be: checkout the code -> building -> unit testing -> package 
		Then depending on wether the version of the code is being prepared to go live or it is aimed to be tested on SIT or UAT environments it can 
		be deploy on specific environments. Continuous Delivery tools takes part on this step of the flow.
* Add SSL certificates to both services.

## Authors

* **Javier Martinez Sancho** 



