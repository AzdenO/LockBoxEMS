package org.vigilance.lockbox.gui;

import javafx.scene.control.Button;

public class DataButton<Object> extends Button {
    /**
     * The attached user-generated data set (ACCOUNT, BANK, CODE, FILE, IMAGE)
     */
    public Object data;

    public double[] origin = new double[2];
}
