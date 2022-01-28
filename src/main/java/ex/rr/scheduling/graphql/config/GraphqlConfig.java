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
        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("ex.rr.scheduling")
                .withOperationsFromSingleton(mutations)
                .withOperationsFromSingleton(queries)
                .generate(); //done
        return new GraphQL.Builder(schema)
                .build();
    }

}
