package exceptions;

public final class UnexpectedObjectTypeException extends IllegalStateException
{
    public UnexpectedObjectTypeException()
    {
        super();
    }

    public UnexpectedObjectTypeException(String message)
    {
        super(message);
    }
}
