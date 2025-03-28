package exceptions;

public final class AudioPlayerLoadingException extends RuntimeException
{
    public AudioPlayerLoadingException()
    {
        super();
    }

    public AudioPlayerLoadingException(String message)
    {
        super(message);
    }
}
