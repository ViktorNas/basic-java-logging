import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;  

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Log
{
    private static File path = new File("C:\\directory\\example");
    private static Log logs[] = new Log[100];
    private static int count;
    
    public static Log[] get() {return logs;}
    
    public static void writeAll(String str)
    {
        for(Log log : logs)
        {
            if(log != null) log.write(str);
        }
    }
    
    public static String[][] readAll()
    {
        String lines[][] = new String[count][256];
        
        for(int i = 0; i < logs.length; i++)
        {
            if(logs[i] != null) lines[i] = logs[i].read();
        }
        
        return lines;
    }
    
    public static void clearAll()
    {
        for(Log log : logs)
        {
            if(log != null) log.clear();
        }
    }
    
    public static void closeAll()
    {
        for(int i = 0; i < logs.length; i++)
        {
            if(logs[i] != null) logs[i].close();
            logs[i] = null;
        }
    }
    
    private File file = null;
    private BufferedWriter in = null;
    private BufferedReader out = null;
    
    public Log(String name)
    {
        file = new File(String.format("%s\\%s.log", path.getPath(), name));
        try
        {
            if(!file.exists()) file.createNewFile();
            
            in = new BufferedWriter(new FileWriter(file, true));
            out = new BufferedReader(new FileReader(file));
            
            logs[count++] = this;
        }
        catch(IOException e) {System.err.println(e);}
    }
    
    public File getFile() {return file;}
    
    public void write(String str)
    {
        LocalDateTime date = LocalDateTime.now();
        String time = date.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        try
        {
            if(out.lines().toArray().length > 0) in.newLine();
            in.write(String.format("[%s] %s", time, str));
            in.flush();
        }
        catch(IOException e) {System.err.println(e);}
    }
    
    public String[] read() {return (String[]) out.lines().toArray();}
    
    public void clear()
    {
        try
        {
            in.close();
            in = new BufferedWriter(new FileWriter(file));
            
            in.write("");
            in.flush();
            
            in.close();
            in = new BufferedWriter(new FileWriter(file, true));
        }
        catch(IOException e) {System.err.println(e);}
    }
    
    public void close()
    {
        try
        {
            if(in != null) in.close();
            if(out != null) out.close();
        }
        catch(IOException e) {System.err.println(e);}
    }
}
