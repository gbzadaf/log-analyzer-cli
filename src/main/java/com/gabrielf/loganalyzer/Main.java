package com.gabrielf.loganalyzer;


import picocli.CommandLine;


public class Main {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LogAnalyzerCommand()).execute(args);
        System.exit(exitCode);

    }

}
