package com.skill.endorsement.service;


import com.skill.endorsement.entrypoint.DTO.EndorsementRequestDTO;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;

import java.util.List;

public interface EndorsementService {

    EndorsementResponse createEndorsement(EndorsementRequestDTO endorsementRequestDTO);


    List<EndorsementResponse> getEndorsementsById(String userId);


}
