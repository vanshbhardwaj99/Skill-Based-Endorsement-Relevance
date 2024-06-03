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
        //return null;
        try {
            CompletableFuture<Optional<User>> reviewerFuture
                    = CompletableFuture.supplyAsync(() -> userRepository.findById(endorsementRequestDTO.reviewerUserId()));
            CompletableFuture<Optional<User>> revieweeFuture
                    = CompletableFuture.supplyAsync(() -> userRepository.findById(endorsementRequestDTO.revieweeUserId()));

            CompletableFuture<Optional<Skill>> skillFuture
                    = CompletableFuture.supplyAsync(() -> skillRepository.findById(endorsementRequestDTO.skill()));

            CompletableFuture<Void> combinedFuture
                    = CompletableFuture.allOf(reviewerFuture, revieweeFuture, skillFuture);


            combinedFuture.get();

            Optional<User> reviewer = reviewerFuture.get();
            Optional<User> reviewee = revieweeFuture.get();
            Optional<Skill> skill = skillFuture.get();

            userRepository.createEndorsement(reviewer.get().id(), reviewee.get().id(), skill.get().name(), endorsementRequestDTO.score(), 0.0);

            return new EndorsementResponse(endorsementRequestDTO.reviewerUserId(), skill.get().name(), endorsementRequestDTO.score(), 0.0);

        }
        catch(Exception ex){
            //Log the exception, here I am using this just for an example
            System.out.println(" Exception inside EndorsementServiceImpl-->createEndorsement " +  ex);
        }

        return null;
    }


    @Override
    public List<EndorsementResponse> getEndorsementsById(String userId){
        try {
            User user = userRepository.findById(userId).orElse(null);
            return userRepository.findEndorsements(user.id());
        }
        catch(Exception ex){
            //Log the exception, here I am using this just for an example
            System.out.println(" Exception inside EndorsementServiceImpl-->getEndorsementsById " +  ex);
        }

        return null;
    }
}
