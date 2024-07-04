package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.config.RestConfig;
import praktikum.steps.UserSteps;

import static org.hamcrest.CoreMatchers.*;

public class LoginUserTest {

    private String email;
    private String password;
    private String name;
    private final UserSteps userSteps = new UserSteps();

    private String randomEmail;
    private String randomPassword;

    @Before
    @Step("Подготовка к тестам. Описание BaseURI и создание тестовых данных.")
    public void setUp() {
        RestAssured.baseURI = RestConfig.HOST;

        email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
    }

    @Test
    @DisplayName("Логин пользователя в система с верными данными. Ожидаемый результат 200")
    public void loginUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        userSteps
                .loginUser(email, password)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
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
