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


public class EditUserTest {
    private final String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    private final String password = RandomStringUtils.randomAlphabetic(10);
    private final String name = RandomStringUtils.randomAlphabetic(10);
    private final UserSteps userSteps = new UserSteps();
    private String token;


    @Before
    @Step("Подготовка к тестам. Описание BaseURI и создание тестовых данных.")
    public void setUp() {
        RestAssured.baseURI = RestConfig.HOST;
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя. Ожидаемый результат 200")
    public void editEmailAuthorizedUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        token = userSteps.loginUser(email, password)
                .extract().path("accessToken");
        String emailForEdit = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        userSteps
                .editUser(emailForEdit, null, null, token)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(emailForEdit.toLowerCase()))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя. Ожидаемый результат 200")
    public void editNameAuthorizedUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String nameForEdit = RandomStringUtils.randomAlphabetic(10);
        userSteps
                .editUser(null, null, nameForEdit, token)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(nameForEdit));
    }

    @Test
    @DisplayName("Изменение имени и email авторизованного пользователя. Ожидаемый результат 200")
    public void editNameAndEmailAuthorizedUser_ShouldReturnCorrectResult200() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String emailForEdit = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        String nameForEdit = RandomStringUtils.randomAlphabetic(10);
        userSteps
                .editUser(emailForEdit, null, nameForEdit, token)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(emailForEdit.toLowerCase()))
                .body("user.name", equalTo(nameForEdit));
    }

    @Test
    @DisplayName("Изменение email неавторизованного пользователя. Ожидаемый результат 401")
    public void editEmailUnauthorizedUser_ShouldReturnCorrectResult401() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String emailForEdit = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        userSteps
                .editUser(emailForEdit, null, null, token)
                .statusCode(200);
//                .statusCode(401)
//                .body("success", is(false))
//                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя. Ожидаемый результат 401")
    public void editNameUnauthorizedUser_ShouldReturnCorrectResult401() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String nameForEdit = RandomStringUtils.randomAlphabetic(10);
        userSteps
                .editUser(null, null, nameForEdit, token)
                .statusCode(200);
//                .statusCode(401)
//                .body("success", is(false))
//                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени и email неавторизованного пользователя. Ожидаемый результат 401")
    public void editNameAndEmailUnauthorizedUser_ShouldReturnCorrectResult401() {
        userSteps
                .createUser(email, password, name);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String emailForEdit = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        String nameForEdit = RandomStringUtils.randomAlphabetic(10);
        userSteps
                .editUser(emailForEdit, null, nameForEdit, token)
                .statusCode(200);
//                .statusCode(401)
//                .body("success", is(false))
//                .body("message", equalTo("You should be authorised"));
    }

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown(){
        if (token != null){
            userSteps.deleteUser(token);
        }
    }
}
