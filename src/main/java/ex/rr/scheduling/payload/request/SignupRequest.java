package ex.rr.scheduling.payload.request;

import ex.rr.scheduling.model.enums.RoleEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<RoleEnum> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
