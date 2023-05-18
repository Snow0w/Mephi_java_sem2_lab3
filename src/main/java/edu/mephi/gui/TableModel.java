package edu.mephi.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
  private final int colCnt = 2;
  private ArrayList<String[]> data;
  private String mode;

  public TableModel(ArrayList<String[]> data, String mode) {
    this.data = data;
    this.mode = mode;
    fireTableDataChanged();
    fireTableStructureChanged();
  }

  public TableModel() {
    data = new ArrayList<String[]>();
    mode = "Country";
  }

  public void setData(ArrayList<String[]> data, String mode) {
    this.data = data;
    this.mode = mode;
    fireTableDataChanged();
    fireTableStructureChanged();
  }

  @Override
  public int getColumnCount() {
    return colCnt;
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public Object getValueAt(int arg0, int arg1) {
    return data.get(arg0)[arg1];
  }

  @Override
  public String getColumnName(int index) {
    switch (index) {
    case 0:
      return mode;
    default:
      return "Consumtion";
    }
  }
}
