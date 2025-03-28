package exceptions;

public final class UnexpectedLevelNumberException extends IllegalStateException
{
    public UnexpectedLevelNumberException()
    {
        super();
    }

    public UnexpectedLevelNumberException(String message)
    {
        super(message);
    }
}
