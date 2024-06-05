package com.skill.endorsement.entrypoint.response;

import com.google.gson.Gson;

import java.util.Objects;

public class ResponseComposer {


    public static <T> String createJSONResponse(T object){
        if(Objects.isNull(object)) return "Data issue";

        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
