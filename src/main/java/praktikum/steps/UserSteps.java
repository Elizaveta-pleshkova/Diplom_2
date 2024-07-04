package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.model.EditUserRequest;
import praktikum.model.RegisterUserRequest;
import praktikum.model.LoginUserRequest;

import static io.restassured.RestAssured.given;
import static praktikum.config.EndPoint.*;

public class UserSteps {

    @Step("Отправка запроса на создание пользователя")
    public ValidatableResponse createUser(String email, String password, String name){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(password);
        registerUserRequest.setName(name);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(registerUserRequest)
                .when()
                .post(CREATE_USER_URL)
                .then();
    }

    @Step("Отправка запроса на логин пользователя")
    public ValidatableResponse loginUser(String email, String password){
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(email);
        loginUserRequest.setPassword(password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUserRequest)
                .when()
                .post(LOGIN_USER_URL)
                .then();
    }

    @Step("Отправка запроса на изменение данных пользователя")
    public ValidatableResponse editUser(String email, String password, String name, String authorization){
        EditUserRequest editUserRequest = new EditUserRequest();
        editUserRequest.setEmail(email);
        editUserRequest.setPassword(password);
        editUserRequest.setName(name);
        return given()
                .header("Content-type", "application/json")
                .headers("Authorization", authorization)
                .and()
                .body(editUserRequest)
                .when()
                .patch(EDIT_USER_URL)
                .then();
    }

    @Step("Отправка запроса на удаление пользователя")
    public ValidatableResponse deleteUser(String authorization){
        return given()
                .header("Content-type", "application/json")
                .headers("Authorization", authorization)
                .when()
                .delete(EDIT_USER_URL)
                .then();
    }

}
