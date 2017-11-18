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

    public JSONObject readJsonFromUrl(String q) throws MalformedURLException, IOException{

        JSONObject json = null;

//		byte[] b = q.getBytes("gbk");//编码
//        String sa = new String(b, "gbk");//解码:用什么字符集编码就用什么字符集解码
        q = URLEncoder.encode(q,"UTF-8");


        String url = "http://10.2.28.124:8080/solr/mynode/select?rows=2000&fl=*%2Cscore&wt=json&indent=true"
                + "&q=" + q;

//		url = URLEncoder.encode(url,"UTF-8");
        System.out.println(url);
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
        //System.exit(0);
//		url = URLEncoder.encode(url,"UTF-8");
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

