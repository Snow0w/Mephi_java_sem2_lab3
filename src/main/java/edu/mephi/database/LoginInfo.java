package edu.mephi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginInfo {
  protected final String url = "jdbc:postgresql://85.234.106.112:4242/java";
  protected String user;
  protected String pass;

  public LoginInfo(String username, String password) {
    this.user = username;
    this.pass = password;
  }

  public void checkConnection() throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, pass);
    conn.close();
  }

  public String getUrl() { return url; }

  public String getUser() { return user; }

  public void setUser(String user) { this.user = user; }

  public String getPass() { return pass; }

  public void setPass(String pass) { this.pass = pass; }

  protected LoginInfo(LoginInfo p) {}
}
