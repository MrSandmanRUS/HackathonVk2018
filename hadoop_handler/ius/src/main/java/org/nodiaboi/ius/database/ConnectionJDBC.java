package org.nodiaboi.ius.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.*;

public class ConnectionJDBC {

    private final Connection connection;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public ConnectionJDBC() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181/hbase");
    }

    public String executeSelect(String query) throws SQLException {
        ArrayNode resultsArray = objectMapper.createArrayNode();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        while (resultSet.next()) {
            //jsonRes += "{";
            ObjectNode resultLine = objectMapper.createObjectNode();
            int counter = 1;
            while (true) {
                Object value;
                String columnName;
                try {
                    columnName = rsmd.getColumnName(counter);
                    value = resultSet.getString(counter);
                    resultLine.put(columnName, value.toString());

                    //jsonRes += columnName + ":" + value;
                } catch (ClassCastException exc) {
                    try {
                        columnName = rsmd.getColumnName(counter);
                        value = resultSet.getArray(counter);
                        resultLine.put(columnName, value.toString());

                        //jsonRes +=
                    } catch (NullPointerException exc2) {
                        columnName = rsmd.getColumnName(counter);
                        resultLine.put(columnName, "[]");
                    }
                    catch (Exception exc2) {
                        break;
                    }
                } catch (NullPointerException exc) {
                    columnName = rsmd.getColumnName(counter);
                    resultLine.put(columnName, "");
                } catch (Exception exc) {
                    break;
                }
                ++counter;
            }
            resultsArray.add(resultLine);
            //jsonRes += "}";
        }
        resultSet.close();
        preparedStatement.close();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("values", resultsArray);
        return objectNode.toString();
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeDml(String sqlQuery) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.executeUpdate();
        connection.commit();
    }

}
