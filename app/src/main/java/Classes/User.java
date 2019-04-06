package Classes;

public class User {
    private String username;
    private String email;
    private String password;


    public static User CreateUser(String username, String email, String password)
    {
        if(!username.isEmpty()){
            return new User(username, email, password);
        }else{
            return new User(email, password);
        }

    }

    private void SendToDB(){
        String hashedPass = BCrypt.hashpw(password,BCrypt.gensalt(11));
    }

    private void checkDB(){

    }

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    private User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
