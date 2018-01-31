package buaa.icourse;

/**
 * Created by zhaoy on 2017/11/18.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
//import net.sf.json.JSONObject;

public class JsonRead {
    String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String q, int type) throws MalformedURLException, IOException{

        JSONObject json = null;

        q = URLEncoder.encode(q,"UTF-8");

        String url = "";
        if (type == 1)//search
            url = "http://60.205.211.127:8080/solr/mynode/select?rows=3000&fl=*%2Cscore&wt=json&indent=true"
                + "&q=" + q;
        else
            url = "http://60.205.211.127:8080/solr/mynode/select?q=*%3A*&fq=college_id%3A%5B"
                    +q+"+TO+"+q+"%5D&rows=3000&wt=json&indent=true";
            //url = "http://10.2.28.124:8080/solr/mynode/select?q=*%3A*&fq=college_id%3A"+q+"&rows=10000&wt=json&indent=true";


        int choose = 0;
        InputStream is;
        while(true) {
            try {
                is = new URL(url).openStream();
                break;
            } catch (IOException e){
                System.out.println(e.toString());
            }

        }
        try {
            BufferedReader rd = null; // = new BufferedReader(new InputStreamReader(new GZIPInputStream(is), Charset.forName("UTF-8")));
            if(choose == 0){
                rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            }
            else if(choose == 1) {
                rd = new BufferedReader(new InputStreamReader(new GZIPInputStream(is), Charset.forName("UTF-8")));
            }
            String jsonText = readAll(rd);
            //JSONObject json = JSONObject.fromObject(jsonText);
            json = new JSONObject(jsonText.toString());

            //System.out.println(jsonText);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return json;
    }
}

