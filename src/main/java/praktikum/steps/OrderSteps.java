package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.OrderUserRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static praktikum.config.EndPoint.ORDER_URL;

public class OrderSteps {

    @Step("Отправка запроса на создание заказа")
    public ValidatableResponse createOrder(List<String> ingredients, String token){
        OrderUserRequest orderUserRequest = new OrderUserRequest();
        orderUserRequest.setIngredients(ingredients);
        return given()
                .header("Content-type", "application/json")
                .headers("Authorization", token)
                .and()
                .body(orderUserRequest)
                .when()
                .post(ORDER_URL)
                .then();
    }

    @Step("Отправка запроса на получение заказов")
    public ValidatableResponse getOrder(String token){
        return given()
                .header("Content-type", "application/json")
                .headers("Authorization", token)
                .when()
                .get(ORDER_URL)
                .then();
    }

}
