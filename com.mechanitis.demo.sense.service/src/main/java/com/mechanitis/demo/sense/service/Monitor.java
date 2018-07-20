package com.mechanitis.demo.sense.service;

import java.util.Arrays;
import java.util.stream.Stream;

@SuppressWarnings({"WeakerAccess", "DeprecatedIsStillUsed"})
public class Monitor {

    public static void main(String[] args) {
        printProcessInformation();
    }

    /**
     * @deprecated this will be removed,
     * use {@link Monitor#fetchProcessInfo(String, String, String)} ()} instead
     */
    @Deprecated
    static void printProcessInformation() {
        ProcessHandle
        .allProcesses()
        .map(ProcessHandle::info)
        .filter(proc -> proc.user()
                            .filter(user -> user.equals("trishagee"))
                            .isPresent())
        .filter(proc -> proc.command()
                            .filter(command -> command.contains("java"))
                            .isPresent())
        .filter(proc -> proc.arguments().isPresent())
        .flatMap(proc -> proc.arguments().stream())
        .flatMap(Arrays::stream)
        .filter(s -> s.matches("^com\\.mechanitis.*"))
        .forEach(processInfo -> System.out.println("processInfo = " + processInfo));
    }

    private static Stream<String> fetchProcessInfo(final String packageRegex, final String userName, final String processType) {
        return ProcessHandle
        .allProcesses()
        .map(ProcessHandle::info)
        .filter(proc -> proc.user()
                            .filter(user -> user.equals(userName))
                            .isPresent())
        .filter(proc -> proc.command()
                            .filter(command -> command.contains(processType))
                            .isPresent())
        .filter(proc -> proc.arguments().isPresent())
        .flatMap(proc -> proc.arguments().stream())
        .flatMap(Arrays::stream)
        .filter(s -> s.matches(packageRegex));
    }
}
