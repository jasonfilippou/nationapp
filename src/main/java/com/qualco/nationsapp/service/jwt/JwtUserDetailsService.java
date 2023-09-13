package com.qualco.nationsapp.service.jwt;


import com.qualco.nationsapp.controller.JwtAuthenticationController;
import com.qualco.nationsapp.model.user.UserDto;
import com.qualco.nationsapp.model.user.UserEntity;
import com.qualco.nationsapp.persistence.UserRepository;
import com.qualco.nationsapp.util.exception.EmailAlreadyInDatabaseException;
import com.qualco.nationsapp.util.jwt.JwtRequestFilter;
import com.qualco.nationsapp.util.logger.Logged;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service class that talks to the database to retrieve and store user information.
 *
 * @author jason
 * @see JwtRequestFilter
 * @see JwtAuthenticationService
 * @see JwtAuthenticationController
 */
@Service
@RequiredArgsConstructor
@Logged
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    /**
     * Load a user from the database given their email. The name of the method is confusing because
     * that's how {@link UserDetailsService} names it; don't shoot the messenger here. For our
     * application, the username is the e-mail.
     *
     * @param email the user's e-mail.
     * @return An instance of {@link org.springframework.security.core.userdetails.User} that
     *     contains the user's username, password and authorities.
     * @throws UsernameNotFoundException if no user with username {@literal username} exists in the
     *     database.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            // Return an appropriate instance of org.springframework.security.core.userdetails.User
            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(), user.get().getPassword(), Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("User with email: " + email + " not found.");
        }
    }

    /**
     * Save a new user in the database.
     *
     * @param newUser A {@link UserDto} with the information of the new user to store in the
     *     database.
     * @return A {@link UserDto} corresponding to the just persisted user.
     * @throws EmailAlreadyInDatabaseException If the username provided already exists in the
     *     database.
     */
    public UserDto save(UserDto newUser) throws EmailAlreadyInDatabaseException {
        try {
            UserEntity savedUser =
                    userRepository.save(
                            new UserEntity(
                                    newUser.getEmail().trim(),
                                    encoder.encode(newUser.getPassword())));
            return new UserDto(savedUser.getEmail(), savedUser.getPassword());
        } catch (DataIntegrityViolationException integrityViolationException) {
            throw new EmailAlreadyInDatabaseException(newUser.getEmail().trim());
        }
    }
}
