package com.skill.endorsement.service;


import com.skill.endorsement.entrypoint.DTO.EndorsementRequestDTO;
import com.skill.endorsement.entrypoint.response.EndorsementResponse;

import java.util.List;
import java.util.Map;

public interface EndorsementService {

    EndorsementResponse createEndorsement(EndorsementRequestDTO endorsementRequestDTO);


    Map<String, List<EndorsementResponse>> getEndorsementsById(String userId);


}
