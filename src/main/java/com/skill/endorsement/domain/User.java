package com.skill.endorsement.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Set;

@Node
public record User(@Id String id, String name, int experience, String domain, Set<String> coworkers, Set<String> skills) {
}
