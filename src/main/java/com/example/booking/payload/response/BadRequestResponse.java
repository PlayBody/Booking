package com.example.booking.payload.response;

import java.util.HashMap;
import java.util.Map;

public class BadRequestResponse {
    Map<String, String> errors;

    public BadRequestResponse() {
        this.errors = new HashMap<>();
    }
    public BadRequestResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addErrors(String key, String value) {
        this.errors.put(key, value);
    }
}
