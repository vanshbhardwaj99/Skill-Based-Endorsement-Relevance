package com.skill.endorsement.entrypoint.response;


public record EndorsementResponse(String reviewerId, String skill, int score, double relevance) {
}
