package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.StubService;

import java.util.Random;

public class UserTestData {
    private static final String[] EXAMPLE_HANDLES = new String[]{"aaa", "bbb",
            "ccc", "ddd", "eee", "fff", "gee", "ggg", "hhh", "iii", "jjj",
            "kkk", "lll", "mmm", "nnn", "ooo", "ppp", "qqq", "rrr", "sss",
            "ttt", "uuu", "vvv", "www", "xxx", "yyy", "zzz"};

    private static final Random random = new Random();

    public static void main(String[] args) {
        StubService stubUserService = new StubService("/users/", 8083, UserTestData::getRandomTwitterHandle);
        stubUserService.run();
    }

    private static String getRandomTwitterHandle() {
        return EXAMPLE_HANDLES[random.nextInt(27)];
    }
}
