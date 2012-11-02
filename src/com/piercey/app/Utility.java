package com.piercey.app;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility
{
	private static final Logger logger = LoggerFactory.getLogger(Utility.class);
	
	public static String unwindStack(Throwable exception)
	{
		try
		{
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(stringWriter);
			
			exception.printStackTrace(printWriter);
			final String stackTrace = stringWriter.toString();
			
			return stackTrace;
		}
		catch (Exception e)
		{
			logger.error(e.toString());
			return "Stack Trace Unavailable: " + e.getMessage();
		}
	}

}
