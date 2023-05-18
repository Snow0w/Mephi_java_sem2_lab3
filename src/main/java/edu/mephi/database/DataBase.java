package edu.mephi.database;
import edu.mephi.exceptions.WrongExcelFileException;
import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.gui.TableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase extends LoginInfo {

  public DataBase(LoginInfo parent) {
    super(parent);
    this.setUser(parent.getUser());
    this.setPass(parent.getPass());
  }
  public DataBase(String username, String password) {
    super(username, password);
  }

  public void create() throws SQLException {
    SqlStrings sql = new SqlStrings();
    Connection con = DriverManager.getConnection(url, user, pass);
    Statement st = con.createStatement();
    st.execute(sql.createDataBase);
    st.close();
    con.close();
  }

  public void fill(String reactor, String excel)
      throws SQLException, WrongExcelFileException, WrongFileFormatException {
    try (Connection con = DriverManager.getConnection(url, user, pass)) {
      DataBaseFiller filler = new DataBaseFiller(con, reactor, excel);
      filler.basicFill();
      filler.unitsFill();
    } catch (SQLException e) {
      failedDrop();
      throw new SQLException(e.getMessage());
    } catch (WrongExcelFileException e) {
      failedDrop();
      throw new WrongExcelFileException(e.getMessage());
    } catch (WrongFileFormatException e) {
      failedDrop();
      throw new WrongFileFormatException(e.getMessage());
    }
  }

  public void drop() throws SQLException {
    SqlStrings sql = new SqlStrings();
    Connection con = DriverManager.getConnection(url, user, pass);
    Statement st = con.createStatement();
    st.execute(sql.dropDataBase);
    st.close();
    con.close();
  }

  private void failedDrop() {
    try {
      SqlStrings sql = new SqlStrings();
      Connection con = DriverManager.getConnection(url, user, pass);
      Statement st = con.createStatement();
      st.execute(sql.dropDataBase);
      st.close();
      con.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public TableModel getTableModel(int selectedIndex) throws SQLException {
    DataBaseQuery query = new DataBaseQuery();
    ArrayList<String[]> newData;
    String mode;
    Connection con = DriverManager.getConnection(url, user, pass);
    switch (selectedIndex) {
    case 0:
      newData = query.dataForCountry(con);
      mode = "Country";
      break;
    case 1:
      newData = query.dataForCompany(con);
      mode = "Company";
      break;
    default:
      newData = query.dataForRegion(con);
      mode = "Region";
    }
    con.close();
    return new TableModel(newData, mode);
  }
}
