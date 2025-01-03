package com.mllukasik.github;

import com.mllukasik.github.exception.GithubClientException;
import com.mllukasik.robusta.util.Strings;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class GithubClient {

    private static final String GITHUB_URI = "api.github.com";

    HttpClient httpClient;

    private GithubClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public static GithubClient create() {
        return new GithubClient();
    }

    public List<Repository> fetchUsersRepositories(String userName) {
        var request = ClassicRequestBuilder.get().setUri(usersRepositoriesUri(userName)).build();
        return List.of(executeRequest(request, Repository[].class));
    }

    public Repository fetchUserRepository(String username, String repositoryName) {
        var request = ClassicRequestBuilder.get().setUri(usersRepository(username, repositoryName)).build();
        return executeRequest(request, Repository.class);
    }

    public String fetchReadme(Repository repository) {
        var request = ClassicRequestBuilder.get()
                .setUri(repository.buildLinkToReadme())
                .setHeader(HttpHeaders.ACCEPT, "application/vnd.github.raw+json")
                .build();
        return executeRequest(request, String.class);
    }

    private URI usersRepository(String username, String repositoryName) {
        try {
            return githubUri().appendPath("repos").appendPath(username).appendPath(repositoryName).build();
        } catch (URISyntaxException ex) {
            throw new GithubClientException(ex);
        }
    }

    private URI usersRepositoriesUri(String userName) {
        try {
            return githubUri().appendPath("users").appendPath(userName).appendPath("repos").build();
        } catch (URISyntaxException ex) {
            throw new GithubClientException(ex);
        }
    }

    private URIBuilder githubUri() {
        return new URIBuilder().setScheme("https").setHost(GITHUB_URI);
    }

    private <T> T executeRequest(ClassicHttpRequest request, Class<T> classType) {
        try {
            var token = System.getenv("GITHUB_TOKEN");
            if (Strings.isNotBlank(token)) {
                request.addHeader(HttpHeaders.AUTHORIZATION, "bearer " + token);
            }
            var handler = ResponseHandlerFactory.handlerByType(classType);
            return httpClient.execute(request, handler);
        } catch (IOException ex) {
            throw new GithubClientException(ex);
        }
    }
}
