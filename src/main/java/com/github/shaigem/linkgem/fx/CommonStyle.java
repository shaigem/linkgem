package com.github.shaigem.linkgem.fx;

/**
 * Created on 2017-01-01.
 */
public enum CommonStyle {

    BUTTON("linkgem-button"),
    TOOLBAR_BUTTON("linkgem-toolbar-button"),
    ICON_MENU_BUTTON("linkgem-button", "linkgem-icon-menu-button"),
    TOGGLE_BUTTON("linkgem-toggle-button");

    private String[] classes;

    private CommonStyle(String... classes) {
        this.classes = classes;
    }

    /**
     * @return an array of css classes
     */
    public String[] getStyleClasses() {
        return classes;
    }


}
