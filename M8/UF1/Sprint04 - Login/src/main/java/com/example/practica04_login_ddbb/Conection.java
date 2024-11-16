package com.example.practica04_login_ddbb;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Conection {

    private static final String SERVER_IP = "10.0.2.2";
    private static final String SERVER_PATH_VALIDAR = "/m8AndroidStudio/validacuenta.php";
    private static final String SERVER_PATH_CONSULTA = "/m8AndroidStudio/consultausuarios.php";

    public String ConexionPost(String username, String password) {
        String urlString = "http://" + SERVER_IP + SERVER_PATH_VALIDAR;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "usuario=" + URLEncoder.encode(username, "UTF-8") + "&contrasena=" + URLEncoder.encode(password, "UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                return "KO";
            }
        } catch (IOException e) {
            return "ConexionFail";
        }
    }

    public void solicitarDatosUsuarios(DataBaseInterna dbHelper) {
        String urlString = "http://" + SERVER_IP + SERVER_PATH_CONSULTA;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/xml");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse XML response
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
            NodeList usuarios = doc.getElementsByTagName("usuario");

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                for (int i = 0; i < usuarios.getLength(); i++) {
                    Element usuarioElement = (Element) usuarios.item(i);
                    String nombre = usuarioElement.getElementsByTagName("nombre").item(0).getTextContent();
                    String contrasena = usuarioElement.getElementsByTagName("contrasena").item(0).getTextContent();

                    ContentValues values = new ContentValues();
                    values.put("username", nombre);
                    values.put("password", contrasena);
                    values.put("fechaNacimiento", "2023-01-01"); // Placeholder for fechaNacimiento

                    db.insert(DataBaseInterna.TABLE_USER_LOGIN, null, values);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (Exception e) {
            Log.e("Conection", "Error fetching user data", e);
        }
    }

}