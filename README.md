# Skill-Based-Endorsement-Relevance
Skill-Based Endorsement Relevance Assessment System


This project is built on Java(21), Spring boot(v3.3.0), and gradle as the build tool. It uses neo4j as the database.
The username/password can be configured in application.properties file.

This project requires neo4j-graph-data-science-2.6.7.jar to be included inside plugins folder in the neo4j database file explorer.

neo4jqueries.sql file is included in this repository at the path: src/main/resources/static/neo4jqueries.sql
This contains a list of CQL(Cypher Query Language) as a mini sample dataset. Please execute these CQL in order to test the APIs.

NOTE: The API body values have to be entered as per the 
This project mainly exposes 2 APIs:

1) Post Endorsement API: Allows a reviewer to endorse a reviewee for a specific skill with a score, and returns a weighted relevance score considering the reviewee's and reviewer's profiles.
API Design-

HTTP: POST
URI: http://127.0.0.1:8080/endorsements
Body: 
{
    "revieweeUserId": "",
    "reviewerUserId": "",
    "skill": "",
    "score": ""
}

Sample Response JSON:
{
    "reviewerId": "4",
    "skill": "Java",
    "score": 4,
    "relevance": 1.7,
    "reasons": [
        "Not Coworkers",
        "Different domains",
        "Experience Gap",
        "Skill gap"
    ]
}





2) Get Endorsements API: Retrieves all the endorsements for a user, displaying each skill with the scores rated by different persons alongside a system-calculated weighted score.

HTTP: GET
URI: http://127.0.0.1:8080/endorsements/{userId}
Body: N/A

Sample Response JSON:

{
    "Java": [
        {
            "reviewerId": "4",
            "skill": "Java",
            "score": 4,
            "relevance": 1.7,
            "reasons": [
                "Not Coworkers",
                "Different domains",
                "Experience Gap",
                "Skill gap"
            ]
        }
    ]
}
   
