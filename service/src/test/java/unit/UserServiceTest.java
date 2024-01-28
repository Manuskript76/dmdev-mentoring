package unit;

import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final UserService userService = new UserService();

    @Test
    void getTest() {
        userService.setTest("test");
        assertEquals("test", userService.getTest());
    }

    @Test
    void setTest() {
        userService.setTest("test");
        assertEquals("test", userService.getTest());
    }
}