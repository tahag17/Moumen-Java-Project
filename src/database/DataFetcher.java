package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataFetcher {

    public Map<String, Integer> fetchEducationLevelData() {
        return fetchData("niveau_etude");
    }

    public Map<String, Integer> fetchExperienceLevelData() {
        return fetchData("experience_level");
    }

    private Map<String, Integer> fetchData(String column) {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT " + column + ", COUNT(*) AS job_count FROM job_details GROUP BY " + column;

        try (Connection connection = new PostgreSQLConnection().connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String key = resultSet.getString(column);
                int count = resultSet.getInt("job_count");
                data.put(key, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
