package exceptions;

public final class UnexpectedChestTypeException extends IllegalStateException
{
    public UnexpectedChestTypeException()
    {
        super();
    }

    public UnexpectedChestTypeException(String message)
    {
        super(message);
    }
}
