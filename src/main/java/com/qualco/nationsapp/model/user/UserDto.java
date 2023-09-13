package com.qualco.nationsapp.model.user;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

/**
 * DTO class for Users.
 *
 * @see UserEntity
 * @author jason
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    @Schema(example = "jason.filippou@gmail.com")
    @NonNull
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @Schema(example = "jasonfilippoupass")
    @JsonProperty(access = WRITE_ONLY)
    @NonNull
    @ToString.Exclude
    @Size(min = 8, max = 30)
    private String password;
}
