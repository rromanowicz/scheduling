# GraphQL Sample app

## 1. Setup

> ### 1. Schema file
> 
> > - Dependencies
> >   
> >   > - `com.graphql-java.graphql-spring-boot-starter`
> >   > - `com.graphql-java.graphql-java-tools`
> >   > - `com.graphql-java.graphiql-spring-boot-starter` (OPTIONAL for web GUI)
> > 
> > - Configuration
> >   
> >   ```yaml
> >   graphql:
> >         servlet:
> >        corsEnabled: true
> >        mapping: /graphql
> >        enabled: true
> >   ```
> > 
> > - Schema file 
> >   
> >   ```graphql
> >   schema {
> >     query: Query,
> >     mutation: Mutation
> >   }
> >   
> >   type Query{
> >    getAllUsers : [User]
> >    getUserById(id : Int) : User
> >    getCalendar: [Calendar]
> >    getCalendarByUserId(userId: Int): [Calendar]
> >   }
> >   
> >   type Mutation{
> >    updateUserEmail(id:Int, val:String) : User
> >    addUser(input: UserMutation) : User
> >    addSession(userId: Int, input : CalendarInput) : String
> >    cancelSession(userId: Int, input : CalendarInput) : String
> >   }
> >   type User {
> >    id : ID!
> >    username : String,
> >    email : String,
> >    role : String
> >   }
> >   ```
> > 
> > - **IMPORTANT**
> > 
> > > - Query and Mutation methods must be consistent with method signatures in `@Service` classes

> ### 2. Query resolvers
> 
> > - Dependencies
> > 
> > > - `com.graphql-java-kickstart.graphiql-spring-boot-starter`
> > > 
> > > - `io.leangen.graphql.spqr`
> > 
> > - Configuration
> > 
> > > - GraphQL `@Bean`
> > >   
> > >   ```java
> > >   @Configuration
> > >   @AllArgsConstructor
> > >       public class GraphqlConfig {
> > >   
> > >       private Queries queries;
> > >       private Mutations mutations;
> > >   
> > >       @Bean
> > >       public GraphQL configureGraphqlSchema() {
> > >           GraphQLSchema schema = new GraphQLSchemaGenerator()
> > >                   .withBasePackages("ex.rr.scheduling")
> > >                   .withOperationsFromSingleton(mutations)
> > >                   .withOperationsFromSingleton(queries)
> > >                   .generate();
> > >           return new GraphQL.Builder(schema)
> > >                   .build();
> > >       }
> > >   }
> > >   ```
> > >   
> > >   - add `.withOperationsFromSingleton()` element for each class containing GraphQL queries
> > > 
> > > - Controller
> > >   
> > >   ```java
> > >   @RestController
> > >   public class GraphqlController {
> > >   
> > >    @Autowired
> > >    private GraphQL graphQL;
> > >   
> > >    @PostMapping(value = "/graphql")
> > >    public Object execute(@RequestBody Map<String, Object> request, HttpServletRequest raw) throws GraphQLException {
> > >        ExecutionInput.Builder queryBuilder = ExecutionInput.newExecutionInput((String) request.get("query"));
> > >   
> > >        if (request.containsKey("variables") && request.get("variables") != null) {
> > >            queryBuilder = queryBuilder.variables((Map) request.get("variables"));
> > >        }
> > >   
> > >        return graphQL.execute(queryBuilder.build());
> > >    }
> > >   }
> > >   ```
> > > 
> > > - Query Resolvers
> > > 
> > > > - Annotate class with `@Component`
> > > > 
> > > > - To call a method from `.../graphql/` endpoint it must be annotated with `@GraphQLQuery` or `@GraphQLMutation`
> > > >   
> > > >   ```java
> > > >      @GraphQLQuery(name = "getUserById")
> > > >      public Optional<UserEntity> getUserById(@GraphQLArgument(name = "userId") Integer userId) {
> > > >          return userRepository.findById(userId);
> > > >      }
> > > >   ```
> > > > 
> > > > - `@GraphQLQuery` methods are used for retrieving data
> > > > 
> > > > - `@GraphQLMutation` methods are used for data manipulation
> > > > 
> > > > - Input parameters should have `@GraphQLArgument` annotation  

## 2. Usage

> **All requests to `.../graphql/` endpoint are POST requests**
> 
> ### 1. Queries
> 
> ### 2. Mutations