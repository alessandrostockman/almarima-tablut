package it.unibo.almarima.tablut.local.control;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TablutFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(record.getLevel() + ": ");
        builder.append(formatMessage(record));
        builder.append(System.lineSeparator());
        // pre-Java7: builder.append(System.getProperty('line.separator'));
        return builder.toString();
    }

   
}