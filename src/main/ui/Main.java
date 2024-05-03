package ui;

import model.Event;
import model.EventLog;

// starts the program.
public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Event thisEvent : EventLog.getInstance()) {
                System.out.println(thisEvent.toString() + "\n\n");
            }
        }));
        new GUI();
    }

}
