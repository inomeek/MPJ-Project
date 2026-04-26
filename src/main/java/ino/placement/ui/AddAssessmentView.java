package ino.placement.ui;

import ino.placement.entity.AssessmentResult;
import ino.placement.entity.Student;
import ino.placement.service.AssessmentResultService;
import ino.placement.util.SessionUtil;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Add Assessment | Placement Portal")
@Route(value = "add-marks", layout = MainLayout.class)
public class AddAssessmentView extends VerticalLayout {

    public AddAssessmentView(AssessmentResultService service) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f3f5f7");

        Student user = SessionUtil.getUser();
        if (user == null) {
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        VerticalLayout card = new VerticalLayout();
        card.setMaxWidth("450px");
        card.setPadding(false);
        card.setSpacing(false);
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM
        );

        VerticalLayout cardHeader = new VerticalLayout();
        cardHeader.setPadding(true);
        cardHeader.getStyle()
                .set("background", "linear-gradient(to right, #667eea, #764ba2)")
                .set("border-radius", "12px 12px 0 0")
                .set("color", "white");

        H3 title = new H3("Add New Assessment");
        title.getStyle().set("margin", "0");

        cardHeader.add(title, new Span("Record your latest performance scores"));
        cardHeader.setAlignItems(Alignment.CENTER);

        VerticalLayout formBody = new VerticalLayout();
        formBody.getStyle().set("padding", "2rem");
        formBody.setSpacing(true);

        ComboBox<String> type = new ComboBox<>("Assessment Type");
        type.setItems("Coding", "Aptitude", "Interview");
        type.setPrefixComponent(VaadinIcon.ACADEMY_CAP.create());
        type.setWidthFull();

        NumberField score = new NumberField("Score Obtained (0-100)");
        score.setPrefixComponent(VaadinIcon.CHART_LINE.create());
        score.setMin(0);
        score.setMax(100);
        score.setStepButtonsVisible(true);
        score.setWidthFull();

        Button submit = new Button("Save Assessment", VaadinIcon.CHECK.create());
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submit.setWidthFull();
        submit.getStyle().set("margin-top", "1rem").set("cursor", "pointer");

        submit.addClickListener(e -> {

            // ✅ Field validation
            if (type.isEmpty() || score.isEmpty()) {
                showError("Please fill all fields");
                return;
            }

            double val = score.getValue();

            // ✅ Strict validation (extra safety beyond UI limits)
            if (val < 0 || val > 100) {
                showError("Score must be between 0 and 100");
                return;
            }

            try {
                AssessmentResult a = new AssessmentResult();
                a.setAssessmentType(type.getValue().trim());
                a.setScoreObtained(val);

                service.saveOrUpdate(user.getId(), a);

                Notification n = Notification.show("Assessment saved successfully!");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.setPosition(Notification.Position.TOP_CENTER);

                score.clear();
                type.clear();

            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        });

        formBody.add(type, score, submit);
        card.add(cardHeader, formBody);
        add(card);
    }

    private void showError(String msg) {
        Notification n = Notification.show(msg);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        n.setPosition(Notification.Position.TOP_CENTER);
    }
}