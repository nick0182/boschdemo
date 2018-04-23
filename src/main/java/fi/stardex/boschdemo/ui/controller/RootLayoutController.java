package fi.stardex.boschdemo.ui.controller;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

public class RootLayoutController {

    @FXML
    public ComboBox<Injector> comboBoxModels;

    @FXML
    private Label nominalP1;

    @FXML
    private Label nominalP2;

    @FXML
    private Label nominalP3;

    @FXML
    private Label nominalP4;

    @FXML
    private Label nominalP5;

    @FXML
    private Label flowRangeP1;

    @FXML
    private Label flowRangeP2;

    @FXML
    private Label flowRangeP3;

    @FXML
    private Label flowRangeP4;

    @FXML
    private Label flowRangeP5;

    @PostConstruct
    private void init() {
        comboBoxModels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                RootLayoutController.this.changed(observable, oldValue, newValue));
    }

    public ComboBox getComboBoxModels() {
        return comboBoxModels;
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
        //codetype.setText(injector.getCodetype());
        //checsumm_m.setText(String.valueOf(injector.getChecksumM()));
    }

    private void setNominalFlowAndFlowRangeForTest(InjectorTest injectorTest) {
        switch (injectorTest.toString()) {
            case "EM":
                setValueToLabels(nominalP1, flowRangeP1, injectorTest);
                break;
            case "LL":
                setValueToLabels(nominalP2, flowRangeP2, injectorTest);
                break;
            case "VL":
                setValueToLabels(nominalP3, flowRangeP3, injectorTest);
                break;
            case "VE":
                setValueToLabels(nominalP4, flowRangeP4, injectorTest);
                break;
            case "VE2":
                setValueToLabels(nominalP5, flowRangeP5, injectorTest);
                break;
        }
    }

    private void setValueToLabels(Label nominalLabel, Label flowRangeLabel, InjectorTest injectorTest) {

    }
}
