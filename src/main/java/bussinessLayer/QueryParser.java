package bussinessLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    public static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> queryMap = new HashMap<>();

        Pattern pattern = Pattern.compile("(\\w+):\"([^\"]+)\"|(\\w+):([^\\s\"]+)");
        Matcher matcher = pattern.matcher(rawQuery);

        while (matcher.find()) {
            if (matcher.group(1) != null && matcher.group(2) != null) {
                queryMap.put(matcher.group(1), matcher.group(2));
            }
            if (matcher.group(3) != null && matcher.group(4) != null) {
                queryMap.put(matcher.group(3), matcher.group(4));
            }
        }

        return queryMap;
    }
}
