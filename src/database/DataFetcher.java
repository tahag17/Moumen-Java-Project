package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataFetcher {

    public Map<String, Integer> fetchRegionData() {
        return fetchData("region");
    }

    public Map<String, Integer> fetchContractTypeData() {
        return fetchData("type_du_contrat");
    }

    public Map<String, Integer> fetchExperienceData() {
        return fetchData("experience");
    }

    public Map<String, Integer> fetchEducationLevelData() {
        return fetchData("niveau_d_etudes");
    }

    public Map<String, Integer> fetchTopSecteursDActivite() {
        return fetchTopData("secteur_d_activite", "secteurs_d_activite");
    }

    public Map<String, Integer> fetchTopHardSkills() {
        return fetchTopData("hard_skills", "hard_skills");
    }

    private Map<String, Integer> fetchData(String column) {
        Map<String, Integer> data = new HashMap<>();
        String query = String.format(
                "SELECT %s, COUNT(*) AS job_count FROM jobs WHERE %s IS NOT NULL AND %s <> '' GROUP BY %s",
                column, column, column, column);

        try (Connection connection = new PostgreSQLConnection().connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String key = resultSet.getString(column);
                if (key != null && !key.equalsIgnoreCase("Unknown")) {
                    int count = resultSet.getInt("job_count");
                    data.put(key, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<String, Integer> fetchTopData(String column, String table) {
        Map<String, Integer> data = new HashMap<>();
        String query = String.format(
                "SELECT %s, COUNT(*) AS count " +
                        "FROM %s " +
                        "WHERE %s IS NOT NULL AND %s <> '' " +
                        "GROUP BY %s " +
                        "ORDER BY count DESC " +
                        "LIMIT 20", column, table, column, column, column);

        try (Connection connection = new PostgreSQLConnection().connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String key = resultSet.getString(column);
                if (key != null && !key.equalsIgnoreCase("Unknown")) {
                    int count = resultSet.getInt("count");
                    data.put(key, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
