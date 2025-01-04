package com.mllukasik.generate;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "fetch", version = "1.0.0-SNAPSHOT")
public class FetchEntrypoint implements Callable<Integer> {

    @Option(names = {"-n", "--name"}, required = true, description = "Github account name")
    private String name;

    @Option(names = {"-o", "--output"}, required = true, description = "Output directory", defaultValue = "build")
    private String output;
    private final FetchService fetchService = new FetchService();

    @Override
    public Integer call() throws Exception {
        fetchService.generate(name, output);
        return 0;
    }
}
