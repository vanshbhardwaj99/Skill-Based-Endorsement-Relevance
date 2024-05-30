package com.skill.endorsement.entrypoint;


import com.skill.endorsement.entrypoint.DTO.EndorsementRequestDTO;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;
import com.skill.endorsement.entrypoint.response.ResponseComposer;
import com.skill.endorsement.service.EndorsementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endorsements")
public class EndorsementEntryPoint {

    private final EndorsementService endorsementService;

    public EndorsementEntryPoint(EndorsementService endorsementService){
        this.endorsementService = endorsementService;
    }

    @PostMapping(value="")
    public ResponseEntity<String> createEndorsement(@RequestBody EndorsementRequestDTO endorsementRequestDTO){
        try{
            EndorsementResponse response = endorsementService.createEndorsement(endorsementRequestDTO);
            return new ResponseEntity<>(ResponseComposer.createJSONResponse(response), HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Failed to create endorsement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/{userId}")
    public ResponseEntity<String> getEndorsementById(@PathVariable String userId){
        try{
            List<EndorsementResponse> response = endorsementService.getEndorsementsById(userId);
            return new ResponseEntity<>(ResponseComposer.createJSONResponse(response), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Failed to retrieve endorsement for given user Id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
