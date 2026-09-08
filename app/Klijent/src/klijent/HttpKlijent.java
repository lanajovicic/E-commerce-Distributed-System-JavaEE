package klijent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpKlijent {

    private static final String BASE_URL = "http://localhost:8080/Server/api";

    public static String get(String putanja) {
        try {
            URL url = new URL(BASE_URL + putanja);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/plain");
            return citajOdgovor(conn);
        } catch (Exception e) {
            return "GRESKA - " + e.getMessage();
        }
    }

    public static String post(String putanja, String body) {
        try {
            URL url = new URL(BASE_URL + putanja);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }
            return citajOdgovor(conn);
        } catch (Exception e) {
            return "GRESKA - " + e.getMessage();
        }
    }

    public static String put(String putanja, String body) {
        try {
            URL url = new URL(BASE_URL + putanja);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }
            return citajOdgovor(conn);
        } catch (Exception e) {
            return "GRESKA - " + e.getMessage();
        }
    }

    public static String delete(String putanja, String body) {
        try {
            URL url = new URL(BASE_URL + putanja);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
            if (body != null && !body.isEmpty()) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes("UTF-8"));
                }
            }
            return citajOdgovor(conn);
        } catch (Exception e) {
            return "GRESKA - " + e.getMessage();
        }
    }

    private static String citajOdgovor(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
        if (is == null) return "GRESKA - Nema odgovora";
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        return sb.toString();
    }
}