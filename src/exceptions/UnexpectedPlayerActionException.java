package exceptions;

public final class UnexpectedPlayerActionException extends IllegalStateException
{
    public UnexpectedPlayerActionException()
    {
        super();
    }

    public UnexpectedPlayerActionException(String message)
    {
        super(message);
    }
}
