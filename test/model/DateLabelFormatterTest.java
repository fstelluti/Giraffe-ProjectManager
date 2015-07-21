package model;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DateLabelFormatterTest
{
	@Test
	public void stringToValueShouldReturnParsedObject() throws ParseException
	{
		String text = "1995-09-01";
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Object obj = dateFormatter.parseObject(text);
		DateLabelFormatter actualFormatter = new DateLabelFormatter();
		Object objActual = actualFormatter.stringToValue(text);
        assertEquals("DateFormatter objects are not equal", obj, objActual);
    }
	
	@Test
	public void svalueToStringShouldReturnParsedString() throws ParseException
	{
		Calendar date = new GregorianCalendar();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String parsedDate = dateFormatter.format(date.getTime());
		DateLabelFormatter actualFormatter = new DateLabelFormatter();
		String parsedDateActual = actualFormatter.valueToString(date);
        assertEquals("DateFormatter strings are not equal", parsedDate, parsedDateActual);
    }

}
