package com.mllukasik;

import com.mllukasik.generate.FetchEntrypoint;
import picocli.CommandLine;

public final class Application {

    private Application() {
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FetchEntrypoint())
                .execute(args);
        System.exit(exitCode);
    }
}
