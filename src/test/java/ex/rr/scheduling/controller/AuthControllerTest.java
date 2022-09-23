package ex.rr.scheduling.controller;

import static ex.rr.scheduling.model.enums.RoleEnum.ROLE_ADMIN;
import static ex.rr.scheduling.model.enums.RoleEnum.ROLE_MODERATOR;
import static ex.rr.scheduling.model.enums.RoleEnum.ROLE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ex.rr.scheduling.model.Role;
import ex.rr.scheduling.repository.RoleRepository;
import ex.rr.scheduling.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeAll
    public void setup() {
        roleRepository.saveAll(List.of(
                new Role(ROLE_ADMIN),
                new Role(ROLE_MODERATOR),
                new Role(ROLE_USER)));
    }

    @Test
    @Order(1)
    public void shouldSignUp() throws Exception {
        this.mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"username\": \"test1\",\n"
                                + "\t\"password\": \"password1\",\n"
                                + "\t\"email\": \"test@test.com\",\n"
                                + "\t\"role\": [\"ROLE_ADMIN\", \"ROLE_USER\"]\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(2)
    public void shouldSignIn() throws Exception {
        this.mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"username\": \"test1\",\n"
                                + "\t\"password\": \"password1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
