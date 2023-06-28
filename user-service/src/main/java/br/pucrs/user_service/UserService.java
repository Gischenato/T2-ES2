package br.pucrs.user_service;

import org.springframework.stereotype.Service;

import br.pucrs.user_service.errors.AlreadyRegisteredException;
import br.pucrs.user_service.errors.NotFoundException;
import br.pucrs.user_service.errors.WrongCredentialsException;
import br.pucrs.user_service.models.AuthDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repository;

    public void save(User user) {
        if (repository.existsByEmail(user.email)) throw new AlreadyRegisteredException("User already registered!");
        String password = user.getPassword();
        if (!(password.matches(".*\\d.*") && password.length() >= 8))
            throw new IllegalArgumentException("Password must have at least 8 caracters and 2 numbers!");
        
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User email not found!"));
    }

    public String authUser(AuthDTO body) {
        User userFound = findByEmail(body.getEmail());
        
        if (!userFound.getPassword().equals(body.getPassword())) {
            throw new WrongCredentialsException("Wrong credentials!");
        }

        return "User logged!";
    }

}
