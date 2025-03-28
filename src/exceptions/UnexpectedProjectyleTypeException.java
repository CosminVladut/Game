package exceptions;

public final class UnexpectedProjectyleTypeException extends IllegalStateException
{
    public UnexpectedProjectyleTypeException()
    {
        super();
    }

    public UnexpectedProjectyleTypeException(String message)
    {
        super(message);
    }
}
