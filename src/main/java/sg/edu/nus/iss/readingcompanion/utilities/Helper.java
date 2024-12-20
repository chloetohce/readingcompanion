package sg.edu.nus.iss.readingcompanion.utilities;

import java.util.List;

public class Helper {
    public static final <T> String listToString(List<T> list) {
        String result = "";
        for (T e : list) {
            result += e.toString();
            result += ", ";
        }
        return result.substring(0, result.length() - 2);
    }
    
}
