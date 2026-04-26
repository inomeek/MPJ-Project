package ino.placement.ui;

import ino.placement.dto.ReadinessResponse;
import ino.placement.entity.Student;
import ino.placement.service.ReadinessService;
import ino.placement.util.SessionUtil;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Readiness Audit | Placement Portal")
@Route(value = "readiness", layout = MainLayout.class)
public class ReadinessView extends VerticalLayout {

    public ReadinessView(ReadinessService service) {
        // 1. Page Background & Centering
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f3f5f7");

        Student user = SessionUtil.getUser();
        if (user == null) {
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // 2. Main Container Card
        VerticalLayout card = new VerticalLayout();
        card.setMaxWidth("600px");
        card.setPadding(false); // We'll manage padding manually for the header
        card.setSpacing(false);
        card.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.LARGE,
            LumoUtility.BoxShadow.MEDIUM,
            LumoUtility.Margin.Top.XLARGE
        );

        // 3. Card Header (with subtle gradient background)
        VerticalLayout cardHeader = new VerticalLayout();
        cardHeader.setPadding(true);
        cardHeader.getStyle()
            .set("background", "linear-gradient(to right, #667eea, #764ba2)")
            .set("border-radius", "12px 12px 0 0")
            .set("color", "white");

        H2 title = new H2("Placement Readiness Audit");
        title.getStyle().set("margin", "0").set("font-size", "1.5rem");
        cardHeader.add(title, new Span("Comprehensive evaluation of your employability"));

        // 4. Content Area
        VerticalLayout content = new VerticalLayout();
        content.getStyle().set("padding", "2rem");
        content.setSpacing(true);

        try {
            ReadinessResponse res = service.evaluateStudent(user.getId());

            // Status Badge Section
            HorizontalLayout statusRow = new HorizontalLayout();
            statusRow.setWidthFull();
            statusRow.setAlignItems(Alignment.CENTER);
            statusRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

            H3 statusHeading = new H3("Evaluation Result:");
            
            Span statusBadge = new Span(res.getStatus());
            statusBadge.getElement().getThemeList().add("badge pill " + 
                (res.getStatus().equalsIgnoreCase("Ready") ? "success" : "contrast"));
            statusBadge.getStyle().set("padding", "8px 16px").set("font-size", "1rem");

            statusRow.add(statusHeading, statusBadge);

            // Message Box
            Paragraph msg = new Paragraph(res.getMessage());
            msg.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.MEDIUM);
            msg.getStyle().set("font-style", "italic").set("border-left", "4px solid #764ba2").set("padding-left", "15px");

            // Metrics Section
            VerticalLayout metrics = new VerticalLayout();
            metrics.setPadding(false);
            metrics.setSpacing(true);
            metrics.add(new Hr());

            metrics.add(
                createMetricRow("Coding Proficiency", res.getCodingScore(), "#4A90E2"),
                createMetricRow("Aptitude & Logic", res.getAptitudeScore(), "#F5A623"),
                createMetricRow("Core Technical Knowledge", res.getCoreScore(), "#764ba2")
            );

            content.add(statusRow, msg, metrics);

        } catch (Exception ex) {
            content.add(new Span("System Error: " + ex.getMessage()));
        }

        card.add(cardHeader, content);
        add(card);
    }

    private VerticalLayout createMetricRow(String title, int score, String color) {
        HorizontalLayout info = new HorizontalLayout(new Span(title), new Span(score + "%"));
        info.setWidthFull();
        info.setJustifyContentMode(JustifyContentMode.BETWEEN);
        info.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.SMALL);

        ProgressBar bar = new ProgressBar();
        bar.setValue(score / 100.0);
        bar.getStyle().set("--lumo-primary-color", color);
        
        VerticalLayout row = new VerticalLayout(info, bar);
        row.setPadding(false);
        row.setSpacing(false);
        row.addClassNames(LumoUtility.Margin.Vertical.SMALL);
        return row;
    }
}