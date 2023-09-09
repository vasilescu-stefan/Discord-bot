package bot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;




public class Search {
    private final String LINK = "https://en.wikipedia.org/w/api.php?";


    public Template searchPages(String query) throws ParseException {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "query");
        params.put("prop", "extracts");
        params.put("exintro","");
        params.put("explaintext","");
        params.put("format","json");
        params.put("titles", query);
        String response = executeRequest(Commands.buildLink(params), "GET");
        int titlePosition=response.indexOf("\"title\"");
        int extractPosition=response.indexOf("\"extract\"");
        int pageidPosition=response.indexOf("\"pageid\"");
        int nsPosition=response.indexOf("\"ns\"");
        System.out.println(titlePosition);
        System.out.println(extractPosition);
        System.out.println(pageidPosition);
        System.out.println(nsPosition);
        String title=response.substring(titlePosition+9, extractPosition);
        String extract=response.substring(extractPosition+11);
        String pageid=response.substring(pageidPosition+9, nsPosition);
        System.out.println(pageid);
        title=title.substring(0,title.length()-2);
        
        extract=extract.substring(0,extract.length()-1);
        pageid=pageid.substring(0, pageid.length()-1);
        
  
        Template t= new Template();
        t.setTitle(title);
        t.setContent(extract);
        System.out.println(pageid);
        t.setPageId(Integer.parseInt(pageid));
        
        return t;
    }



    private String executeRequest(String urlParameters, String type) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(LINK);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Request failed";
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return "Request failed";
        }
        try {
            connection.setRequestMethod(type);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Request failed";
        }

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        try {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
            rd.close();
            if (connection != null) {
                connection.disconnect();
            }
            return response.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}