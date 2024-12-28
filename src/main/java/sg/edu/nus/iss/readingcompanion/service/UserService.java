package sg.edu.nus.iss.readingcompanion.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.repository.UserRepository;

@Service
public class UserService implements UserDetailsS {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        // Check if the username already exists
        if (userRepository.get("users", user.getUsername()) != null) {
            throw new RuntimeException("Username is already taken");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password: " + user.getPassword());

        // Save user to Redis
        userRepository.add("users", user.getUsername(), user.toString());
    }

    public User findByUsername(String username) {
        return User.deserialize(userRepository.get("users", username));
    }
}
