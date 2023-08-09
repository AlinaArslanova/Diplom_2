import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellaburgers.User;
import stellaburgers.UserGenerator;
import stellaburgers.UserMethods;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTests {

    private User user;
    private UserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User();
        userMethods = UserGenerator.getUser();
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
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser() {
        ValidatableResponse createResponse = user.createUser(userMethods);
        createResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createExistingUser() {
        ValidatableResponse createFirstResponse = user.createUser(userMethods);
        ValidatableResponse createSecondResponse = user.createUser(userMethods);
        createSecondResponse.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
        accessToken = createFirstResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным полем name")
    public void createUserWithoutName() {
        userMethods.setName(null);
        ValidatableResponse createResponse = user.createUser(userMethods);
        createResponse.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с не заполненным полем email")
    public void createUserWithoutEmail() {
        userMethods.setEmail(null);
        ValidatableResponse createResponse = user.createUser(userMethods);
        createResponse.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с не заполненным полем password")
    public void createUserWithoutPassword() {
        userMethods.setPassword(null);
        ValidatableResponse createResponse = user.createUser(userMethods);
        createResponse.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}