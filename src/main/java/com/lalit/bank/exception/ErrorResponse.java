package com.lalit.bank.exception;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> details;

    public ErrorResponse() {}
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, Map<String, String> details) {
        this.timestamp = timestamp; this.status = status; this.error = error; this.message = message; this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details; }

    public static ErrorResponseBuilder builder() { return new ErrorResponseBuilder(); }
    public static class ErrorResponseBuilder {
        private LocalDateTime timestamp;
        private int status;
        private String error, message;
        private Map<String, String> details;
        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public ErrorResponseBuilder status(int status) { this.status = status; return this; }
        public ErrorResponseBuilder error(String error) { this.error = error; return this; }
        public ErrorResponseBuilder message(String message) { this.message = message; return this; }
        public ErrorResponseBuilder details(Map<String, String> details) { this.details = details; return this; }
        public ErrorResponse build() { return new ErrorResponse(timestamp, status, error, message, details); }
    }
}
