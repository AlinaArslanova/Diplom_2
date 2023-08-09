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

public class ChangeDataUserTests {

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
    @DisplayName("Изменение данных с авторизацией")
    public void changeDataUserWithAuth() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse updateResponse = user.updateUserWithAuth(UserGenerator.getUser(), accessToken);
        updateResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных без авторизации")
    public void changeDataUserWithoutAuth() {
        ValidatableResponse updateResponse = user.updateUserWithoutAuth(UserGenerator.getUser());
        updateResponse.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}