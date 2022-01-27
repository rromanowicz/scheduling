package ex.rr.scheduling.graphql.controller;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.GraphQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GraphqlController {

    @Autowired
    private GraphQL graphQL;

    @PostMapping(value = "/graphql")
    public Object execute(@RequestBody Map<String, Object> request, HttpServletRequest raw) throws GraphQLException {
        ExecutionInput.Builder queryBuilder = ExecutionInput.newExecutionInput((String) request.get("query"));

        if (request.containsKey("variables") && request.get("variables") != null) {
            queryBuilder = queryBuilder.variables((Map) request.get("variables"));
        }

        return graphQL.execute(queryBuilder.build());
    }
}