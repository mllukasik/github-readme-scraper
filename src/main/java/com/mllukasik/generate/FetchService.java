package com.mllukasik.generate;

import com.mllukasik.github.GithubClient;
import com.mllukasik.github.Repository;
import com.mllukasik.github.exception.GithubClientException;
import com.mllukasik.robusta.util.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

class FetchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchService.class);

    private final GithubClient githubClient;
    private final MetaDataGenerator metaDataGenerator;

    FetchService() {
        githubClient = GithubClient.create();
        metaDataGenerator = new MetaDataGenerator();
    }

    void generate(String name, String output) throws IOException {
        var outputPath = Path.of(output);
        Paths.ensureDirectoryExists(outputPath);
        var repositories = githubClient.fetchUsersRepositories(name);
        for (var repository : repositories) {
            var readme = fetchReadme(repository);
            if (readme.isEmpty()) {
                continue;
            }
            var path = outputPath.resolve(repository.name() + ".md");
            var content = metaDataGenerator.metadata(repository) + readme.get();
            writeToFile(path, content);
        }
    }

    private void writeToFile(Path path, String content) throws IOException {
        try (var writer = new FileWriter(path.toFile())) {
           writer.write(content);
        }
    }

    private Optional<String> fetchReadme(Repository repository) {
        try {
            var readme = githubClient.fetchReadme(repository);
            return Optional.of(readme);
        } catch (GithubClientException exception) {
            LOGGER.error("Could not fetch readme file for " + repository.name() + "repository");
            return Optional.empty();
        }
    }
}
