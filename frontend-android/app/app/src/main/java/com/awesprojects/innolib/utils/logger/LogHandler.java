package com.awesprojects.innolib.utils.logger;

import android.util.Log;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Ilya on 3/22/2018.
 */

public class LogHandler extends ConsoleHandler {

    Formatter formatter = getFormatter();
    PrintStream baseStream = LogStorageManager.getInstance().getBasePrintStream();
    PrintStream debugStream = LogStorageManager.getInstance().getDebugPrintStream();

    @Override
    public Level getLevel() {
        return Level.ALL;
    }

    @Override
    public synchronized void setLevel(Level newLevel) throws SecurityException {
        super.setLevel(newLevel);
    }

    @Override
    public Formatter getFormatter() {
        return new Formatter() {
            @Override
            public synchronized String format(LogRecord record) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(record.getMillis());
                StringBuilder sb = new StringBuilder();
                dumpCalendar(c, sb);
                return new StringBuilder()
                        .append(sb).append(" ")
                        .append(String.format("%1$-7s ",record.getLevel().getName()))
                        .append(String.format("[%1$-18s] : ",record.getLoggerName()))
                        .append(record.getMessage())
                        .toString();
            }
        };
    }

    @Override
    public void publish(LogRecord record) {
        int priority = getPriority(record);
        if (priority >= Log.INFO) {
            baseStream.println(formatter.format(record));
            Log.println(priority, record.getLoggerName(), record.getMessage());
        } else {
            debugStream.println(formatter.format(record));
        }
    }

    public int getPriority(LogRecord record) {
        Level level = record.getLevel();
        if (level == Level.SEVERE) return Log.ERROR;
        if (level == Level.WARNING) return Log.WARN;
        if (level == Level.CONFIG) return Log.INFO;
        if (level == Level.INFO) return Log.INFO;
        if (level == Level.FINE) return Log.DEBUG;
        return Log.VERBOSE;
    }

    private static void dumpCalendar(Calendar c, StringBuilder sb) {
        int ms = c.get(Calendar.MILLISECOND);
        int s = c.get(Calendar.SECOND);
        int m = c.get(Calendar.MINUTE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        sb.append(year % 10000).append("")
                .append("-")
                .append(month < 10 ? "0" + month : month)
                .append("-")
                .append(day < 10 ? "0" + day : day + "")
                .append(" ")
                .append(h < 10 ? "0" + h : h)
                .append(":")
                .append(m < 10 ? "0" + m : m)
                .append(":")
                .append(s < 10 ? "0" + s : s)
                .append(".")
                .append(ms < 100 ? (ms < 10 ? "00" + ms : "0" + ms) : ms + "");
    }
}
