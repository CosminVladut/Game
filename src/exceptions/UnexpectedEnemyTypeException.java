package exceptions;

public final class UnexpectedEnemyTypeException extends IllegalArgumentException
{
    public UnexpectedEnemyTypeException()
    {
        super();
    }

    public UnexpectedEnemyTypeException(String message)
    {
        super(message);
    }
}
