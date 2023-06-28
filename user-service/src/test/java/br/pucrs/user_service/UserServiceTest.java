package br.pucrs.user_service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.pucrs.user_service.errors.AlreadyRegisteredException;
import br.pucrs.user_service.errors.NotFoundException;
import br.pucrs.user_service.errors.WrongCredentialsException;
import br.pucrs.user_service.models.AuthDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSave_Success() {
        User user = new User(10L, "user1", "user1@test.com", "password123");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSave_AlreadyRegistered() {
        User user = new User(10L, "user1", "user1@test.com", "password123");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        AlreadyRegisteredException thrown = assertThrows(
            AlreadyRegisteredException.class,
            () -> userService.save(user)
        );

        assertEquals("User already registered!", thrown.getMessage());
    }

    @Test
    void testSave_InvalidPassword() {
        User user1 = new User(10L, "user1", "user1@test.com", "password");
        User user2 = new User(20L, "user2", "user2@test.com", "pass28p");

        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(false);
        when(userRepository.existsByEmail(user2.getEmail())).thenReturn(false);

        IllegalArgumentException thrown1 = assertThrows(
            IllegalArgumentException.class,
            () -> userService.save(user1)
        );

        IllegalArgumentException thrown2 = assertThrows(
            IllegalArgumentException.class,
            () -> userService.save(user2)
        );
        assertEquals("Password must have at least 8 caracters and 2 numbers!", thrown1.getMessage());
        assertEquals("Password must have at least 8 caracters and 2 numbers!", thrown2.getMessage());
    }

    @Test
    public void findByEmail_UserExists_ReturnsUser() {
        User user = new User(10L, "user1", "user1@test.com", "password123");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User returnedUser = userService.findByEmail(user.getEmail());

        assertEquals(user, returnedUser);
        verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void findByEmail_UserDoesNotExists_ThrowsNotFoundException() {
        String email = "user2@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
            NotFoundException.class,
            () -> userService.findByEmail(email)
        );

        assertEquals("User email not found!", thrown.getMessage());
        verify(userRepository, Mockito.times(1)).findByEmail(email);
    }

        @Test
    public void authUser_ValidCredentials_ReturnsLoggedMessage() {
        String email = "user1@test.com";
        String password = "password123";
        User user = new User(10L, "user1", email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        AuthDTO body = new AuthDTO(email, password);

        String result = userService.authUser(body);

        assertEquals("User logged!", result);
    }

    @Test
    public void authUser_InvalidPassword_ThrowsWrongCredentialsException() {
        String email = "user1@test.com";
        String password = "password123";
        User user = new User(10L, "user1", email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        AuthDTO body = new AuthDTO(email, "wrongPassword");

        WrongCredentialsException thrown = assertThrows(
            WrongCredentialsException.class,
            () -> userService.authUser(body)
        );

        assertEquals("Wrong credentials!", thrown.getMessage());
    }
}
