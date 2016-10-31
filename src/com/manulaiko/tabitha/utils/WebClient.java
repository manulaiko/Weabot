package com.manulaiko.tabitha.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import com.manulaiko.tabitha.Console;

/**
 * Class for executing HTTP Request.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class WebClient
{
    /**
     * Executes an HTTP Request.
     *
     * @param URL     URL to request.
     * @param params  Params to send.
     * @param method  Request method.
     * @param headers Headers.
     *
     * @return Response string.
     */
    public static String execute(String URL, String params, String method, Map<String, String> headers)
    {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            for(Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(params);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            return response.toString();
        } catch (Exception e) {
            Console.println("Couldn't execute "+ method +" request!");
            Console.println(e.getMessage());

            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Executes a POST request.
     *
     * @param url    URL to request.
     * @param params Params to send.
     *
     * @return Response string.
     */
    public static String post(String url, Map<String, String> params)
    {
        String p = WebClient.parseParams(params);

        return WebClient.execute(url, p, "POST", new HashMap<>());
    }

    /**
     * Executes a GET request.
     *
     * @param url    URL to request.
     * @param params Params to send.
     *
     * @return Response string.
     */
    public static String get(String url, Map<String, String> params)
    {
        String p = WebClient.parseParams(params);

        return WebClient.execute(url, p, "GET", new HashMap<>());
    }

    /**
     * Converts a map to a HTTP params set.
     *
     * @param params Params to convert.
     *
     * @return Converted params.
     */
    public static String parseParams(Map<String, String> params)
    {
        String p = "";
        for(Map.Entry<String, String> param : params.entrySet()) {
            p += param.getKey() +"="+ param.getValue() +"&";
        }
        p = p.substring(0, p.length() - 1);

        return p;
    }
}
