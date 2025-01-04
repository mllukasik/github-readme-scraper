package com.mllukasik.github.exception;

public class GithubClientException extends RuntimeException {

    public GithubClientException(Throwable cause) {
        super(cause);
    }

    public GithubClientException(String message) {
        super(message);
    }
}
