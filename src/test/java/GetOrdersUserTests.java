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

public class GetOrdersUserTests {

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
    @DisplayName("Получение заказа авторизованного пользователя")
    public void getOrderWithAuth() {
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

        ValidatableResponse getOrderResponseWithAuth = order.getOrderWithAuth(accessToken);
        getOrderResponseWithAuth.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    public void getOrderWithoutAuth() {
        orderMethods = new OrderMethods(ingredients);
        ValidatableResponse orderResponseWithoutAuth = order.createOrderWithoutAuth(orderMethods);
        orderResponseWithoutAuth.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());

        ValidatableResponse getOrderResponseWithoutAuth = order.getOrderWithoutAuth();
        getOrderResponseWithoutAuth.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}