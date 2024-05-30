package com.skill.endorsement.entrypoint.response;

import com.google.gson.Gson;

public class ResponseComposer {


    public static <T> String createJSONResponse(T object){
        Gson gson = new Gson();

        return gson.toJson(object);
    }
}
