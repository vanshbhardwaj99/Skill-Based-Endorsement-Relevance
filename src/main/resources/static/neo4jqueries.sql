-- Create Users
CREATE (:User {id: '1', name: 'Vansham', domain: 'Software Engineering', experience: 3, coworkers: ['4','5'], skills: ['2','3','4','5']});
CREATE (:User {id: '2', name: 'Kunal', domain: 'Sales', experience: 8, coworkers: [], skills: ['13']});
CREATE (:User {id: '3', name: 'Abhishek', domain: 'Networking', experience: 5, coworkers: [], skills: ['10', '11']});
CREATE (:User {id: '4', name: 'Kapil', domain: 'Software Engineering', experience: 1, coworkers: ['1', '5'], skills: ['4','5','6','7']});
CREATE (:User {id: '5', name: 'Himanshu', domain: 'Engineering Management', experience: 12, coworkers: ['1','4'], skills: ['1','2']});
CREATE (:User {id: '6', name: 'Rahul', domain: 'Software Engineering', experience: 2, coworkers: [], skills: ['2','4','6']});


--Create Skills
CREATE (:Skill {id: '0', name: 'ROOT SKILL'});
CREATE (:Skill {id: '1', name: 'Software Engineering'});
CREATE (:Skill {id: '2', name: 'Java'});
CREATE (:Skill {id: '3', name: 'Spring Boot'});
CREATE (:Skill {id: '4', name: 'ReactJS'});
CREATE (:Skill {id: '5', name: 'Redux'});
CREATE (:Skill {id: '6', name: 'DBMS'});
CREATE (:Skill {id: '7', name: 'MySQL'});
CREATE (:Skill {id: '8', name: 'Networking'});
CREATE (:Skill {id: '9', name: 'Security'});
CREATE (:Skill {id: '10', name: 'TCP/IP'});
CREATE (:Skill {id: '11', name: 'Linux'});
CREATE (:Skill {id: '12', name: 'Sales'});
CREATE (:Skill {id: '13', name: 'Communication'});
CREATE (:Skill {id: '14', name: 'Relationship Building'});



-- Create relationships between ROOT SKILL and its child skills
MATCH (root:Skill {id: '0'}) , (softwareDev:Skill {id: '1'}), (networking:Skill {id: '8'}), (sales:Skill {id: '12'})
CREATE (root)-[:CHILD_SKILL {weight: 0.5}]->(softwareDev)
CREATE (root)-[:CHILD_SKILL {weight: 0.5}]->(networking)
CREATE (root)-[:CHILD_SKILL {weight: 0.5}]->(sales)
CREATE (softwareDev)-[:PARENT_SKILL {weight: 0.5}]->(root)
CREATE (networking)-[:PARENT_SKILL {weight: 0.5}]->(root)
CREATE (sales)-[:PARENT_SKILL {weight: 0.5}]->(root)

-- Create relationships between Software Development and its child skills
 MATCH (softwareDev:Skill {id: '1'}) , (java:Skill {id: '2'}), (springBoot:Skill {id: '3'}), (reactJS:Skill {id: '4'}), (redux:Skill {id: '5'}), (dbms:Skill {id: '6'})
CREATE (softwareDev)-[:CHILD_SKILL {weight: 0.3}]->(java)
CREATE (java)-[:PARENT_SKILL {weight: 0.3}]->(softwareDev)

CREATE (java)-[:CHILD_SKILL {weight: 0.2}]->(springBoot)
CREATE (springBoot)-[:PARENT_SKILL {weight: 0.2}]->(java)

CREATE (softwareDev)-[:CHILD_SKILL {weight: 0.3}]->(reactJS)
CREATE (reactJS)-[:PARENT_SKILL {weight: 0.3}]->(softwareDev)

CREATE (reactJS)-[:CHILD_SKILL {weight: 0.2}]->(redux)
CREATE (redux)-[:PARENT_SKILL {weight: 0.2}]->(reactJS)

CREATE (softwareDev)-[:CHILD_SKILL {weight: 0.3}]->(dbms)
CREATE (dbms)-[:PARENT_SKILL {weight: 0.3}]->(softwareDev)

-- Create relationships between DBMS and its child skill
MATCH (dbms:Skill {id: '6'}), (mysql:Skill {id: '7'})
CREATE (dbms)-[:CHILD_SKILL {weight: 0.2}]->(mysql)
CREATE (mysql)-[:PARENT_SKILL {weight: 0.2}]->(dbms)

-- Create relationships between Networking and its child skills
MATCH (networking:Skill {id: '8'}) , (security:Skill {id: '9'}), (tcpip:Skill {id: '10'}), (linux:Skill {id: '11'})
CREATE (networking)-[:CHILD_SKILL {weight: 0.3}]->(security)
CREATE (security)-[:CHILD_SKILL {weight: 0.2}]->(tcpip)
CREATE (networking)-[:CHILD_SKILL {weight: 0.3}]->(linux)
CREATE (security)-[:PARENT_SKILL {weight: 0.3}]->(networking)
CREATE (tcpip)-[:PARENT_SKILL {weight: 0.2}]->(security)
CREATE (linux)-[:PARENT_SKILL {weight: 0.3}]->(networking)


-- Create relationships between Sales and its child skills

MATCH (sales:Skill {id: '12'}), (communication:Skill {id: '13'}), (relationshipBuilding:Skill {id: '14'})
CREATE (sales)-[:CHILD_SKILL {weight: 0.3}]->(communication)
CREATE (communication)-[:CHILD_SKILL {weight: 0.2}]->(relationshipBuilding)
CREATE (communication)-[:PARENT_SKILL {weight: 0.3}]->(sales)
CREATE (relationshipBuilding)-[:PARENT_SKILL {weight: 0.2}]->(communication)
