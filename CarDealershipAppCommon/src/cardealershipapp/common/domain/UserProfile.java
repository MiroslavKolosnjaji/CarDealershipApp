package cardealershipapp.common.domain;

import java.io.Serializable;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class UserProfile implements Serializable  {
    
    private String email;
    private String password;
    private User user;

    public UserProfile() {
    }

    public UserProfile(String email, String password, User user) {
        this.email = email;
        this.password = password;
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(", user: ").append(user);
        sb.append("email: ").append(email);
        return sb.toString();
    }

   

}
