package com.piercey.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationLogger
{
	private final Logger logger;

	public ApplicationLogger(Class<?> classType)
	{
		logger = LoggerFactory.getLogger(classType);
	}

	public void error(String message)
	{
		if (logger.isTraceEnabled()) // speed-up when not erring
			logger.error(message);
	}

	public void error(Throwable exception)
	{
		if (logger.isTraceEnabled()) // speed-up when not erring
			logger.error(unwindStack(exception));
	}

	public void error(String message, Throwable exception)
	{
		if (logger.isTraceEnabled()) // speed-up when not erring
			logger.error(message + ": " + unwindStack(exception));
	}

	public void info(String message)
	{
		if (logger.isInfoEnabled()) // speed-up when not informing
			logger.info(message);
	}

	public void info(Throwable exception)
	{
		if (logger.isInfoEnabled()) // speed-up when not informing
			logger.info(unwindStack(exception));
	}

	public void info(String message, Throwable exception)
	{
		if (logger.isInfoEnabled()) // speed-up when not informing
			logger.info(message + ": " + unwindStack(exception));
	}

	public void warn(String message)
	{
		if (logger.isWarnEnabled()) // speed-up when not warning
			logger.warn(message);
	}

	public void warn(Throwable exception)
	{
		if (logger.isWarnEnabled()) // speed-up when not warning
			logger.warn(unwindStack(exception));
	}

	public void warn(String message, Throwable exception)
	{
		if (logger.isWarnEnabled()) // speed-up when not warning
			logger.warn(message + ": " + unwindStack(exception));
	}

	public void debug(String message)
	{
		if (logger.isDebugEnabled()) // speed-up when not debugging
			logger.debug(message);
	}

	public void debug(Throwable exception)
	{
		if (logger.isDebugEnabled()) // speed-up when not debugging
			logger.debug(unwindStack(exception));
	}

	public void debug(String message, Throwable exception)
	{
		if (logger.isDebugEnabled()) // speed-up when not debugging
			logger.debug(message + ": " + unwindStack(exception));
	}

	public void trace(String message)
	{
		if (logger.isTraceEnabled()) // speed-up when not tracing
			logger.trace(message);
	}

	public void trace(Throwable exception)
	{
		if (logger.isTraceEnabled()) // speed-up when not tracing
			logger.trace(unwindStack(exception));
	}

	public void trace(String message, Throwable exception)
	{
		if (logger.isTraceEnabled()) // speed-up when not tracing
			logger.trace(message + ": " + unwindStack(exception));
	}

	public void executionTrace()
	{
		if (logger.isTraceEnabled()) // speed-up when not tracing
		{
			final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			logger.trace(formatTraceMessage(null, stackTrace[2]));
		}
	}

	public void executionTrace(String message)
	{
		if (logger.isTraceEnabled()) // speed-up when not tracing
		{
			final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			logger.trace(formatTraceMessage(message, stackTrace[2]));
		}
	}

	private String formatTraceMessage(String message, StackTraceElement stackElement)
	{
		try
		{
			final StringBuilder traceMessage = new StringBuilder();

			traceMessage.append(String.format("Execution Trace: [%s:%d] %s()",
				stackElement.getFileName(), stackElement.getLineNumber(), stackElement.getMethodName()));

			if (message != null && message.length() > 0)
				traceMessage.append(": " + traceMessage);

			return traceMessage.toString();
		}
		catch (Exception e)
		{
			this.error(e);
			return "Trace unavailable due to previous errors...";
		}
	}

	public static String unwindStack(Throwable exception)
	{
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);

		exception.printStackTrace(printWriter);
		final String stackTrace = stringWriter.toString();

		return stackTrace;
	}
}
