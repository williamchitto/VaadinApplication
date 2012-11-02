package com.piercey.app.security;

public class AuthenticationException extends Exception
{
	private static final long serialVersionUID = -7385155171164478603L;

	public AuthenticationException()
	{
		super();
	}

	public AuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AuthenticationException(String message)
	{
		super(message);
	}

	public AuthenticationException(Throwable cause)
	{
		super(cause);
	}
}
