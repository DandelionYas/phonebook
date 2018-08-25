package phonebook.da;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementFactory {
    private String url;
    private String username;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Statement get() {
        try {
            return DriverManager.getConnection(url, username, password).createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can not connect to server.");
            return null;
        }
    }
}
