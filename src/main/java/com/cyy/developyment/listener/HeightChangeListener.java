package com.cyy.developyment.listener;

import com.cyy.developyment.entity.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class HeightChangeListener implements ChangeListener<Number> {

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        Config.changeHeight(newValue.doubleValue());
    }
}
