package edu.mephi.gui;

import edu.mephi.database.DataBase;
import edu.mephi.database.LoginInfo;
import edu.mephi.exceptions.WrongExcelFileException;
import edu.mephi.exceptions.WrongFileFormatException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Gui extends JFrame implements ActionListener {
  private static final int HEIGHT = 600;
  private static final int WIDTH = 800;

  private LoginInfo loginData;
  private String reactorFile;
  private String excelFile;
  private TableModel tableModel;

  private JPanel buttonPane;
  private JButton loginButton;
  private JButton createButton;
  private JButton dropButton;
  private JButton chooseExcelButton;
  private JButton chooseFileButton;
  private JComboBox<String> agregBox;
  private JButton renderButton;
  private JTable table;
  private JScrollPane scroll;

  public Gui(String name) {
    super(name);
    this.setSize(WIDTH, HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(new BorderLayout());
    this.buttonPane = new JPanel(new GridLayout(0, 4));
    this.loginButton = new JButton("Login to database");
    loginButton.addActionListener(this);
    createButton = new JButton("Fill database");
    createButton.addActionListener(this);
    dropButton = new JButton("Clear database");
    dropButton.addActionListener(this);
    chooseExcelButton = new JButton("Choose excel file");
    chooseExcelButton.addActionListener(this);
    chooseFileButton = new JButton("Choose reactors file");
    chooseFileButton.addActionListener(this);
    agregBox = new JComboBox<>(new String[] {"Counrty", "Company", "Region"});
    renderButton = new JButton("Render table");
    renderButton.addActionListener(this);

    buttonPane.add(loginButton);
    buttonPane.add(createButton);
    buttonPane.add(dropButton);
    buttonPane.add(chooseFileButton);
    buttonPane.add(chooseExcelButton);
    buttonPane.add(agregBox);
    buttonPane.add(renderButton);
    tableModel = new TableModel();
    table = new JTable(tableModel);
    // tablePane.add(table);
    // this.add(this.tablePane, BorderLayout.CENTER);
    scroll = new JScrollPane(table);

    this.add(this.scroll, BorderLayout.CENTER);
    this.add(this.buttonPane, BorderLayout.SOUTH);
    loginData = new LoginInfo(null, null);
    reactorFile = null;
    excelFile = null;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == loginButton) {
      this.loginAction();
    }
    if (event.getSource() == chooseFileButton) {
      this.chooseFileAction();
    }
    if (event.getSource() == chooseExcelButton) {
      this.chooseExcelAction();
    }
    if (event.getSource() == createButton) {
      this.createDatabase();
    }
    if (event.getSource() == dropButton) {
      this.dropAction();
    }
    if (event.getSource() == renderButton) {
      this.renderTable();
    }
  }

  private void renderTable() {
    DataBase db = new DataBase(loginData);
    try {
      tableModel = db.getTableModel(agregBox.getSelectedIndex());
      table = new JTable(tableModel);
      this.remove(scroll);
      this.revalidate();
      scroll = new JScrollPane(table);
      this.add(this.scroll, BorderLayout.CENTER);
      this.revalidate();
      repaint();
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Can't create",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void dropAction() {
    DataBase db = new DataBase(loginData);
    try {
      db.drop();
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Can't create",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void createDatabase() {
    DataBase db = new DataBase(loginData);
    try {
      db.create();
      db.fill(reactorFile, excelFile);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      JOptionPane.showMessageDialog(this, e.getMessage(), "Can't create",
                                    JOptionPane.ERROR_MESSAGE);
    } catch (WrongExcelFileException e) {
      JOptionPane.showMessageDialog(this, "Excel file error: " + e.getMessage(),
                                    "Can't create", JOptionPane.ERROR_MESSAGE);
    } catch (WrongFileFormatException e) {
      System.out.println(e.getMessage());
      JOptionPane.showMessageDialog(this,
                                    "Reactor file error: " + e.getMessage(),
                                    "Can't create", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void chooseExcelAction() {
    JFileChooser fileopen = new JFileChooser();
    // JFileChooser fileopen =
    //     new JFileChooser("/home/snow0w/repos/JAVA/lab3/files/");
    int ret = fileopen.showDialog(null, "Choose file");
    if (ret != JFileChooser.APPROVE_OPTION) {
      return;
    }
    excelFile = fileopen.getSelectedFile().getAbsolutePath();
  }

  private void chooseFileAction() {
    JFileChooser fileopen = new JFileChooser();
    // JFileChooser fileopen =
    //     new JFileChooser("/home/snow0w/repos/JAVA/lab3/files/");
    int ret = fileopen.showDialog(null, "Choose file");
    if (ret != JFileChooser.APPROVE_OPTION) {
      return;
    }
    reactorFile = fileopen.getSelectedFile().getAbsolutePath();
  }

  private void loginAction() {
    JPanel panel = new JPanel(new BorderLayout(5, 5));

    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
    label.add(new JLabel("User", SwingConstants.RIGHT));
    label.add(new JLabel("Password", SwingConstants.RIGHT));
    panel.add(label, BorderLayout.WEST);

    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
    JTextField username = new JTextField();
    controls.add(username);
    JPasswordField password = new JPasswordField();
    controls.add(password);
    panel.add(controls, BorderLayout.CENTER);

    JOptionPane.showMessageDialog(this, panel, "login",
                                  JOptionPane.QUESTION_MESSAGE);
    try {
      loginData.setUser(username.getText());
      loginData.setPass(new String(password.getPassword()));
      loginData.checkConnection();
      JOptionPane.showMessageDialog(this, "Success", "ok",
                                    JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Wrong login data",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
}
