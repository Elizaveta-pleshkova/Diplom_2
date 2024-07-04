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

public class AddUserTest {
    private String email;
    private String password;
    private String name;
    private final UserSteps userSteps = new UserSteps();

    @Before
    @Step("Подготовка к тестам. Описание BaseURI и создание тестовых данных.")
    public void setUp() {
        RestAssured.baseURI = RestConfig.HOST;

        email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
    }

    @Test
    @DisplayName("Создание нового пользователя. Ожидаемый результат 200")
    public void createNewUser_ShouldReturnCorrectResult200(){
        userSteps
                .createUser(email, password, name)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание нового пользователя, с данными ранее созданного пользователя. Ожидаемый результат 403")
    public void createTwoNewUser_ShouldReturnIncorrectResult403(){
        userSteps
                .createUser(email, password, name);
        userSteps
                .createUser(email, password, name)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание нового пользователя без логина. Ожидаемый результат 403")
    public void createNewUserNotLogin_ShouldReturnIncorrectResult403(){
        userSteps
                .createUser(null, password, name)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание нового пользователя без пароля. Ожидаемый результат 403")
    public void createNewUserNotPassword_ShouldReturnIncorrectResult400(){

        userSteps
                .createUser(email, null, name)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание нового пользователя без имени. Ожидаемый результат 403")
    public void createNewUserNotName_ShouldReturnIncorrectResult403(){

        userSteps
                .createUser(email, password, null)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
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
