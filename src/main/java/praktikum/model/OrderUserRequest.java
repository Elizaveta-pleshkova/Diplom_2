package praktikum.model;

import lombok.*;

import java.util.List;

@Data
public class OrderUserRequest {
    private List<String> ingredients;
}
