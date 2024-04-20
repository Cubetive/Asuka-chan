package com.fubukigrin.utilities;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvConfig {
    private final Dotenv config;

    public DotenvConfig() {
        config = Dotenv.configure().load();
    }

    public Dotenv getConfig() {
        return config;
    }
}
