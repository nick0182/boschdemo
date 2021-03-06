package fi.stardex.boschdemo.ui.controller;

import fi.stardex.boschdemo.coding.CodeTypeCoding;
import fi.stardex.boschdemo.coding.CodeTypeCodingFactory;
import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

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

    private static final String PATTERN = "[0-9]*\\.?[0-9]*";

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
            if (!newValue.equals(""))
                realFlowMap.put("EM", Float.parseFloat(newValue));
        });

        realFlowLL.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(""))
                realFlowMap.put("LL", Float.parseFloat(newValue));
        });

        realFlowVL.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(""))
                realFlowMap.put("VL", Float.parseFloat(newValue));
        });

        realFlowVE.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(""))
                realFlowMap.put("VE", Float.parseFloat(newValue));
        });

        realFlowVE2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")&&!newValue.equals("0"))
                realFlowMap.put("VE2", Float.parseFloat(newValue));
        });

        realFlowEM.setTextFormatter(new TextFormatter<String>(filter));
        realFlowLL.setTextFormatter(new TextFormatter<String>(filter));
        realFlowVL.setTextFormatter(new TextFormatter<String>(filter));
        realFlowVE.setTextFormatter(new TextFormatter<String>(filter));
        realFlowVE2.setTextFormatter(new TextFormatter<String>(filter));

        calculateBtn.setOnMouseClicked(event -> {
            try {
                CodeTypeCoding type = CodeTypeCodingFactory.getCodTypeCoding(comboBoxModels.getSelectionModel().getSelectedItem(), realFlowMap, nominalFlowMap);
                codeTF.setText(type.calculate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void changed(ObservableValue<? extends Injector> observable, Injector oldValue, Injector newValue) {
        if (newValue == null)
            return;
        if (newValue.getCodetype() == 3) {
            nominalVE2.setText("");
            flowRangeVE2.setText("");
            realFlowVE2.setText("0");
            realFlowVE2.setVisible(false);
        }
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
                if (comboBoxModels.getSelectionModel().getSelectedItem().getCodetype() != 3) {
                    setValueToLabels(nominalVE2, flowRangeVE2, injectorTest);
                    nominalFlowMap.put("VE2", injectorTest.getNominalFlow().floatValue());
                    realFlowVE2.setVisible(true);
                } else {
                    realFlowMap.remove("VE2");
                }
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

    private UnaryOperator<TextFormatter.Change> filter = change -> {
        String input = change.getText();
        if (input.matches(PATTERN)) {
            return change;
        }
        return null;
    };
}
