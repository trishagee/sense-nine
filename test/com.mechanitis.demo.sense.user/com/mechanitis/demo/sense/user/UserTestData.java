package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.StubService;

import java.util.Random;
import java.util.Set;

class UserTestData {
    private static final Set<String> EXAMPLE_HANDLES = Set.of("aaa", "bbb",
            "ccc", "ddd", "eee", "fff", "gee", "ggg", "hhh", "iii", "jjj",
            "kkk", "lll", "mmm", "nnn", "ooo", "ppp", "qqq", "rrr", "sss",
            "ttt", "uuu", "vvv", "www", "xxx", "yyy", "zzz");

    private static final Random random = new Random();

    public static void main(String[] args) {
        StubService stubUserService = new StubService("/users/", 8083,
                UserTestData::getRandomTwitterHandle);
        stubUserService.run();
    }

    private static String getRandomTwitterHandle() {
        int required = random.nextInt(27);
        for (String next : EXAMPLE_HANDLES) {
            if (required == 0) {
                return next;
            }
            required--;
        }
        return null;
    }
}
