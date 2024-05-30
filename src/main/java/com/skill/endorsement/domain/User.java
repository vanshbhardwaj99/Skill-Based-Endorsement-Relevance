package com.skill.endorsement.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public record User(@Id String userId, String name, int experience, String domain) {
}
