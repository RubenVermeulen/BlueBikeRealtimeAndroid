package be.rubenvermeulen.android.bluebikert;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ruben on 20/10/2016.
 */

public class Locations {
    private static Map<String, String> keyValues = new TreeMap<>();

    private static void initialize() {
        if (keyValues.isEmpty()) {
            keyValues.put("Gent-Sint-Pieters", "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsensintpieters.json");
            keyValues.put("Gent-Dampoort", "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsendampoort.json");
        }
    }

    public static Map<String, String> locations() {
        initialize();

        return keyValues;
    }

    public static Map<String, String> filtered(String query) {
        initialize();

        Map<String, String> filteredResults = new TreeMap<>();

        for (String key : keyValues.keySet()) {
            if (key.toLowerCase().contains(query.toLowerCase())) {
                filteredResults.put(key, keyValues.get(key));
            }
        }

        return filteredResults;
    }
}
