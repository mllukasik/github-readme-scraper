package com.mllukasik.generate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mllukasik.github.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataGenerator.class);
    private final ObjectMapper objectMapper;

    public MetaDataGenerator() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
    }

    public String metadata(Repository repository) {
        var metadata = new MetaData(repository);
        try {
            return postfix(objectMapper.writeValueAsString(metadata));
        } catch (JsonProcessingException exception) {
            LOGGER.error("Could not generate metadata", exception);
            return "";
        }
    }

    private String postfix(String metadata) {
        return metadata + "---\n";
    }
}
