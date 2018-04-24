package fi.stardex.boschdemo.ui.controller;

import fi.stardex.boschdemo.coding.Coding;
import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RootLayoutController {

    @FXML
    public ComboBox<Injector> comboBoxModels;

    @FXML
    public Label codetype;

    @FXML
    public Label k_coefficient;

    @FXML
    private Label nominalEM;

    @FXML
    private Label nominalLL;

    @FXML
    private Label nominalVL;

    @FXML
    private Label nominalVE;

    @FXML
    private Label nominalVE2;

    @FXML
    private Label flowRangeEM;

    @FXML
    private Label flowRangeLL;

    @FXML
    private Label flowRangeVL;

    @FXML
    private Label flowRangeVE;

    @FXML
    private Label flowRangeVE2;

    @FXML
    public TextField realFlowEM;

    @FXML
    public TextField realFlowLL;

    @FXML
    public TextField realFlowVL;

    @FXML
    public TextField realFlowVE;

    @FXML
    public TextField realFlowVE2;

    @FXML
    public Button calculateBtn;

    private Map<String, Double> realFlowMap = new HashMap<>();

    {
        realFlowMap.put("EM", 0d);
        realFlowMap.put("LL", 0d);
        realFlowMap.put("VL", 0d);
        realFlowMap.put("VE", 0d);
        realFlowMap.put("VE2", 0d);
    }

    private Map<String, Double> nominalFlowMap = new HashMap<>();

    public ComboBox getComboBoxModels() {
        return comboBoxModels;
    }

    @PostConstruct
    private void init() {
        comboBoxModels.getSelectionModel().selectedItemProperty().addListener(RootLayoutController.this::changed);

        realFlowEM.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("EM", Double.parseDouble(newValue));
        });
        
        realFlowLL.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("LL", Double.parseDouble(newValue));
        });

        realFlowVL.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VL", Double.parseDouble(newValue));
        });

        realFlowVE.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VE", Double.parseDouble(newValue));
        });

        realFlowVE2.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VE2", Double.parseDouble(newValue));
        });

        calculateBtn.setOnMouseClicked(event ->
                Coding.calculate(comboBoxModels.getSelectionModel().getSelectedItem(), realFlowMap, nominalFlowMap));
    }


    private void changed(ObservableValue<? extends Injector> observable, Injector oldValue, Injector newValue) {
        if (newValue == null)
            return;

        changeInjector(newValue);
    }

    private void changeInjector(Injector injector) {
        for (InjectorTest injectorTest : injector.getInjectorTests()) {
            setNominalFlowAndFlowRangeForTest(injectorTest);
        }
        codetype.setText(injector.getCodetype().toString());
        k_coefficient.setText(injector.getK_coefficient().toString());
    }

    private void setNominalFlowAndFlowRangeForTest(InjectorTest injectorTest) {
        switch (injectorTest.toString()) {
            case "EM":
                setValueToLabels(nominalEM, flowRangeEM, injectorTest);
                nominalFlowMap.put("EM", injectorTest.getNominalFlow().doubleValue());
                break;
            case "LL":
                setValueToLabels(nominalLL, flowRangeLL, injectorTest);
                nominalFlowMap.put("LL", injectorTest.getNominalFlow().doubleValue());
                break;
            case "VL":
                setValueToLabels(nominalVL, flowRangeVL, injectorTest);
                nominalFlowMap.put("VL", injectorTest.getNominalFlow().doubleValue());
                break;
            case "VE":
                setValueToLabels(nominalVE, flowRangeVE, injectorTest);
                nominalFlowMap.put("VE", injectorTest.getNominalFlow().doubleValue());
                break;
            case "VE2":
                setValueToLabels(nominalVE2, flowRangeVE2, injectorTest);
                nominalFlowMap.put("VE2", injectorTest.getNominalFlow().doubleValue());
                break;
            default:
        }
    }

    private void setValueToLabels(Label nominalLabel, Label flowRangeLabel, InjectorTest injectorTest) {
        double nominalFlow = injectorTest.getNominalFlow().doubleValue();
        double flowRangePercents = injectorTest.getFlowRange().doubleValue();

        double flowRange = new BigDecimal(nominalFlow * (flowRangePercents / 100)).setScale(2, BigDecimal.ROUND_UP).doubleValue();

        nominalLabel.setText(String.valueOf(nominalFlow));
        flowRangeLabel.setText(String.valueOf(flowRange));
    }
}
