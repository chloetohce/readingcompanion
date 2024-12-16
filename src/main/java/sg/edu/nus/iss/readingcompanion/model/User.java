package sg.edu.nus.iss.readingcompanion.model;

public class User {
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
        return "%s,%s".formatted(username, password);
    }
    
}
