package com.example;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Test {
  public static void main(String[] args) {
    port(8080);

    // Configuration des en-têtes CORS pour toutes les réponses
    before((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
    });

    options("/*", (request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
      return "OK";
    });

    get("/especes", (req, res) -> {
      String token = req.headers("Authorization");
      if (!isValidToken(token)) {
        res.status(401);
        return "Token invalide ou manquant";
      }

      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/wildlens", "wildlens_user", "iEI8NTMn7vDzTng");
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM espece";
        ResultSet resultSet = stmt.executeQuery(sql);

        List<JsonObject> speciesList = new ArrayList<>();

        while (resultSet.next()) {
          JsonObject speciesObject = new JsonObject();
          speciesObject.addProperty("espece_id", resultSet.getInt("espece_id"));
          speciesObject.addProperty("espece_nom", resultSet.getString("espece_nom"));
          speciesObject.addProperty("espece_description", resultSet.getString("espece_description"));
          speciesObject.addProperty("espece_habitat", resultSet.getString("espece_habitat"));
          speciesObject.addProperty("espece_nom_latin", resultSet.getString("espece_nom_latin"));
          speciesObject.addProperty("espece_fun_fact", resultSet.getString("espece_fun_fact"));
          speciesObject.addProperty("espece_famille", resultSet.getString("espece_famille"));
          speciesObject.addProperty("espece_region", resultSet.getString("espece_region"));
          speciesObject.addProperty("espece_taille", resultSet.getString("espece_taille"));

          speciesList.add(speciesObject);
        }

        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(speciesList).getAsJsonArray();
        res.type("application/json");
        return jsonArray.toString();
      } catch (Exception e) {
        e.printStackTrace();
        res.status(500);
        return "Une erreur s'est produite lors de la récupération des données.";
      }
    });
  }

  private static boolean isValidToken(String token) {
    return true; // Implémentez la validation réelle du token ici
  }
}
