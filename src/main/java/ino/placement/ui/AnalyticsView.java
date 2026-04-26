package ino.placement.ui;

import ino.placement.entity.AssessmentResult;
import ino.placement.entity.Student;
import ino.placement.service.AnalyticsService;
import ino.placement.util.ReadinessUtil;
import ino.placement.util.SessionUtil;
import ino.placement.util.SuggestionUtil;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.*;

@PageTitle("Career Analytics | Placement Portal")
@Route(value = "analytics", layout = MainLayout.class)
public class AnalyticsView extends VerticalLayout {

    public AnalyticsView(AnalyticsService service) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f3f5f7");

        Student user = SessionUtil.getUser();

        if (user == null) {
            UI.getCurrent().navigate("login");
            return;
        }

        VerticalLayout card = new VerticalLayout();
        card.setMaxWidth("750px");
        card.setSpacing(true);
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Margin.Top.XLARGE
        );
        card.getStyle().set("padding", "2rem");

        H2 title = new H2("Career Performance Analytics");
        Paragraph desc = new Paragraph("Your performance insights and growth.");
        desc.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        card.add(title, desc, new Hr());

        try {
            List<AssessmentResult> list = service.getTimeline(user.getId());

            if (list == null || list.isEmpty()) {
                card.add(new Paragraph("No assessment data available. Add marks to see analytics."));
                add(card);
                return;
            }

            // =========================
            // 🎯 OVERALL SCORE
            // =========================
            double score = ReadinessUtil.calculateOverall(list);
            String status = ReadinessUtil.classify(score);

            HorizontalLayout scoreHero = new HorizontalLayout();
            scoreHero.setWidthFull();
            scoreHero.setJustifyContentMode(JustifyContentMode.BETWEEN);
            scoreHero.setAlignItems(Alignment.CENTER);

            H1 scoreValue = new H1(String.format("%.1f%%", score));
            scoreValue.getStyle().set("margin", "0").set("color", "#764ba2");

            Span statusBadge = new Span(status);

            String theme;
            if (status.equalsIgnoreCase("READY")) theme = "success";
            else if (status.equalsIgnoreCase("PARTIALLY READY")) theme = "contrast";
            else theme = "error";

            statusBadge.getElement().getThemeList().add("badge pill " + theme);

            scoreHero.add(scoreValue, statusBadge);

            card.add(scoreHero, new Hr());

            // =========================
            // 📊 SKILL BREAKDOWN
            // =========================
            card.add(new H4("Skill Breakdown"), createCategoryBreakdown(list), new Hr());

            // =========================
            // 📈 PROGRESS TIMELINE (IMPROVED)
            // =========================
            list.sort((a, b) -> b.getAssessmentDate().compareTo(a.getAssessmentDate())); // latest first

            VerticalLayout timeline = new VerticalLayout();
            timeline.setSpacing(false);

            Double prev = null;

            for (AssessmentResult a : list) {

                double percent = (a.getScoreObtained() * 100.0) / a.getMaxScore();
                percent = Math.max(0, Math.min(100, percent));

                HorizontalLayout row = new HorizontalLayout();
                row.setWidthFull();
                row.setAlignItems(Alignment.CENTER);
                row.setJustifyContentMode(JustifyContentMode.BETWEEN);

                Span left = new Span(a.getAssessmentDate().toString());
                left.addClassNames(LumoUtility.FontSize.SMALL);

                Span value = new Span(String.format("%.0f%%", percent));
                value.addClassNames(LumoUtility.FontWeight.BOLD);

                Span trend = new Span();

                if (prev != null) {
                    if (percent > prev) {
                        trend.setText(" ↑");
                        trend.getStyle().set("color", "green");
                    } else if (percent < prev) {
                        trend.setText(" ↓");
                        trend.getStyle().set("color", "red");
                    }
                }

                prev = percent;

                HorizontalLayout right = new HorizontalLayout(value, trend);
                right.setSpacing(false);

                row.add(left, right);
                timeline.add(row);
            }

            card.add(new H4("Progress Timeline"), timeline, new Hr());

            // =========================
            // 💡 SUGGESTIONS
            // =========================
            H4 suggestionsHeader = new H4("Personalized Recommendations");
            card.add(suggestionsHeader);

            List<String> suggestions = SuggestionUtil.getSuggestions(list);

            if (suggestions.isEmpty()) {
                card.add(new Paragraph("No suggestions available. Keep up the good work!"));
            } else {
                for (String s : suggestions) {

                    HorizontalLayout row = new HorizontalLayout();
                    row.setAlignItems(Alignment.CENTER);
                    row.setWidthFull();

                    row.addClassNames(
                            LumoUtility.Background.CONTRAST_5,
                            LumoUtility.BorderRadius.MEDIUM
                    );

                    row.getStyle().set("padding", "10px 15px");

                    var icon = VaadinIcon.LIGHTBULB.create();
                    icon.getStyle().set("color", "#E67E22");

                    Span text = new Span(s);

                    row.add(icon, text);
                    card.add(row);
                }
            }

        } catch (Exception ex) {
            card.add(new Span("Unable to load data: " + ex.getMessage()));
        }

        add(card);
    }

    // =========================
    // SKILL BREAKDOWN
    // =========================
    private VerticalLayout createCategoryBreakdown(List<AssessmentResult> list) {

        double coding = 0, aptitude = 0, core = 0;
        int cCount = 0, aCount = 0, coreCount = 0;

        for (AssessmentResult a : list) {
            double percent = (a.getScoreObtained() * 100.0) / a.getMaxScore();

            switch (a.getAssessmentType()) {
                case "Coding":
                    coding += percent; cCount++; break;
                case "Aptitude":
                    aptitude += percent; aCount++; break;
                case "Interview":
                    core += percent; coreCount++; break;
            }
        }

        if (cCount > 0) coding /= cCount;
        if (aCount > 0) aptitude /= aCount;
        if (coreCount > 0) core /= coreCount;

        VerticalLayout layout = new VerticalLayout();

        layout.add(
                createBar("Coding", coding, "#4A90E2"),
                createBar("Aptitude", aptitude, "#F5A623"),
                createBar("Core", core, "#27ae60")
        );

        return layout;
    }

    private VerticalLayout createBar(String label, double value, String color) {

        Span title = new Span(label + " - " + String.format("%.0f%%", value));

        Div bg = new Div();
        bg.setWidthFull();
        bg.getStyle()
                .set("background", "#eee")
                .set("height", "10px")
                .set("border-radius", "6px");

        Div fill = new Div();
        fill.setWidth(Math.min(value, 100) + "%");
        fill.getStyle()
                .set("background", color)
                .set("height", "100%")
                .set("border-radius", "6px");

        bg.add(fill);

        return new VerticalLayout(title, bg);
    }
}