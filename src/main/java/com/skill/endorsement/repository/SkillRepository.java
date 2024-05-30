package com.skill.endorsement.repository;


import com.skill.endorsement.domain.Skill;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill, String> {
}
