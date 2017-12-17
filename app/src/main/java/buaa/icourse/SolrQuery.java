package buaa.icourse;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class SolrQuery {

    public JSONArray work(String q) throws IOException, JSONException {
        JsonRead jr = new JsonRead();
        JSONObject json = jr.readJsonFromUrl(q);

        JSONObject json2 = json.getJSONObject("response");

        JSONArray jsonArray = json2.getJSONArray("docs");

        double score;
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject courseData = jsonArray.getJSONObject(i);
            score = courseData.getDouble("score");
            if (score < 5)
                continue;
            System.out.println(score);
            System.out.println(courseData.get("college_id")+"  :  "+courseData.get("name"));
        }
        return jsonArray;
    }

}
