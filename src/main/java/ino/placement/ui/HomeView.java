package ino.placement.ui;

import ino.placement.entity.Student;
import ino.placement.entity.AssessmentResult;
import ino.placement.service.AnalyticsService;
import ino.placement.util.ReadinessUtil;
import ino.placement.util.SessionUtil;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.Map;

@PageTitle("Dashboard | Placement Portal")
@Route(value = "home", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView(AnalyticsService analyticsService) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f3f5f7");

        Student user = SessionUtil.getUser();
        if (user == null) {
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);
        headerLayout.setMaxWidth("1100px");
        headerLayout.addClassNames(
                LumoUtility.Margin.Top.LARGE,
                LumoUtility.Margin.Bottom.MEDIUM
        );

        H2 title = new H2("Welcome back, " + user.getFullName());
        title.addClassNames(LumoUtility.Margin.NONE);

        Span subtitle = new Span("Here is a summary of your placement readiness.");
        subtitle.addClassNames(
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.FontSize.SMALL
        );

        headerLayout.add(title, subtitle);

        // ✅ Fetch BOTH timeline + latest
        List<AssessmentResult> timeline = analyticsService.getTimeline(user.getId());
        Map<String, Double> scores = analyticsService.getLatestScores(user.getId());

        double coding = scores.getOrDefault("Coding", 0.0);
        double aptitude = scores.getOrDefault("Aptitude", 0.0);
        double core = scores.getOrDefault("Interview", 0.0);

        // ✅ CORRECT overall calculation (uses cleaned logic)
        double overall = ReadinessUtil.calculateOverall(timeline);
        String status = ReadinessUtil.classify(overall);

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setMaxWidth("1100px");
        cards.setSpacing(true);

        // 👤 Profile Card
        VerticalLayout profileCard = createDashboardCard("Profile", VaadinIcon.USER, "#667eea");
        profileCard.add(
                createDataLine("Department", safe(user.getDepartment())),
                createDataLine("Batch", safe(user.getBatch())),
                createDataLine("CGPA", String.valueOf(user.getCgpa()))
        );

        // 📊 Scores Card
        VerticalLayout analyticsCard = createDashboardCard("Latest Scores", VaadinIcon.CHART, "#764ba2");
        analyticsCard.add(
                createScoreBar("Coding", coding),
                createScoreBar("Aptitude", aptitude),
                createScoreBar("Core", core)
        );

        // 🎯 Status Card
        VerticalLayout readinessCard = createDashboardCard("Overall Status", VaadinIcon.FLAG, "#27ae60");

        H1 bigScore = new H1(String.format("%.1f%%", overall));
        bigScore.getStyle().set("margin", "10px 0").set("color", "#27ae60");

        Span statusLabel = new Span(status);

        String theme;
        if (status.equalsIgnoreCase("READY")) theme = "success";
        else if (status.equalsIgnoreCase("PARTIALLY READY")) theme = "contrast";
        else theme = "error";

        statusLabel.getElement().getThemeList().add("badge pill " + theme);

        readinessCard.add(new Span("Current Readiness:"), bigScore, statusLabel);
        readinessCard.setAlignItems(Alignment.CENTER);

        cards.add(profileCard, analyticsCard, readinessCard);
        cards.setFlexGrow(1, profileCard, analyticsCard, readinessCard);

        add(headerLayout, cards);
    }

    private VerticalLayout createDashboardCard(String title, VaadinIcon icon, String accentColor) {

        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE
        );

        card.getStyle().set("border-top", "4px solid " + accentColor);

        HorizontalLayout cardHeader = new HorizontalLayout(icon.create(), new H3(title));
        cardHeader.setAlignItems(Alignment.CENTER);
        cardHeader.getStyle().set("color", accentColor);
        cardHeader.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        card.add(cardHeader);
        return card;
    }

    private HorizontalLayout createDataLine(String label, String value) {

        Span l = new Span(label + ": ");
        l.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.SMALL);

        Span v = new Span(value);
        v.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        HorizontalLayout row = new HorizontalLayout(l, v);
        row.setSpacing(false);
        return row;
    }

    private VerticalLayout createScoreBar(String label, double score) {

        // ✅ Clamp just in case
        score = Math.max(0, Math.min(100, score));

        Span l = new Span(label + ": " + String.format("%.0f%%", score));
        l.addClassNames(LumoUtility.FontSize.XSMALL, LumoUtility.FontWeight.BOLD);

        Div barContainer = new Div();
        barContainer.setWidthFull();
        barContainer.getStyle()
                .set("background-color", "#eee")
                .set("border-radius", "4px")
                .set("height", "8px");

        Div fill = new Div();
        fill.setWidth(score + "%");
        fill.getStyle()
                .set("background-color", "#764ba2")
                .set("border-radius", "4px")
                .set("height", "100%");

        barContainer.add(fill);

        VerticalLayout layout = new VerticalLayout(l, barContainer);
        layout.setPadding(false);
        layout.setSpacing(false);

        return layout;
    }

    private String safe(String val) {
        return val == null ? "N/A" : val;
    }
}