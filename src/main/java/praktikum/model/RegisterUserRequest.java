package praktikum.model;

import lombok.*;

@Data
public class RegisterUserRequest {
    private String email;
    private String password;
    private String name;

}
