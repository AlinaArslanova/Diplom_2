import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellaburgers.User;
import stellaburgers.UserCredentials;
import stellaburgers.UserGenerator;
import stellaburgers.UserMethods;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTests {

    private User user;
    private UserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User();
        userMethods = UserGenerator.getUser();
        user.createUser(userMethods);
    }

    @After
    public void cleanUp() {
        try {
            user.deleteUser(accessToken);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginExistingUser() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = loginResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginWrongEmail() {
        UserCredentials userCredentials = new UserCredentials("asdfgh", userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWrongPassword() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), "12345");
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWrongEmailAndPassword() {
        UserCredentials userCredentials = new UserCredentials("asdfgh", "12345");
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}