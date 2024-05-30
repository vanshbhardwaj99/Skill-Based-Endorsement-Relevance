package com.skill.endorsement.domain;


import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public record Skill(@Id String id, String name) {
}
