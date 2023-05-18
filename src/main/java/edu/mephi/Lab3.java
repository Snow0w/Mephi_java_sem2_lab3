package edu.mephi;

import edu.mephi.database.LoginInfo;
import edu.mephi.gui.Gui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Lab3 {
  public static void main(String[] args) throws SQLException {
    Gui gui = new Gui("App");
    gui.setVisible(true);
  }
}
