package com.mllukasik.generate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mllukasik.github.Repository;

import java.time.LocalDateTime;

public record MetaData(

        String name,
        String url,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime createdAt
) {

    public MetaData(Repository repository) {
        this(
                repository.name(),
                repository.htmlUrl(),
                repository.createdAt()
        );
    }
}
