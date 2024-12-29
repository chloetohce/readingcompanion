package sg.edu.nus.iss.readingcompanion.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.repository.UserRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) throws Exception {
        try {
            loadUserByUsername(user.getUsername());
            throw new Exception("Username already taken.");
        } catch (UsernameNotFoundException e) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.add("users", user.getUsername(), user.toString());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userRaw = userRepository.get(RedisUtil.KEY_USERS, username);
        if (userRaw == null) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        return User.deserialize(userRepository.get("users", username));
    }
}
