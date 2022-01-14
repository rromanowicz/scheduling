package ex.rr.scheduling.model.graphql;

import graphql.schema.GraphQLInputType;
import lombok.Data;

@Data
public class UserMutation implements GraphQLInputType {

    @Override
    public String getName() {
        return "userMutation";
    }

    private String username;

    private String password;

    private String email;
}
