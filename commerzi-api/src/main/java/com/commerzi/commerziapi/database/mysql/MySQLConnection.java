package com.commerzi.commerziapi.database.mysql;

import com.commerzi.commerziapi.properties.SecretsPropertiesHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MySQLConnection implements InitializingBean, DisposableBean {

    private static final String PROPERTIES_TEMPLATE = "database.mysql.";

    private static final SecretsPropertiesHandler secretsPropertiesHandler = SecretsPropertiesHandler.getInstance();

    private Connection connection;

    @Override
    public void destroy() throws Exception {
        connection.close();
    }

    @Override
    public void afterPropertiesSet() throws SQLException {
        String databaseHost = secretsPropertiesHandler.getProperty(PROPERTIES_TEMPLATE + "host");
        String databasePort = secretsPropertiesHandler.getProperty(PROPERTIES_TEMPLATE + "port");
        String databaseName = secretsPropertiesHandler.getProperty(PROPERTIES_TEMPLATE + "name");
        String databaseUsername = secretsPropertiesHandler.getProperty(PROPERTIES_TEMPLATE + "username");
        String databasePassword = secretsPropertiesHandler.getProperty(PROPERTIES_TEMPLATE + "password");

        connection = DriverManager.getConnection(
            "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName,
            databaseUsername,
            databasePassword
        );

        Statement statement = connection.createStatement();
        statement.execute("select * from test");
    }

    public Connection getConnection() {
        return connection;
    }
}
