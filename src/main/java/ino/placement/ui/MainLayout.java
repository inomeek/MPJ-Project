package ino.placement.ui;

import ino.placement.util.SessionUtil;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SessionUtil.getUser() == null) {
            event.forwardTo("login");
        }
    }

    private void createHeader() {
        H3 logo = new H3("Placement System");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {

        // ✅ SIMPLE RouterLinks (no generics issues)
        RouterLink home = new RouterLink("Home", HomeView.class);
        RouterLink profile = new RouterLink("My Profile", StudentView.class);
        RouterLink addMarks = new RouterLink("Add Marks", AddAssessmentView.class);
        RouterLink readiness = new RouterLink("Readiness", ReadinessView.class);
        RouterLink analytics = new RouterLink("Analytics", AnalyticsView.class);

        VerticalLayout menu = new VerticalLayout(
                home,
                profile,
                addMarks,
                readiness,
                analytics
        );

        Button logout = new Button("Logout", e -> {
            SessionUtil.logout();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        logout.addThemeVariants(ButtonVariant.LUMO_ERROR);

        VerticalLayout drawer = new VerticalLayout(menu, logout);
        drawer.setSizeFull();
        drawer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        addToDrawer(drawer);
    }
}