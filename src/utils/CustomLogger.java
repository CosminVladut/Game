package utils;

import main.GameWindow;

import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;

public final class CustomLogger
{
    private static GameWindow gameWindow;
    private static final String LOG_FILE_PATH = "errors.log";
    private static SimpleDateFormat DATE_FORMAT;

    static
    {
        try
        {
            DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        }
        catch(NullPointerException e)
        {
            System.err.println("Tipar inexistent pentru data.");
            exitWithErrorCode(1);
        }
        catch(IllegalFormatException e)
        {
            System.err.println("Tipar invalid pentru data.");
            exitWithErrorCode(1);
        }
    }

    private CustomLogger()
    {

    }

    public static void setGameWindow(GameWindow value)
    {
        gameWindow = value;
    }

    private static void exitWithErrorCode(int errorCode)
    {
        gameWindow.setCode(errorCode);
        gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
    }

    public static void logException(String message, Exception exception)
    {
        String logEntry = formatLogEntry(message, exception);
        writeToFile(logEntry);
        System.err.println("Eroare, verifica logul.");
        exitWithErrorCode(1);
    }

    public static void assertWithLogging(boolean condition, String message)
    {
        if (!condition)
        {
            logException(message, new NullPointerException());
        }
    }

    private static String formatLogEntry(String message, Exception exception)
    {
        String timestamp = DATE_FORMAT.format(new Date());
        String exceptionType = exception.getClass().getSimpleName();
        try
        {
            return String.format("[%s] [%s] %s\n", timestamp, exceptionType, message);
        }
        catch (IllegalFormatException e)
        {
            System.err.println("Nu se poate folosi formatul pentru a face data pentru loguri.");
            exitWithErrorCode(1);
        }
        return null;
    }

    private static void writeToFile(String logEntry)
    {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE_PATH, true);
             PrintWriter printWriter = new PrintWriter(fileWriter))
        {
            printWriter.print(logEntry);
        }
        catch (IOException e)
        {
            System.err.println("Nu s-a putut scrie in log.");
            exitWithErrorCode(1);
        }
    }
}
