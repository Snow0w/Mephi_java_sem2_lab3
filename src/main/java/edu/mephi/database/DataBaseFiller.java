package edu.mephi.database;

import edu.mephi.exceptions.WrongExcelFileException;
import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.reactor.ReactorStorage;
import edu.mephi.reactor.ReactorStorageMaker;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataBaseFiller {
  private Connection con;
  private SqlStrings sql;
  private XSSFWorkbook wb;
  private FileInputStream fis;
  private String reactorFilename;
  private PreparedStatement statement;

  public DataBaseFiller(Connection con, String reactor, String excel)
      throws WrongExcelFileException {
    this.con = con;
    this.reactorFilename = reactor;
    this.sql = new SqlStrings();
    initWorkbook(excel);
    try {
      fis.close();
    } catch (IOException e) {
      throw new WrongExcelFileException(e.getMessage());
    }
  }

  private void initWorkbook(String filename) throws WrongExcelFileException {
    try {
      fis = new FileInputStream(filename);
      this.wb = new XSSFWorkbook(fis);
    } catch (NullPointerException e) {
      throw new WrongExcelFileException("Excel file is not choosen");
    } catch (Exception e) {
      throw new WrongExcelFileException(e.getMessage());
    }
  }

  public void basicFill() throws SQLException, WrongExcelFileException {
    String[] table_names =
        new String[] {"regions", "countries", "companies", "sites"};
    int cnt = 0;
    for (String name : table_names) {
      try {
        XSSFSheet sheet = wb.getSheet(name);
        statement = con.prepareStatement(sql.insertArray[cnt]);
        basicFillFromExcel(sheet);
        statement.executeBatch();
        statement.close();
        cnt++;
      } catch (NullPointerException e) {
        throw new WrongExcelFileException(e.getMessage());
      } catch (IllegalStateException e) {
        throw new WrongExcelFileException(e.getMessage());
      }
    }
  }

  private void basicFillFromExcel(XSSFSheet sheet) throws SQLException {
    int num_rows = sheet.getPhysicalNumberOfRows();
    int num_cells = sheet.getRow(0).getPhysicalNumberOfCells();
    int cnt = 0;
    XSSFCell cell;
    for (int i = 1; i < num_rows; i++) {
      for (int j = 0; j < num_cells; j++) {
        cell = sheet.getRow(i).getCell(j);
        cnt = j + 1;
        if (j == 0) {
          statement.setInt(cnt, (int)cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING &&
                   cell.getCellType() != CellType.BLANK) {
          if (cell.getStringCellValue().trim().equals("")) {
            statement.setNull(cnt, java.sql.Types.NULL);
          } else
            statement.setString(cnt, cell.getStringCellValue().trim());
        } else if (cell.getCellType() == CellType.NUMERIC) {
          statement.setInt(cnt, (int)cell.getNumericCellValue());
        } else {
          statement.setNull(cnt, java.sql.Types.NULL);
        }
      }
      statement.addBatch();
    }
  }

  public void unitsFill() throws WrongFileFormatException, SQLException {
    ReactorStorageMaker maker = new ReactorStorageMaker();
    ReactorStorage storage;
    try {
      storage = maker.makeReactorStorage(reactorFilename);
      XSSFSheet sheet = wb.getSheet("units");
      statement = con.prepareStatement(sql.insertUnits);
      unitsFillFromExcel(sheet, storage);
      statement.executeBatch();
      statement.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new WrongFileFormatException(e.getMessage());
    } catch (NullPointerException e) {
      throw new WrongFileFormatException(e.getMessage());
    } catch (IllegalStateException e) {
      throw new WrongFileFormatException(e.getMessage());
    }
  }

  private void unitsFillFromExcel(XSSFSheet sheet, ReactorStorage storage)
      throws SQLException {
    int num_rows = sheet.getPhysicalNumberOfRows();
    int num_cells = sheet.getRow(0).getPhysicalNumberOfCells();
    int cnt = 0;
    XSSFCell cell;
    for (int i = 1; i < num_rows; i++) {
      for (int j = 0; j < num_cells; j++) {
        cell = sheet.getRow(i).getCell(j);
        cnt = j + 1;
        if (j == 0) {
          // setIntCellValue(cell, cnt);
          statement.setInt(cnt, (int)cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING &&
                   cell.getCellType() != CellType.BLANK) {
          setStringCellValue(cell, cnt);
        } else if (cell.getCellType() == CellType.NUMERIC) {
          statement.setInt(cnt, (int)cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
          statement.setBoolean(cnt, cell.getBooleanCellValue());
        } else {
          statement.setNull(cnt, java.sql.Types.NULL);
        }
      }
      updateViaStorage(sheet.getRow(i), storage);
      statement.addBatch();
    }
  }

  private void updateViaStorage(XSSFRow row, ReactorStorage storage)
      throws SQLException {
    statement.setDouble(18, row.getCell(17).getNumericCellValue());
    XSSFCell load_factor = row.getCell(18);
    if (load_factor.getCellType() == CellType.BLANK ||
        load_factor.getCellType() == CellType._NONE) {
      statement.setInt(19, 90);
    }
    if (row.getCell(14).getDateCellValue() != null)
      statement.setDate(15,
                        new Date(row.getCell(14).getDateCellValue().getTime()));
    if (row.getCell(15).getDateCellValue() != null)
      statement.setDate(16,
                        new Date(row.getCell(15).getDateCellValue().getTime()));
    if (row.getCell(16).getDateCellValue() != null)
      statement.setDate(17,
                        new Date(row.getCell(16).getDateCellValue().getTime()));
    if (!row.getCell(4).getStringCellValue().trim().equals("in operation")) {
      statement.setNull(20, java.sql.Types.NULL);
      statement.setNull(21, java.sql.Types.NULL);
      return;
    }
    String class_name = row.getCell(7).getStringCellValue().trim();
    class_name = fixClassName(class_name);
    statement.setDouble(
        20, Double.valueOf(storage.getStorage().get(class_name).getBurnup()));
    statement.setDouble(
        21,
        Double.valueOf(storage.getStorage().get(class_name).getFirstLoad()));
  }

  private String fixClassName(String class_name) {
    if (class_name.indexOf("VVER") != -1) {
      return new String("VVER_1000");
    }
    if (class_name.indexOf("PWR") != -1) {
      return new String("PWR");
    }
    if (class_name.indexOf("CPR") != -1 || class_name.indexOf("CNP") != -1) {
      return new String("CPR_1000");
    }
    if (class_name.indexOf("AGR") != -1) {
      return new String("MAGNOX");
    }
    return class_name;
  }

  private void setStringCellValue(XSSFCell cell, int cnt) throws SQLException {
    if (cell.getStringCellValue().trim().equals("")) {
      statement.setNull(cnt, java.sql.Types.NULL);
      return;
    }
    statement.setString(cnt, cell.getStringCellValue().trim());
  }
}
