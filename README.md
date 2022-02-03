# GraphQL Sample app

## 1. Setup

> ### 1.1 Schema file
> 
> - Dependencies
>   
>   - com.graphql-java.graphql-spring-boot-starter
>   
>   - com.graphql-java.graphql-java-tools
>   
>   - com.graphql-java.graphiql-spring-boot-starter (OPTIONAL for web GUI)
> 
> - Configuration
>   
>   ```yaml
>   graphql:
>      servlet:
>        corsEnabled: true
>        mapping: /graphql
>        enabled: true
>   ```
> 
> - Schema file
>   
>   ```graphql
>   schema {
>       query: Query,
>       mutation: Mutation
>   }
>   
>   type Query{
>       getAllUsers : [User]
>       getUserById(id : Int) : User
>       getCalendar: [Calendar]
>       getCalendarByUserId(userId: Int): [Calendar]
>   }
>   
>   type Mutation{
>       updateUserEmail(id:Int, val:String) : User
>       addUser(input: UserMutation) : User
>       addSession(userId: Int, input : CalendarInput) : String
>       cancelSession(userId: Int, input : CalendarInput) : String
>   }
>   
>   type User {
>       id : ID!
>       username : String,
>       email : String,
>       role : String
>   }
>   ...
>   ```
> 
> - **IMPORTANT**
>   
>   - Query and Mutation methods must be consistent with method signatures in `@Service` classes

> ### 1.2 Query resolvers