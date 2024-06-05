package com.skill.endorsement.repository;


import com.skill.endorsement.domain.Skill;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill, String> {

    @Query("MATCH (start:Skill {name: $domain1}), (end:Skill {name: $domain2}), " +
            "p = shortestPath((start)-[:PARENT_SKILL|CHILD_SKILL*]->(end)) " +
            "RETURN reduce(weight = 0.0, r in relationships(p) | weight + r.weight) AS totalWeight")
    Double findShortestPathWeightBetweenDomains(String domain1, String domain2);

}
