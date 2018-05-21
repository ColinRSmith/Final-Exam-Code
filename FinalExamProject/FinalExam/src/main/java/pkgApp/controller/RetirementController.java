package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import pkgCore.Retirement;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;
	

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		hmTextFieldRegEx.put(txtYearsToWork, "([0-9]|[1-3][0-9]|40)");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "[0]+(\\.[0-1][0-9]?)?");
		hmTextFieldRegEx.put(txtYearsRetired, "\\d*?");
		hmTextFieldRegEx.put(txtYearsRetired, "([0-9]|1[0-9]|20)");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "[0]+(\\.[0-1][0-9]?)?");//This checks if the decimal value is between 0 and 0.19, I can't figure out how to regex it to only go between 0 and 0.10
		hmTextFieldRegEx.put(txtRequiredIncome, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtRequiredIncome, "(264[2-9]|26[5-9][0-9]|2[7-9][0-9]{2}|[3-9][0-9]{3}|10000)");
		hmTextFieldRegEx.put(txtMonthlySSI, "\\d*(\\.\\d*)?");
		hmTextFieldRegEx.put(txtMonthlySSI, "([0-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|1[0-9]{3}|2[0-5][0-9]{2}|26[0-3][0-9]|264[0-2])");
		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

		//
		// TODO: Validate Working Annual Return %, accept only numbers and decimals [done]
		// TODO: Validate Years retired, accepted only decimals [done]
		// TODO: Validate Retired Annual Return %, accept only numbers and deciamls [done]
		// TODO: Validate Required Income, accept only decimals [done]
		// TODO: Validate Monthly SSI, accept only decimals [done]
	}

	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtSaveEachMonth.clear();
		txtAnnualReturnWorking.clear();
		txtWhatYouNeedToSave.clear();
		txtYearsRetired.clear();
		txtAnnualReturnRetired.clear();
		txtRequiredIncome.clear();
		txtMonthlySSI.clear();
		txtYearsToWork.setDisable(false);

		// TODO: Clear, enable the rest of the input controls. Hint! You already have a
		// HashMap of all the input controls....!!!! [done]
	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		
		String a = txtYearsToWork.getText();
		int YearsToWork = Integer.parseInt(a);
		String b = txtAnnualReturnWorking.getText();
		Double AnnualReturnWorking = Double.parseDouble(b);
		String c = txtYearsRetired.getText();
		int YearsRetired = Integer.parseInt(c);
		String d = txtAnnualReturnRetired.getText();
		Double AnnualReturnRetired = Double.parseDouble(d);
		String e = txtRequiredIncome.getText();
		double RequiredIncome = Double.parseDouble(e);
		String f = txtMonthlySSI.getText();
		double MonthlySSI = Double.parseDouble(f);
		Retirement Calculations = new Retirement(YearsToWork, AnnualReturnWorking, YearsRetired, AnnualReturnRetired, RequiredIncome, MonthlySSI);
		String WhatYouNeedToSave = Double.toString(Calculations.TotalAmountToSave());
		txtWhatYouNeedToSave.setText(WhatYouNeedToSave);
		String SaveEachMonth = Double.toString(Calculations.MonthlySavings());
		txtSaveEachMonth.setText(SaveEachMonth);
		
		// TODO: Calculate txtWhatYouNeedToSave value...
		// TODO: Then calculate txtSaveEachMonth, using amount from txtWhatYouNeedToSave
		// as input
	}
}
