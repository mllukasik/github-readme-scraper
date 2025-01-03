package com.mllukasik.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mllukasik.github.exception.GithubClientException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/*todo: why this is utility class ?*/
final class ResponseHandlerFactory {

    private ResponseHandlerFactory() {
    }

    public static <T> HttpClientResponseHandler<T> handlerByType(Class<T> classType) {
        return (ClassicHttpResponse response) -> {
            validateStatus(response.getCode());
            if (classType == String.class) {
                return classType.cast(readFromInputStream(response.getEntity().getContent()));
            }
            return handleJsonResponse(response, classType);
        };
    }

    private static void validateStatus(int code) {
        if (code != HttpStatus.SC_OK) {
            throw new GithubClientException("Response code is not ok");
        }
    }

    private static String readFromInputStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream)).lines()
                .collect(Collectors.joining("\n"));
    }

    private static <T> T handleJsonResponse(ClassicHttpResponse httpResponse, Class<T> classType) throws IOException {
        var content = httpResponse.getEntity().getContent();
        return buildJsonMapper().readValue(content, classType);
    }

    private static ObjectMapper buildJsonMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
