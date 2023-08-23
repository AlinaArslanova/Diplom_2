package ru.yandex.stellaburgers;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static UserMethods getUser() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        return new UserMethods(name, email, password);
    }
}
