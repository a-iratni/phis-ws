//******************************************************************************
//                            EnvironmentDates.java
// SILEX-PHIS
// Copyright © INRA 2018
// Creation date: 5 Feb. 2018
// Contact: arnaud.charleroy@inra.fr, morgane.vidal@inra.fr, anne.tireau@inra.fr, 
//          pascal.neveu@inra.fr
//******************************************************************************
package opensilex.service.utils.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import opensilex.service.configuration.DateFormats;

/**
 * Environmental measure dates manager.
 * @see opensilex.service.utils.dates.Dates
 * @author Arnaud Charleroy <arnaud.charleory@inra.fr>, Morgane Vidal <morgane.vidal@inra.fr>
 */
public class EnvironmentDates extends Dates {
    
    /**
     * Generates a date parser using the list of formats expected to be used 
     * for environmental data.
     * @see opensilex.service.configuration.DateFormats AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS
     * @return the dates parser
     */
    protected static DateTimeParser[] getUsedParsers() {
        DateTimeParser[] parsers = new DateTimeParser[DateFormats.AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS.size()];
        int count = 0;
        for (String format : DateFormats.AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS) {
            parsers[count] = DateTimeFormat.forPattern(format).getParser();
            count++;
        }
        return parsers;
    }
    
    /**
     * Converts a string date in DateTime if the date format corresponds to one 
     * of the used date format specified in the environment usable date formats.
     * @see opensilex.service.configuration.DateFormats AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS
     * @param stringDate the date to be converted
     * @return DateTime the DateTime corresponding to the date string given. 
     *         null if the date could not be converted
     */
    public static DateTime stringToUsedDateTime(String stringDate) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, getUsedParsers()).toFormatter();
        try {
            return formatter.withOffsetParsed().parseDateTime(stringDate);
        } catch (Exception e) {
            return null;
        }
    }    
    
    /**
     * Tries to parse the given date in one of the date formats expected to be 
     * received.
     * @see opensilex.service.configuration.DateFormats AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS
     * @param date the date string to parse
     * @return true if the date format is correct
     *         false if the date format is not in the 
     *         DateFormats.AUTHORIZED_USER_ENVIRONMENT_DATE_FORMATS list
     */
    public static boolean isUsedDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, getUsedParsers()).toFormatter();
        try {
            DateTime d = formatter.parseDateTime(date);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
