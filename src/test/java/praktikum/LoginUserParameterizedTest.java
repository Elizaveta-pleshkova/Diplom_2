package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.config.RestConfig;
import praktikum.steps.UserSteps;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class LoginUserParameterizedTest {


    private String emailForLogin;
    private String passwordForLogin;
    private final static String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    private final static String password = RandomStringUtils.randomAlphabetic(10);
    private final static String name = RandomStringUtils.randomAlphabetic(10);
    private final UserSteps userSteps = new UserSteps();

    private final static String randomEmail = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    private final static String randomPassword = RandomStringUtils.randomAlphabetic(10);


    @Parameterized.Parameters()
    public static Object[][] data(){
        return new Object[][]{
                {email, null},
                {null, password},
                {null, null},
                {email, randomPassword},
                {randomEmail, password},
                {randomEmail, randomPassword}
        };
    }

    public LoginUserParameterizedTest(String email, String password) {
        this.emailForLogin = email;
        this.passwordForLogin = password;
    }

    @Before
    @Step("Подготовка к тестам. Описание BaseURI")
    public void setUp() {
        RestAssured.baseURI = RestConfig.HOST;
    }

    @Test
    @DisplayName("Логин пользователя в системе с отсутствующими или неверными данным. Ожидаемый результат 401")
    public void loginUser_ShouldReturnIncorrectResult401() {
        userSteps
                .createUser(email, password, name);
        userSteps
                .loginUser(emailForLogin, passwordForLogin)
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo( "email or password are incorrect"));
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
