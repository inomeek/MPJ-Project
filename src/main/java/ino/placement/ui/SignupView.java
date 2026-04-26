package ino.placement.ui;

import ino.placement.entity.Student;
import ino.placement.service.StudentService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Signup | Placement Readiness")
@Route("signup")
public class SignupView extends VerticalLayout {

    public SignupView(StudentService service) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("background", "linear-gradient(to right, #667eea, #764ba2)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("box-shadow", "0 15px 35px rgba(0,0,0,0.2)")
                .set("padding", "30px");

        H2 title = new H2("Create Account");

        TextField name = new TextField("Full Name");
        name.setPrefixComponent(VaadinIcon.USER.create());
        name.setWidthFull();

        TextField email = new TextField("Email");
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        email.setWidthFull();

        TextField password = new TextField("Password");
        password.setPrefixComponent(VaadinIcon.LOCK.create());
        password.setWidthFull();

        TextField dept = new TextField("Department");
        dept.setWidthFull();

        TextField batch = new TextField("Batch");
        batch.setWidthFull();

        Button register = new Button("Register", e -> {
            try {
                Student s = new Student();
                s.setFullName(name.getValue());
                s.setEmail(email.getValue());
                s.setPassword(password.getValue());
                s.setDepartment(dept.getValue());
                s.setBatch(batch.getValue());

                service.save(s);

                Notification.show("Registered successfully!", 3000, Notification.Position.TOP_CENTER);
            } catch (Exception ex) {
                Notification n = Notification.show("Error: " + ex.getMessage());
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.setWidthFull();

        Button login = new Button("Back to Login",
                e -> getUI().ifPresent(ui -> ui.navigate("login")));
        login.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        card.add(title, name, email, password, dept, batch, register, login);

        add(card);
    }
}