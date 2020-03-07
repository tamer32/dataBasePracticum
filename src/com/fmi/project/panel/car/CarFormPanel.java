package com.fmi.project.panel.car;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.h2.util.StringUtils;

import com.fmi.project.MainFrame;

@SuppressWarnings("serial")
public class CarFormPanel extends JPanel {

  JLabel brandLabel = new JLabel("Brand:");
  JLabel modelLabel = new JLabel("Model:");
  JLabel creationYearLabel = new JLabel("Creation year:");

  static JTextField brandTField = new JTextField();
  static JTextField modelTField = new JTextField();
  static JTextField creationYearTField = new JTextField();

  public CarFormPanel() {
    this.setPreferredSize(new Dimension(550, 100));
    this.setLayout(new GridLayout(3, 2));
    this.add(brandLabel);
    this.add(brandTField);
    this.add(modelLabel);
    this.add(modelTField);
    this.add(creationYearLabel);
    this.add(creationYearTField);
  }

  static void clearForm() {
    brandTField.setText("");
    modelTField.setText("");
    creationYearTField.setText("");
  }

  public static boolean validateForm() {
    String brand = brandTField.getText();
    String model = modelTField.getText();
    String creationYear = creationYearTField.getText();

    String errorMsg = validateFields(brand, model, creationYear);
    if (!StringUtils.isNullOrEmpty(errorMsg)) {
      MainFrame.setUserMessage(errorMsg, Color.RED);
      return true;
    }

    return false;
  }

  private static String validateFields(String brand, String model, String creationYear) {
    StringBuilder errorMsg = new StringBuilder();

    if (StringUtils.isNullOrEmpty(brand)) {
      errorMsg.append("Brand should not be empty<br/>");
    } else if (brand.length() > 10) {
      errorMsg.append("Brand should not be more than 10 characters<br/>");
    }

    if (StringUtils.isNullOrEmpty(model)) {
      errorMsg.append("Model should not be empty<br/>");
    } else if (model.length() > 20) {
      errorMsg.append("Model should not be more than 20 characters<br/>");
    }

    if (StringUtils.isNullOrEmpty(creationYear)) {
      errorMsg.append("Creation year should not be empty");
    } else {
      int yearOfCreation = Integer.valueOf(creationYear);

      if (!creationYear.matches("[0-9]+")) {
        errorMsg.append("Creation year should be only numbers<br/>");
      } else if (!(yearOfCreation > 1950
          && yearOfCreation < Calendar.getInstance().get(Calendar.YEAR))) {
        errorMsg.append(
            "Creation year must be between 1950 and " + Calendar.getInstance().get(Calendar.YEAR));
      }
    }

    if (!StringUtils.isNullOrEmpty(errorMsg.toString())) {
      errorMsg.insert(0, "<html>");
      errorMsg.insert(errorMsg.length(), "</html>");
    }

    return errorMsg.toString();
  }
}
