package com.qualco.nationsapp.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * Database object for application users.
 *
 * @author jason
 * @see UserDto
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(
        name =
                "`USER`") // Need backticks because "USER" is a reserved table in H2 and tests are
                          // affected.
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email_address", unique = true)
    @NonNull
    @Email
    @NotBlank
    private String email;

    @Column(name = "password")
    @JsonIgnore
    @ToString.Exclude
    @NonNull
    @NotBlank
    private String password;

    public UserEntity(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy
                        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy
                        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                        : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity userEntity = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), userEntity.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
