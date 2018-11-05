package connectors;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONObject;



public  class RestUtils {

    public static String makeRequest(String myUrl, String httpMethod, JSONObject parameters) {

        URL url = null;
        try {
            url = new URL(myUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);

        conn.setReadTimeout(10000);

        conn.setRequestProperty("Content-Type", "application/json");
        DataOutputStream dos = null;
        int respCode = 0;
        String inputString = null;
        try {
            conn.setRequestMethod(httpMethod);

            if (Arrays.asList("POST", "PUT").contains(httpMethod)) {
                String params = parameters.toString();

                conn.setDoOutput(true);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(params);
                dos.flush();
                dos.close();
            }
            respCode = conn.getResponseCode();
            if (respCode != 200 && respCode != 201) {
                String error = inputStreamToString(conn.getErrorStream());
                return error;
            }
            inputString = inputStreamToString(conn.getInputStream());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return inputString;
    }

    public static String inputStreamToString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
