package sg.edu.nus.iss.readingcompanion.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User implements UserDetails{
    @NotEmpty(message = "Please provide a username.")
    @Size(min = 3, max = 48, message = "Username must be within 3 - 48 characters.")
    private String username;

    @NotEmpty(message = "Please provide a password.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User deserialize(String data) {
        String[] arr = data.split(",");
        return new User(arr[0], arr[1]);
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "%s,%s".formatted(username, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }
    
}
