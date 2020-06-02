package com.mechanitis.demo.sense.service;

import java.util.Arrays;

public class Monitor {

    public static void main(String[] args) {
        ProcessHandle.allProcesses()
                     .map(ProcessHandle::info)
                     .filter(processInfo -> processInfo.user()
                                                       .filter(user -> user.equals("trishagee"))
                                                       .isPresent())
                     .filter(processInfo -> processInfo.command()
                                                       .filter(command -> command.contains("java"))
                                                       .isPresent())
                     .filter(processInfo -> processInfo.arguments().isPresent())
                     .flatMap(processInfo -> processInfo.arguments().stream()) // new in Java 9
                     .flatMap(Arrays::stream)
                     .filter(s -> s.matches("^com\\.mechanitis.*"))
                     .forEach(processInfo -> System.out.println("processInfo = " + processInfo));

        final long pid = ProcessHandle.current().pid();
        System.out.println("pid = " + pid);
    }
}
