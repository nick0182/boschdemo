package fi.stardex.boschdemo.ui;

import javafx.scene.Parent;

public class ViewHolder {
    private Parent view;
    private Object controller;

    public Parent getView() {
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
