package org.youbai.opentcs.data.model;

public class Color {

    private String Color;

    public final static Color RED = new Color("#FFB6C1");


    public Color(String color) {
        Color = color;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
