package praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.config.RestConfig;
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateOrderTest {

    private String email;
    private String password;
    private String name;
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private final String ingredient1 = "61c0c5a71d1f82001bdaaa72";
    private final String ingredient2 = "61c0c5a71d1f82001bdaaa6e";
    private final String noValidIngredient = "zxc";

    @Before
    @Step("Подготовка к тестам. Описание BaseURI и создание тестовых данных.")
    public void setUpOrder() {
        RestAssured.baseURI = RestConfig.HOST;

        email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);

    }
    @Test
    @Step("Создание заказа с существующими ингредиентами под авторизованным пользователем. Ожидаемый результат 200")
    public void createOrderWithValidIngredientsAuthorizedUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        List<String> listIngredients = Arrays.asList(ingredient1, ingredient2);
        orderSteps
                .createOrder(listIngredients, token)
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @Step("Создание заказа с несуществующими ингредиентами под авторизованным пользователем. Ожидаемый результат 500")
    public void createOrderWithNoValidIngredientsAuthorizedUser_ShouldReturnCorrectResult500() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        List<String> listIngredients = Arrays.asList(ingredient1, noValidIngredient);
        orderSteps
                .createOrder(listIngredients, token)
                .statusCode(500);
    }

    @Test
    @Step("Создание заказа без ингредиентов под авторизованным пользователем. Ожидаемый результат 400")
    public void createOrderWithNoIngredientsAuthorizedUser_ShouldReturnCorrectResult400() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        List<String> listIngredients = Arrays.asList();
        orderSteps
                .createOrder(listIngredients, token)
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo( "Ingredient ids must be provided"));
    }

    @Test
    @Step("Создание заказа с существующими ингредиентами под неавторизованным пользователем. Ожидаемый результат 200")
    public void createOrderWithValidIngredientsUnauthorizedUser_ShouldReturnCorrectResult200() {

        List<String> listIngredients = Arrays.asList(ingredient1, ingredient2);
        orderSteps
                .createOrder(listIngredients, "")
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @Step("Создание заказа с несуществующими ингредиентами под неавторизованным пользователем. Ожидаемый результат 500")
    public void createOrderWithNoValidIngredientsUnauthorizedUser_ShouldReturnCorrectResult500() {
        List<String> listIngredients = Arrays.asList(ingredient1, noValidIngredient);
        orderSteps
                .createOrder(listIngredients, "")
                .statusCode(500);
    }

    @Test
    @Step("Создание заказа без ингредиентов под неавторизованным пользователем. Ожидаемый результат 400")
    public void createOrderWithNoIngredientsUnauthorizedUser_ShouldReturnCorrectResult400() {
        List<String> listIngredients = Arrays.asList();
        orderSteps
                .createOrder(listIngredients, "")
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo( "Ingredient ids must be provided"));
    }

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown(){
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        if (token != null){
            userSteps.deleteUser(token);
        }
    }
}
