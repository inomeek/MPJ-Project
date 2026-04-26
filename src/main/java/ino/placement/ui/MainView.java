package ino.placement.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import ino.placement.util.SessionUtil;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {

        // 🔐 If already logged in → go to home
        if (SessionUtil.getUser() != null) {
            UI.getCurrent().navigate("home");
        } 
        // ❌ Otherwise → go to login
        else {
            UI.getCurrent().navigate("login");
        }
    }
}