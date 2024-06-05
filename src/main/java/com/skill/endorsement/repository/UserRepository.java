package com.skill.endorsement.repository;

import com.skill.endorsement.domain.User;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("MATCH (r:User {id: $reviewerId}), (e:User {id: $revieweeId}), (s:Skill {name: $skillName}) " +
            "MERGE (r)-[endorse:ENDORSES {name: $skillName, score: $score, relevance: $relevanceScore, reasons: $reasons}]->(e)")
    void createEndorsement(String reviewerId, String revieweeId, String skillName, int score, double relevanceScore, Set<String> reasons);

    @Query("MATCH (e:User {id: $userId})<-[endorse:ENDORSES]-(r:User), (s:Skill {name: endorse.name}) " +
            " RETURN r.id AS reviewerId, r.name as name, r.experience as experience, r.domain as domain, s.name AS skill, endorse.score AS score, endorse.relevance AS relevance, endorse.reasons as reasons ")
    List<EndorsementResponse> findEndorsements(String userId);

}
