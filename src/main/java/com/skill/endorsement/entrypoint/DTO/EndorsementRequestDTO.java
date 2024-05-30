package com.skill.endorsement.entrypoint.DTO;

public record EndorsementRequestDTO(String revieweeUserId, String reviewerUserId, String skill, int score) {
}
