package ex.rr.scheduling.model.graphql;

import lombok.Data;

@Data
public class UserMutation {

    private String username;

    private String password;

    private String email;
}
