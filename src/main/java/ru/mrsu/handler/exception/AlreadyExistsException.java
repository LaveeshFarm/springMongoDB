package ru.mrsu.handler.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseApiException {
    private AlreadyExistsException(String code, String message, String description) {
        super(code, message, description);
    }


    public String toString() {
        return "AlreadyExistsException()";
    }

    public static class Builder {
        private String code = "conflict", message, description;

        private Builder() {
            this.message = HttpStatus.CONFLICT.getReasonPhrase();
            this.description = "Объект уже существует";
        }

        public static AlreadyExistsException.Builder alreadyExistsException() {return new AlreadyExistsException.Builder();}

        public AlreadyExistsException.Builder code(String code) {
            this.code = code;
            return this;
        }

        public AlreadyExistsException.Builder message(String message) {
            this.message = message;
            return this;
        }

        public AlreadyExistsException.Builder description(String description) {
            this.description = description;
            return this;
        }

        public AlreadyExistsException build() {
            return new AlreadyExistsException(this.code, this.message, this.description);
        }
    }
}
