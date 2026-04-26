package ino.placement.util;

import com.vaadin.flow.server.VaadinSession;
import ino.placement.entity.Student;

public class SessionUtil {

    private static final String USER = "loggedUser";

    public static void setUser(Student student) {
        VaadinSession.getCurrent().setAttribute(USER, student);
    }

    public static Student getUser() {
        return (Student) VaadinSession.getCurrent().getAttribute(USER);
    }

    public static void logout() {
        VaadinSession.getCurrent().setAttribute(USER, null);
    }
}