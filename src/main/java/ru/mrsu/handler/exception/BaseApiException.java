package ru.mrsu.handler.exception;

public abstract class BaseApiException extends RuntimeException {
    private final String code;
    private final String message;
    private final String description;

    protected BaseApiException(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        String var10000 = this.getCode();
        return "BaseApiException(code=" + var10000 + ", message=" + this.getMessage() + ", description=" + this.getDescription() + ")";
    }
}
