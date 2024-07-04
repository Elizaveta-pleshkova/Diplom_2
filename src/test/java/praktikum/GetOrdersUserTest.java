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


import static org.hamcrest.CoreMatchers.*;

public class GetOrdersUserTest {
    private String email;
    private String password;
    private String name;
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    @Before
    @Step("Подготовка к тестам. Описание BaseURI и создание тестовых данных.")
    public void setUpOrder() {
        RestAssured.baseURI = RestConfig.HOST;

        email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);

    }

    @Test
    @Step("Получение заказов авторизованного пользователя. Ожидаемый результат 200")
    public void getOrderAuthorizedUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        orderSteps
                .getOrder(token)
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @Step("Получение заказов неавторизованного пользователя. Ожидаемый результат 401")
    public void getOrderUnauthorizedUser_ShouldReturnCorrectResult401() {

        orderSteps
                .getOrder("")
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo( "You should be authorised"));
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
