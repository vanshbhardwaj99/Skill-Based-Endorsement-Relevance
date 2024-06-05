package com.skill.endorsement.entrypoint.response;


import java.util.Set;

public record EndorsementResponse(String reviewerId, String skill, int score, double relevance, Set<String> reasons) {
}
