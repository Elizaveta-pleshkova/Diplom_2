package praktikum.model;

import lombok.*;
@Data
public class EditUserRequest {
    private String email;
    private String password;
    private String name;
}
