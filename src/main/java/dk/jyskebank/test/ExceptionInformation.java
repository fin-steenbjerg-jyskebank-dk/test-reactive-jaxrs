package dk.jyskebank.test;

import jakarta.json.bind.annotation.JsonbProperty;

public record ExceptionInformation(String cause, @JsonbProperty("exception-class") String exceptionClass, String message) {
    public static final String MEDIA_TYPE = "application/vnd.mycompany.error.v1+json";

    public ExceptionInformation(String cause, Exception e) {
        this(cause, e.getClass().getName(), e.getMessage());
    }
}