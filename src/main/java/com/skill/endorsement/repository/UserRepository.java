package com.skill.endorsement.repository;

import com.skill.endorsement.domain.User;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("MATCH (r:User {id: $reviewerId}), (e:User {id: $revieweeId}), (s:Skill {name: $skillName}) " +
            "MERGE (r)-[endorse:ENDORSES {name: $skillName, score: $score, relevance: $relevanceScore}]->(e)")
    void createEndorsement(String reviewerId, String revieweeId, String skillName, int score, double relevanceScore);

    @Query("MATCH (e:User {id: $userId})<-[endorse:ENDORSES]-(r:User), (s:Skill {name: endorse.name}) " +
            " RETURN r.id AS reviewerId, r.name as name, r.experience as experience, r.domain as domain, s.name AS skill, endorse.score AS score, endorse.relevance AS relevance ")
            //(String reviewerId, String skill, int score, double relevance)
          //  " RETURN new com.skill.endorsement.entrypoint.response.EndorsementResponse(r.id, s.name, endorse.score, endorse.relevance) ")
    List<EndorsementResponse> findEndorsements(String userId);

    @Query("MATCH (u1:User {id: $userId1})-[:COWORKER_WITH]-(u2:User {id: $userId2}) RETURN COUNT(*) > 0")
    boolean areCoworkers(String userId1, String userId2);
}
