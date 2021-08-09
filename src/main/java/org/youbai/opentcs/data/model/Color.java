package org.youbai.opentcs.data.model;

import java.io.Serializable;

public class Color implements Serializable {

    private String Color;

    public static final Color white = new Color("saaa");

    /**
     * The color white.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color WHITE = white;

    /**
     * The color light gray.  In the default sRGB space.
     */
    public static final Color lightGray = new Color("saaa");

    /**
     * The color light gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color LIGHT_GRAY = lightGray;

    /**
     * The color gray.  In the default sRGB space.
     */
    public static final Color gray      = new Color("saaa");

    /**
     * The color gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public static final Color darkGray  = new Color("saaa");

    /**
     * The color dark gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     */
    public static final Color black     = new Color("saaa");

    /**
     * The color black.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color BLACK = black;

    /**
     * The color red.  In the default sRGB space.
     */
    public static final Color red       = new Color("saaa");

    /**
     * The color red.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color RED = red;

    /**
     * The color pink.  In the default sRGB space.
     */
    public static final Color pink      = new Color("saaa");

    /**
     * The color pink.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     */
    public static final Color orange    = new Color("saaa");

    /**
     * The color orange.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     */
    public static final Color yellow    = new Color("saaa");

    /**
     * The color yellow.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     */
    public static final Color green     = new Color("saaa");

    /**
     * The color green.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     */
    public static final Color magenta   = new Color("saaa");

    /**
     * The color magenta.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     */
    public static final Color cyan      = new Color("saaa");

    /**
     * The color cyan.  In the default sRGB space.
     * @since 1.4
     */
    public static final Color CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     */
    public static final Color blue      = new Color("saaa");

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
