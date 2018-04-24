package fi.stardex.boschdemo.ui.controller;

import fi.stardex.boschdemo.coding.Coding;
import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;
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
import java.util.LinkedHashMap;
import java.util.Map;

public class RootLayoutController {

    @FXML
    private ComboBox<Injector> comboBoxModels;

    @FXML
    private Label codetype;

    @FXML
    private Label k_coefficient;

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
    private TextField realFlowEM;

    @FXML
    private TextField realFlowLL;

    @FXML
    private TextField realFlowVL;

    @FXML
    private TextField realFlowVE;

    @FXML
    private TextField realFlowVE2;

    @FXML
    private Button calculateBtn;

    @FXML
    private TextField codeTF;

    private Map<String, Float> realFlowMap = new LinkedHashMap<>();

    {
        realFlowMap.put("EM", 0f);
        realFlowMap.put("LL", 0f);
        realFlowMap.put("VL", 0f);
        realFlowMap.put("VE", 0f);
        realFlowMap.put("VE2", 0f);
    }

    private Map<String, Float> nominalFlowMap = new LinkedHashMap<>();

    public ComboBox getComboBoxModels() {
        return comboBoxModels;
    }

    @PostConstruct
    private void init() {
        comboBoxModels.getSelectionModel().selectedItemProperty().addListener(RootLayoutController.this::changed);

        realFlowEM.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("EM", Float.parseFloat(newValue));
        });

        realFlowLL.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("LL", Float.parseFloat(newValue));
        });

        realFlowVL.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VL", Float.parseFloat(newValue));
        });

        realFlowVE.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VE", Float.parseFloat(newValue));
        });

        realFlowVE2.textProperty().addListener((observable, oldValue, newValue) -> {
            realFlowMap.put("VE2", Float.parseFloat(newValue));
        });

        calculateBtn.setOnMouseClicked(event -> {
            String code = Coding.calculate(comboBoxModels.getSelectionModel().getSelectedItem(), realFlowMap, nominalFlowMap);
            codeTF.setText(code);
        });
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
                nominalFlowMap.put("EM", injectorTest.getNominalFlow().floatValue());
                break;
            case "LL":
                setValueToLabels(nominalLL, flowRangeLL, injectorTest);
                nominalFlowMap.put("LL", injectorTest.getNominalFlow().floatValue());
                break;
            case "VL":
                setValueToLabels(nominalVL, flowRangeVL, injectorTest);
                nominalFlowMap.put("VL", injectorTest.getNominalFlow().floatValue());
                break;
            case "VE":
                setValueToLabels(nominalVE, flowRangeVE, injectorTest);
                nominalFlowMap.put("VE", injectorTest.getNominalFlow().floatValue());
                break;
            case "VE2":
                setValueToLabels(nominalVE2, flowRangeVE2, injectorTest);
                nominalFlowMap.put("VE2", injectorTest.getNominalFlow().floatValue());
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
