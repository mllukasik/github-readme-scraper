package com.mllukasik.github;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record Repository(

        String name,
        String url,

        @JsonProperty("private")
        Boolean isPrivate,

        @JsonProperty("html_url")
        String htmlUrl,

        @JsonProperty("created_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt
) {

    String buildLinkToReadme() {
        return url + "/contents/README.md";
    }

    public boolean isPublic() {
        return !isPrivate;
    }
}
