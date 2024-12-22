package sg.edu.nus.iss.readingcompanion.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Made User class implement UserDetails to get Spring Security to map @AuthenticationPrincipal
// to  thsi class. I've also made the CustomUserDetailsService class return model.User instead of 
// Spring Security's own User class. 
public class User implements UserDetails{
    // TODO: Validation
    private String username;
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
        System.out.println("User toString() used.");
        return "%s,%s".formatted(username, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }
    
}
