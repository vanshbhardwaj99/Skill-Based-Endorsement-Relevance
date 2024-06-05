package com.skill.endorsement.service;


import com.skill.endorsement.domain.Skill;
import com.skill.endorsement.domain.User;
import com.skill.endorsement.entrypoint.DTO.EndorsementRequestDTO;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;
import com.skill.endorsement.repository.SkillRepository;
import com.skill.endorsement.repository.UserRepository;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EndorsementServiceImpl implements EndorsementService{


    private final UserRepository userRepository;

    private final SkillRepository skillRepository;

    private final Neo4jClient neo4jClient;

    public EndorsementServiceImpl(UserRepository userRepository, SkillRepository skillRepository, Neo4jClient neo4jClient){
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.neo4jClient = neo4jClient;
    }

    @Override
    public EndorsementResponse createEndorsement(EndorsementRequestDTO endorsementRequestDTO){
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

            Set<String> reasons = new HashSet<>();

            if(reviewer.isPresent() && reviewee.isPresent() && skill.isPresent()) {
                double relevanceScore = calculateRelevanceScore(reviewer.get(), reviewee.get(), skill.get(), endorsementRequestDTO.score(), reasons);

                userRepository.createEndorsement(reviewer.get().id(), reviewee.get().id(), skill.get().name(), endorsementRequestDTO.score(), relevanceScore, reasons);

                return new EndorsementResponse(endorsementRequestDTO.reviewerUserId(), skill.get().name(), endorsementRequestDTO.score(), relevanceScore, reasons);
            }

        }
        catch(Exception ex){
            //Log the exception, here I am using this just for an example
            System.out.println(" Exception inside EndorsementServiceImpl-->createEndorsement " +  ex);
        }

        return null;
    }


    @Override
    public Map<String, List<EndorsementResponse>> getEndorsementsById(String userId){
        try {
            User user = userRepository.findById(userId).orElse(null);
            List<EndorsementResponse> response =  userRepository.findEndorsements(user.id());
            Map<String, List<EndorsementResponse>> groupedBySkill = response.stream()
                    .collect(Collectors.groupingBy(EndorsementResponse::skill));

            return groupedBySkill;
        }
        catch(Exception ex){
            //Log the exception, here I am using this just for an example
            System.out.println(" Exception inside EndorsementServiceImpl-->getEndorsementsById " +  ex);
        }

        return null;
    }

    private double calculateRelevanceScore(User reviewer, User reviewee, Skill skill, int score, Set<String> reasons){
        double adjustment = 0.0;

        //Coworker
        if(!reviewer.coworkers().contains(reviewee.id())) {
            adjustment += 0.1;
            reasons.add("Not Coworkers");
        }

        //Experience
        if(reviewer.experience() < 2 || (reviewee.experience() - reviewer.experience() > 5)){
            adjustment += 0.2;
            reasons.add("Experience Gap");
        }

        //Domain
        if(!reviewer.domain().equals(reviewee.domain())){
            Double shortestWeight = skillRepository.findShortestPathWeightBetweenDomains(reviewer.domain(), reviewee.domain());
            adjustment += shortestWeight;
            reasons.add("Different domains");
        }

        //Skill
        if(!reviewer.skills().contains(skill.id())){
            adjustment += 0.2;
            reasons.add("Skill gap");
        }
        if(!reviewee.skills().contains(skill.id())){
            adjustment += 0.2;
            reasons.add("Skill gap");
        }

        String query = queryToCheckMinimumSkillsDistance(skill.id(), new ArrayList<>(reviewer.skills()));

        Double minDistanceBetweenSkills = neo4jClient.query(query)
                .fetchAs(Double.class)
                .one()
                .orElse(0.0);

        adjustment += minDistanceBetweenSkills;

        return new BigDecimal(score - adjustment).setScale(1, RoundingMode.DOWN).doubleValue();
    }

    private String queryToCheckMinimumSkillsDistance(String start, List<String> target) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(String.format("MATCH (start:Skill {id: '%s'}), ", start));

        target.forEach(skillId -> queryBuilder.append(String.format("(target%s:Skill {id: '%s'})", skillId, skillId)).append(" ,"));

        queryBuilder.deleteCharAt(queryBuilder.length() - 1);

        // Calculate shortest paths and distances
        queryBuilder.append(" WITH start, ");

        target.forEach(skillId -> queryBuilder.append(String.format("target%s, ", skillId)));

        target.forEach(skillId -> queryBuilder.append(String.format(" shortestPath((start)-[:PARENT_SKILL|CHILD_SKILL*]->(target%s)) AS pathToTarget%s",
                skillId, skillId)).append(" ,"));

        queryBuilder.deleteCharAt(queryBuilder.length() - 1);

        queryBuilder.append(" WITH start, ");
        target.forEach(skillId -> queryBuilder.append(String.format("pathToTarget%s, ", skillId)));

        target.forEach(skillId -> queryBuilder.append(String.format("reduce(weight = 0.0, r in relationships(pathToTarget%s) | weight + r.weight) AS distanceToTarget%s", skillId, skillId))
                .append(" ,"));

        queryBuilder.deleteCharAt(queryBuilder.length() - 1);


        // Return the minimum distance
        queryBuilder.append("  RETURN ");
        for (int i = 0; i < target.size(); i++) {
            if (i == 0) {
                queryBuilder.append(" CASE ");
            }
            queryBuilder.append(String.format(" WHEN pathToTarget%s IS NULL THEN distanceToTarget%s ", target.get(i), target.get(i)));
            if (i < target.size() - 1) {
                queryBuilder.append(String.format(" WHEN distanceToTarget%s < distanceToTarget%s THEN distanceToTarget%s ", target.get(i), target.get((i + 1) % target.size()), target.get(i)));
            } else {
                queryBuilder.append(" ELSE 0 END ");
            }
        }
        queryBuilder.append(" AS minDistance");


        return queryBuilder.toString();
    }
}
