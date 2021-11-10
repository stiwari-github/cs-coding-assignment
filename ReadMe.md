# Getting Started

### Reference Documentation

Follow these steps to run and build application:
------------------------------------------------
1. run [mvn clean install] from /cs-coding-assignment folder.
   Note : If running this command from git bash, make sure [JAVA_HOME] and [M2_HOME] is configured in system environment variable.
   
2. Execute [mvn spring-boot:run] from /cs-coding-assignment folder.
3. APIs can be tested using curl or any rest client tool like postman, reqbin etc.

Sample Curl Commands:
----------------------

1. API to save log events from text file to hsql database: [/logs/save-events]
-----------------------------------------------------------
Method: POST
Request Params: filePath (Mandatory)
Sample Response: 'Log events have been saved successfully'

curl -X POST http://localhost:8080/logs/save-events?filePath=C%3A%5CUsers%5Ciamsa%5CDocuments%5Clog-file.txt -H 'Content-Type: application/json'
--------------------------------------------------------------------
2. API to get all saved events from text file: [/logs/all-events]

Method: GET
Request Params:None
Sample Response:
[{
    "id": "1",
    "duration": 9,
    "type": null,
    "host": null,
    "alert": true
}, {
    "id": "2",
    "duration": 3,
    "type": "APPLICATION_LOG",
    "host": "localhost",
    "alert": false
}, {
    "id": "3",
    "duration": 44,
    "type": null,
    "host": null,
    "alert": true
}]

curl GET http://localhost:8080/logs/all-events
----------------------------------------------------------------------
3. API to get events with alerts flag as true: [/logs/logs-with-alert]

Method: GET
Request Params: None
Sample Response:
[{
    "id": "1",
    "duration": 9,
    "type": null,
    "host": null,
    "alert": true
}, {
    "id": "3",
    "duration": 44,
    "type": null,
    "host": null,
    "alert": true
}]

curl GET http://localhost:8080/logs/logs-with-alert
------------------------------------------------------------------------------