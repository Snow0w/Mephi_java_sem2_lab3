package edu.mephi.database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseQuery {
  private Connection con;
  private SqlStrings sqlStrings;

  public DataBaseQuery() { sqlStrings = new SqlStrings(); }

  public ArrayList<String[]> dataForCountry(Connection con)
      throws SQLException {
    this.con = con;
    return logic(sqlStrings.sumCountry);
  }

  public ArrayList<String[]> dataForRegion(Connection con) throws SQLException {
    this.con = con;
    return logic(sqlStrings.sumRegion);
  }
  public ArrayList<String[]> dataForCompany(Connection con)
      throws SQLException {
    this.con = con;
    return logic(sqlStrings.sumCompany);
  }

  private ArrayList<String[]> logic(String sql) throws SQLException {
    ArrayList<String[]> out = new ArrayList<String[]>();
    String[] row;
    Statement st = con.createStatement();
    ResultSet set = st.executeQuery(sql);
    while (set.next()) {
      row = new String[2];
      row[0] = set.getString(1);
      row[1] = String.valueOf(set.getDouble(2));
      out.add(row);
    }
    set.close();
    st.close();
    return out;
  }
}
