package com.mechanitis.demo.sense.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;

public class Monitor {
    private void monitoring() throws IOException {
        ProcessBuilder ls = new ProcessBuilder()
                .command("ls")
                .directory(Paths.get("/")
                        .toFile());
        ProcessBuilder grepPdf = new ProcessBuilder()
                .command("grep", "pdf")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT);
        List<Process> lsThenGrep = ProcessBuilder
                .startPipeline(asList(ls, grepPdf));

        CompletableFuture[] lsThenGrepFutures = lsThenGrep.stream()
                // onExit returns a
                // CompletableFuture<Process>
                .map(Process::onExit)
                .map(future -> future.thenAccept(
                        process -> System.out.println("PID: " + process.pid())))
                .toArray(CompletableFuture[]::new);
        // wait until all processes are finished
        CompletableFuture
                .allOf(lsThenGrepFutures)
                .join();

        long pid = ProcessHandle.current().pid();
    }
}
