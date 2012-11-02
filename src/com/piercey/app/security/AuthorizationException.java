package com.piercey.app.security;

public class AuthorizationException extends Exception
{
	private static final long serialVersionUID = 7016645749165884573L;

	public AuthorizationException()
	{
		super();
	}

	public AuthorizationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AuthorizationException(String message)
	{
		super(message);
	}

	public AuthorizationException(Throwable cause)
	{
		super(cause);
	}
}
