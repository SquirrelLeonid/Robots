package log;

public final class Logger
{
    private static final LogWindowSource defaultLogSource;
    //static и... что это вообще?
    static {
        defaultLogSource = new LogWindowSource(10);
    }
    
    private Logger()
    {
    }

    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
