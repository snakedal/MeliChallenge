# MeliChallenge
Challenge Meli FindShipAndPosition

Greetings!!

In the following document you will find the necessary information to clone, import and run, a SpingBoot application that has as purpose to find ship Position and Message as specified in the Challenge suggested, the solution is also UP in AWS so if you want to validate the endpoints the URLs to do so are the followings and the steps shown in the local testing part are the same just change the URLs mentioned for this ones:

1. http://findshipandmessage2-env.sa-east-1.elasticbeanstalk.com/topsecret (GET)
2. http://findshipandmessage2-env.sa-east-1.elasticbeanstalk.com/topsecret_split/{satellite_name} (POST)
3. http://findshipandmessage2-env.sa-east-1.elasticbeanstalk.com/topsecret_split/{satellite_name} (GET)

HOW TO CLONE THE PROJECT To clone the project, please, follow the next steps:

Open GitBash.

Go to the path in your local machine where you want to clone the project.

Clone the repository using the URL: https://github.com/snakedal/MeliChallenge.git  it will ask for credentials, please input as user: snakedal and as password: PrivateRepositorySnakedal, wait for the project to download completely

Ok, so now that the project has been successfully downloaded, lets import it to our IDE, in this hand book we are going to use IntelliJ IDE

HOW TO IMPORT THE PROJECT

Open Eclipse IDE and go to the path "File -> Open..." and select the folder FindShipAndMessage inside the folder challengeMeli that was download using the clone instruction, and click the OK button.

It should appear in your IDE Project Explorer, the project "FindShipAndMessage"

EXPLAINING PROJECT FILES The project has one big package: ⦁ "com.challenge" This one has the springBootApplication defined in the class "FindShipAndMessageApplication.java", this class is the one that should be ran to validate the functionality, and inside this package you will find other six packages: 

1. "controller": The controller is were we defined all three RequestMapping that we will use: /topsecret, (POST) /topsecret_split/{satellite_name} and (GET)/topsecret_split/{satellite_name}. 
2. "dto": The DTO is where we will find the Objects used to wrap some valus in Objects to work easier with the values.
3. "model" : The model is the one that has the mapping for the table that will be created in our h2 DB, named SATELLITE_MESSAGES, this table has three columns: "SATELLITE_NAME", DISTANCE and "MESSAGE", this table is used generally to persist the correct messages sent using the API (POST) /topsecret_split/{satellite_name}, please have in mind that the GET API does not persist the message sent, just uses it when the request is made.
4. "repository": This one is where we define the functions for our DB but we only extend it to the class CrudRepository so that we were able to use the functions already defined there. 
5. "service": In this package we define the interface that models the methods that we will be implementing and the class that implements it calls all the proper methos in our util package to do all the hard work calculating our values, or just persisting the message to the H2 DB.
6. "util": In this Packge we define the class in which we will define the methods that are used accros the app to do the proper caculations and some other methods used to simplify the code comprehension. 

RESOURCE FOLDER In the path "src/main/resources" you will find one file: 
1. application.properties This file was only used to define some properties that were needed for the h2 DB connection. 

JUNIT FOLDER At the path "src/test/java" you will find a package named "com.challenge" this package contains the class responsable for the Junits that we thought were pertinent for this problem.

RUNNING THE PROJECT To Run the Project follow these steps:

Go to the class "FindShipAndMessageApplication.java", right click it and run it as a "Spring Boot App"

Wait until you see that everything has been loaded

To validate the three Endpoints exposed by this app you we used postman to send the proper message and get the answer:

local testing

Once the APP is up and running you will be able to send requests to the following URLs:
1. http://localhost:8080/topsecret (GET)
2. http://localhost:8080/topsecret_split/{satellite_name} (POST)
3. http://localhost:8080/topsecret_split/{satellite_name} (GET)

NOTE: Please have in ind that the correct satellite Names are: kenobi, sato and skywalker, if a different name satellite name is sent it will trigger a not successfull reponse.

Example:
For the Endpoint testing please in PostMan select in the Body Tab "Raw" and "JSON", one example message is the ones shown below, also have in mind the tipe of the request POST OR GET for the corresponding Endpoint. 

1. http://localhost:8080/topsecret (GET)

This message should work and show a successfull response

Request:

{
    "satellites": [
        {
        "name": "kenobi",
        "distance": 400.0,
        "message": ["este", "", "", "mensaje", ""]
        },
        {
        "name": "skywalker",
        "distance": 400.5,
        "message": ["", "es", "", "", "secreto"]
        },
        {
        "name": "sato",
        "distance": 899.4,
        "message": ["este", "", "un", "", ""]
        }
    ]
}

Successfull Response :

STATUS CODE 200
{
    "position": {
        "x": -243.09987252667094,
        "y": -406.7061608357012
    },
    "message": "[este, es, un, mensaje, secreto]"
}

if it is needed to validate the not successfull response, it is possible with the following request that has an incorrect satellite name:
Request:

{
    "satellites": [
        {
        "name": "keno",
        "distance": 400.0,
        "message": ["este", "", "", "mensaje", ""]
        },
        {
        "name": "skywalker",
        "distance": 400.5,
        "message": ["", "es", "", "", "secreto"]
        },
        {
        "name": "sato",
        "distance": 899.4,
        "message": ["este", "", "un", "", ""]
        }
    ]
}

Response:

STATUS CODE 404
{
    "timestamp": "2020-09-24T03:34:51.906+0000",
    "status": 404,
    "error": "Not Found",
    "message": "OPS! we were unable to decode the message or find the position",
    "path": "/topsecret"
}

2. http://localhost:8080/topsecret_split/{satellite_name} (POST)

This Endpoint just uses the H2 DB to persist the messages sent, so that this information is used by the GET Endpoint explained After this one, so for this Endpoint a message with the folowing structure is sent for each satellite using the request parameter "satellite_name" to stablish to which satellite is linked the message sent:

{
    "distance": 899.4,
    "message": ["este", "", "un", "", ""]
}

And an informative response is shown saying that the process works OK:
STATUS CODE 200

Message received successfully, use Get Service to determine message and position

if the satellite Name is not a valid One it will show an Error request example to validate Error Response:
URL Example: http://localhost:8080/topsecret_split/skywalke

{
     "distance": 899.4,
     "message": ["este", "", "un", "", ""]
}

Error reponse will be shown as the one below:

STATUS CODE 500

{
    "timestamp": "2020-09-24T03:45:25.519+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Invalid Satellite Name",
    "path": "/topsecret_split/skywalke"
}

3. http://localhost:8080/topsecret_split/{satellite_name} (GET)

This Endpoint uses the values persisted in the H2 DB and the one sent in the message to stablish the position and message of the ship, so in order for this Endpoint to work properly is needed to run successfully the POST service for the three satellites before using it, another way is just have 2 messages persisted in the DB and sending in the request the message for the satellite missing, have in mind that if the message for the satellite is already persisted it will give priority to the one in the DB as this is a GET service we did'n think it was correct for it to do any update Nor insertion to the DB.

A valid Request for this Enpoint is the following, having in mind what was just mentioned beforehand:

{
     "distance": 899.4,
     "message": ["este", "", "un", "", ""]
}

Successfull Response:
STATUS CODE 200
{
    "position": {
        "x": -243.09987252667094,
        "y": -406.7061608357012
    },
    "message": "[este, es, un, mensaje, secreto]"
}

if by example there is not enough information to calculate the position or the message the following response message will be shown:
STATUS CODE 404
{
    "timestamp": "2020-09-24T03:57:50.313+0000",
    "status": 404,
    "error": "Not Found",
    "message": "Not enough information received to get position and message",
    "path": "//topsecret_split/skywalker"
}







