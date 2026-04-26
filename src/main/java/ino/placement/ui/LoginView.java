package ino.placement.ui;

import ino.placement.entity.Student;
import ino.placement.service.AuthService;
import ino.placement.util.SessionUtil;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login | Placement Readiness")
@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService authService) {

        // Full screen layout
        setSizeFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Background gradient (modern look)
        getStyle().set("background", "linear-gradient(to right, #667eea, #764ba2)");

        // Login Card
        VerticalLayout loginCard = new VerticalLayout();
        loginCard.setWidth("400px");
        loginCard.setPadding(true);
        loginCard.setSpacing(true);
        loginCard.setAlignItems(Alignment.CENTER);

        loginCard.getStyle()
                .set("background-color", "white")
                .set("box-shadow", "0 15px 35px rgba(0, 0, 0, 0.2)")
                .set("border-radius", "16px")
                .set("padding", "30px");

        // Title
        H2 title = new H2("Placement Portal");
        title.getStyle().set("margin", "0");

        Paragraph subtitle = new Paragraph("Login to continue");
        subtitle.getStyle().set("color", "gray").set("margin-top", "0");

        // Fields
        TextField email = new TextField("Email");
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        email.setWidthFull();

        PasswordField password = new PasswordField("Password");
        password.setPrefixComponent(VaadinIcon.LOCK.create());
        password.setWidthFull();

        // Login Button
        Button loginBtn = new Button("Login");
        loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginBtn.setWidthFull();
        loginBtn.getStyle()
                .set("margin-top", "15px")
                .set("cursor", "pointer");

        loginBtn.addClickShortcut(Key.ENTER);

        loginBtn.addClickListener(e -> {
            try {
                Student s = authService.login(email.getValue(), password.getValue());

                SessionUtil.setUser(s);

                getUI().ifPresent(ui -> ui.navigate("home"));

            } catch (Exception ex) {
                showError("Invalid email or password");
            }
        });

        // Divider
        Hr divider = new Hr();

        // Signup section
        HorizontalLayout signupLayout = new HorizontalLayout();
        signupLayout.setAlignItems(Alignment.CENTER);

        Span text = new Span("New user?");
        Button signupBtn = new Button("Create Account",
                e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        signupBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        signupLayout.add(text, signupBtn);

        // Assemble
        loginCard.add(title, subtitle, email, password, loginBtn, divider, signupLayout);

        add(loginCard);
    }

    private void showError(String message) {
        Notification n = Notification.show(message);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        n.setPosition(Notification.Position.TOP_CENTER);
        n.setDuration(3000);
    }
}