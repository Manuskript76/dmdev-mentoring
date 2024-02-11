package com.vmdev.eshop.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceIT {

    private static final UserService userService = new UserService();

    @Test
    void getTest() {
        userService.setTest2("test");
        assertEquals("test", userService.getTest2());
    }

    @Test
    void setTest() {
        userService.setTest2("test");
        assertEquals("test", userService.getTest2());
    }
}