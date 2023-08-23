package ru.yandex.stellaburgers;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class Order extends Rest {

    private static final String ORDER_PATH = "api/orders";

    @Step("Создание заказа авторизованного пользователя")
    public ValidatableResponse createOrderWithAuth(OrderMethods orderMethods, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(orderMethods)
                .when()
                .post(ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Создание заказа неавторизованного пользователя")
    public ValidatableResponse createOrderWithoutAuth(OrderMethods orderMethods) {
        return given()
                .spec(getBaseSpec())
                .body(orderMethods)
                .when()
                .post(ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получение заказа авторизованного пользователя")
    public ValidatableResponse getOrderWithAuth(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then()
                .log().all();
    }
}