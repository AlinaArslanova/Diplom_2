import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellaburgers.*;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTests {

    private User user;
    private Order order;
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    List<String> ingredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa7a",
            "61c0c5a71d1f82001bdaaa77",
            "61c0c5a71d1f82001bdaaa6c",
            "61c0c5a71d1f82001bdaaa74");
    private String accessToken;


    @Before
    public void setUp() {
        user = new User();
        userMethods = UserGenerator.getUser();
        user.createUser(userMethods);
        order = new Order();
    }

    @After
    public void tearDown() {
        try {
            user.deleteUser(accessToken);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuth() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = loginResponse.extract().path("accessToken");
        orderMethods = new OrderMethods(ingredients);
        ValidatableResponse orderResponse = order.createOrderWithAuth(orderMethods, accessToken);
        orderResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации и ингредиентов")
    public void createOrderWithoutAuth() {
        orderMethods = new OrderMethods(ingredients);
        ValidatableResponse orderResponse = order.createOrderWithoutAuth(orderMethods);
        orderResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithoutIngredients() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = loginResponse.extract().path("accessToken");
        orderMethods = new OrderMethods(null);
        ValidatableResponse orderResponse = order.createOrderWithAuth(orderMethods, accessToken);
        orderResponse.assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хешем ингредиентов")
    public void createOrderWithWrongHashIngredient() {
        UserCredentials userCredentials = new UserCredentials(userMethods.getEmail(), userMethods.getPassword());
        ValidatableResponse loginResponse = user.loginUser(userCredentials);
        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = loginResponse.extract().path("accessToken");
        ingredients.set(0, "000000");
        orderMethods = new OrderMethods(ingredients);
        ValidatableResponse orderResponse = order.createOrderWithAuth(orderMethods, accessToken);
        orderResponse.assertThat().statusCode(500);
    }
}