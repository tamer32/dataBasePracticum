package com.fmi.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.fmi.project.panel.car.CarActionButtonsPanel;
import com.fmi.project.panel.car.CarFormPanel;
import com.fmi.project.panel.car.CarsTablePanel;
import com.fmi.project.panel.country.CountryActionButtonsPanel;
import com.fmi.project.panel.country.CountryFormPanel;
import com.fmi.project.panel.country.CountryTablePanel;
import com.fmi.project.panel.owner.OwnerActionButtonsPanel;
import com.fmi.project.panel.owner.OwnerFormPanel;
import com.fmi.project.panel.owner.OwnersTablePanel;
import com.fmi.project.panel.search.SearchActionButtonsPanel;
import com.fmi.project.panel.search.SearchFormPanel;
import com.fmi.project.panel.search.SearchTablePanel;

public class MainFrame extends JFrame {
  JLabel upPanelLabel = new JLabel("Car-owner app", JLabel.CENTER);
  public static JLabel userMessageLabel = new JLabel("", JLabel.CENTER);

  public static JButton ownerBtn = new JButton("Owners");
  public static JButton homeBtn = new JButton("Home");
  public static JButton carBtn = new JButton("Cars");
  public static JButton searchViewBtn = new JButton("Search Owner");
  public static JButton countryViewBtn = new JButton("Country");
  JButton createCarBtn = new JButton("Add");

  JPanel upPanel = new JPanel();
  JPanel midPanel = new JPanel();
  JPanel downPanel = new JPanel();

  OwnerFormPanel ownerForm = new OwnerFormPanel();
  OwnersTablePanel ownersTable = new OwnersTablePanel();
  OwnerActionButtonsPanel ownersActionButton = new OwnerActionButtonsPanel();

  CarFormPanel carForm = new CarFormPanel();
  CarsTablePanel carsTable = new CarsTablePanel();
  CarActionButtonsPanel carActionButton = new CarActionButtonsPanel();

  SearchFormPanel searchForm = new SearchFormPanel();
  SearchTablePanel searchTable = new SearchTablePanel();
  SearchActionButtonsPanel searchActionButton = new SearchActionButtonsPanel();

  CountryFormPanel countryForm = new CountryFormPanel();
  CountryTablePanel countryTable = new CountryTablePanel();
  CountryActionButtonsPanel countryActionButton = new CountryActionButtonsPanel();

  JTable table = new JTable();

  public MainFrame() {
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(600, 600);
    this.setLayout(new GridLayout(3, 1));
    this.add(upPanel);
    this.add(midPanel);
    this.add(downPanel);

    // top panel
    upPanel.add(upPanelLabel);

    // mid panel
    userMessageLabel.setPreferredSize(new Dimension(550, 60));
    midPanel.add(userMessageLabel);

    searchViewBtn.addActionListener(new ViewSearchTabAction());
    ownerBtn.addActionListener(new ViewOwnersTabAction());
    homeBtn.addActionListener(new HomeTabAction());
    homeBtn.setVisible(false);
    carBtn.addActionListener(new ViewCarsTabAction());
    createCarBtn.addActionListener(new CreateCarAction());
    createCarBtn.setVisible(false);
    countryViewBtn.addActionListener(new ViewCountriesTabAction());

    midPanel.add(homeBtn);
    midPanel.add(searchViewBtn);
    midPanel.add(ownerBtn);
    midPanel.add(carBtn);
    midPanel.add(createCarBtn);
    midPanel.add(countryViewBtn);
  }

  public static void setUserMessage(String message, Color color) {
    userMessageLabel.setForeground(color);
    userMessageLabel.setText(message);
  }

  public void refreshMainPanels() {
    upPanel.revalidate();
    upPanel.repaint();
    midPanel.revalidate();
    midPanel.repaint();
    downPanel.revalidate();
    downPanel.repaint();
  }

  public class ViewOwnersTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      upPanelLabel.setText("Owner creation form");
      upPanel.add(ownerForm);
      midPanel.add(ownersActionButton);
      downPanel.add(ownersTable);
      OwnerFormPanel.prepareCountryCombo();
      changeButtonLabelVisibility();
      refreshMainPanels();
      OwnersTablePanel.fillTableWithModel();
    }

    private void changeButtonLabelVisibility() {
      searchViewBtn.setVisible(false);
      homeBtn.setVisible(true);
      ownerBtn.setVisible(false);
      carBtn.setVisible(false);
      countryViewBtn.setVisible(false);
    }
  }

  public class ViewCountriesTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      upPanelLabel.setText("Country creation form");
      upPanel.add(countryForm);
      midPanel.add(countryActionButton);
      downPanel.add(countryTable);
      CountryTablePanel.fillTableWithModel();
      changeButtonLabelVisibility();
      refreshMainPanels();
    }

    private void changeButtonLabelVisibility() {
      searchViewBtn.setVisible(false);
      homeBtn.setVisible(true);
      ownerBtn.setVisible(false);
      carBtn.setVisible(false);
      carActionButton.setVisible(false);
      countryViewBtn.setVisible(false);
    }
  }

  public class HomeTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      changeButtonLabelVisibility();
      upPanelLabel.setText("Welcome to my application");
      userMessageLabel.setText("");

      upPanel.remove(ownerForm);
      midPanel.remove(ownersActionButton);
      downPanel.remove(ownersTable);

      upPanel.remove(carForm);
      midPanel.remove(carActionButton);
      downPanel.remove(carsTable);

      upPanel.remove(searchForm);
      midPanel.remove(searchActionButton);
      downPanel.remove(searchTable);

      upPanel.remove(countryForm);
      midPanel.remove(countryActionButton);
      downPanel.remove(countryTable);

      refreshMainPanels();
    }

    private void changeButtonLabelVisibility() {
      searchViewBtn.setVisible(true);
      ownerBtn.setVisible(true);
      carBtn.setVisible(true);
      homeBtn.setVisible(false);
      createCarBtn.setVisible(false);
      carActionButton.addBtn.setVisible(false);
      countryViewBtn.setVisible(true);
    }
  }

  public class ViewCarsTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      upPanelLabel.setText("Car creation form");
      upPanel.add(carForm);
      midPanel.add(carActionButton);
      downPanel.add(carsTable);
      CarsTablePanel.fillTableWithModel();
      changeButtonLabelVisibility();
      refreshMainPanels();
    }

    private void changeButtonLabelVisibility() {
      searchViewBtn.setVisible(false);
      homeBtn.setVisible(true);
      createCarBtn.setVisible(true);
      ownerBtn.setVisible(false);
      carBtn.setVisible(false);
      carActionButton.setVisible(true);
      countryViewBtn.setVisible(false);
      carActionButton.deleteBtn.setVisible(true);
      carActionButton.updateBtn.setVisible(true);
      carActionButton.addBtn.setVisible(false);
      carActionButton.searchBtn.setVisible(true);
    }
  }

  public class CreateCarAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      if (CarFormPanel.validateForm()) {
        return;
      }
      upPanelLabel.setText("Select an owner of the car");
      upPanel.remove(carForm);
      downPanel.remove(carsTable);
      downPanel.add(ownersTable);
      changeButtonLabelVisibility();
      refreshMainPanels();
      userMessageLabel.setText("");
    }

    private void changeButtonLabelVisibility() {
      createCarBtn.setVisible(false);
      carActionButton.deleteBtn.setVisible(false);
      carActionButton.updateBtn.setVisible(false);
      carActionButton.addBtn.setVisible(true);
      carActionButton.searchBtn.setVisible(false);
    }
  }

  public class ViewSearchTabAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      upPanelLabel.setText("Search owner form");
      upPanel.add(searchForm);
      midPanel.add(searchActionButton);
      downPanel.add(searchTable);
      SearchFormPanel.prepareComboBox();
      changeButtonLabelVisibility();
      refreshMainPanels();
    }

    private void changeButtonLabelVisibility() {
      homeBtn.setVisible(true);
      searchViewBtn.setVisible(false);
      createCarBtn.setVisible(false);
      ownerBtn.setVisible(false);
      carBtn.setVisible(false);
      countryViewBtn.setVisible(false);
    }
  }
}
