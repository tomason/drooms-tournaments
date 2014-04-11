package org.drooms.tournaments.client.interactive.util;

public interface OutputDevice {

    public void print(String format, Object... arguments);

    public void printLine();

    public void printLine(String format, Object... arguments);

    public String readLine();

    public String readLine(String format, Object... arguments);

    public String readPassword();

    public String readPassword(String format, Object... arguments);

    public static class Factory {
        public static OutputDevice getDevice() {
            if (System.console() == null) {
                return new SystemOutOutputDevice();
            } else {
                return new ConsoleOutputDevice();
            }
        }
    }

}
