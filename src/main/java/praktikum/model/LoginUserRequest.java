package praktikum.model;

import lombok.*;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
