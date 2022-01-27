package ex.rr.scheduling.graphql.config;

import ex.rr.scheduling.graphql.resolver.Mutations;
import ex.rr.scheduling.graphql.resolver.Queries;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GraphqlConfig {

    private Queries queries;
    private Mutations mutations;

    @Bean
    public GraphQL configureGraphqlSchema() {
//    UserService userService = new UserService(); //instantiate the service (or inject by Spring or another framework)
        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("ex.rr.scheduling") //not mandatory but strongly recommended to set your "root" packages
                .withOperationsFromSingleton(mutations) //register the service
                .withOperationsFromSingleton(queries)
                .generate(); //done
        return new GraphQL.Builder(schema)
                .build();
    }

}
