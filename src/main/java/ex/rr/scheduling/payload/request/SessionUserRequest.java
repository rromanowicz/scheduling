package ex.rr.scheduling.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SessionUserRequest {

    @NotNull
    private Integer sessionId;

    @NotNull
    private String username;

}
