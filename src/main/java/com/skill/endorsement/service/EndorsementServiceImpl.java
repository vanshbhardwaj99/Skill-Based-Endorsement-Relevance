package com.skill.endorsement.service;


import com.skill.endorsement.domain.Skill;
import com.skill.endorsement.domain.User;
import com.skill.endorsement.entrypoint.DTO.EndorsementRequestDTO;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;
import com.skill.endorsement.repository.SkillRepository;
import com.skill.endorsement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EndorsementServiceImpl implements EndorsementService{


    private final UserRepository userRepository;

    private final SkillRepository skillRepository;

    public EndorsementServiceImpl(UserRepository userRepository, SkillRepository skillRepository){
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }
    @Override
    public EndorsementResponse createEndorsement(EndorsementRequestDTO endorsementRequestDTO){
        return null;
//        CompletableFuture<Optional<User>> reviewer
//                = CompletableFuture.supplyAsync(() -> userRepository.findById(endorsementRequestDTO.reviewerUserId()));
//        CompletableFuture<Optional<User>> reviewee
//                = CompletableFuture.supplyAsync(() -> userRepository.findById(endorsementRequestDTO.revieweeUserId()));
//
//        CompletableFuture<Skill> skill
//                = CompletableFuture.supplyAsync(() -> skillRepository.);
//
//        CompletableFuture<Void> combinedFuture
//                = CompletableFuture.allOf(reviewer, reviewee, skill);
//
//
//        combinedFuture.get();
    }


    @Override
    public List<EndorsementResponse> getEndorsementsById(String userId){
        return null;
    }
}
