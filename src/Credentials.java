public class Credentials {
    public String email;
    public String password;

    public Credentials(String mail, String pass) {
        this.email = mail;
        this.password = pass;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String pass) {
        this.password = pass;
    }

    public String toString() {
        return "Email: " + this.email + "; Password: " + this.password + ";";
    }
}
