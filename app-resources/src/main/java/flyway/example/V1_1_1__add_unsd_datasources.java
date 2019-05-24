package flyway.example;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Register UN statistics datasource
 */
public class V1_1_1__add_unsd_datasources implements JdbcMigration {

    private static final String layer = "ne_110m_countries";
    private static final String prefix = "UN Agenda 2030 SD Goal";

    public void migrate(Connection connection) throws Exception {
        addDS(connection, "Affordable and clean energy", "7");
        addDS(connection, "Decent work and economic growth", "8");
        addDS(connection, "Industry, innovation and infrastructure", "9");
        addDS(connection, "Reduced inequalities", "10");
        addDS(connection, "Sustainable cities and communities", "11");
        addDS(connection, "Responsible consumption and production", "12");
    }

    private void addDS(Connection conn, String name, String goal) throws SQLException {
        name = prefix + " " + goal + ": " + name;
        try (PreparedStatement statement = conn.prepareStatement(getDSInsert(name, goal))){
            statement.execute();
        }

        try (PreparedStatement statement = conn.prepareStatement(getRegionsetLinkInsert(name))){
            statement.execute();
        }
    }


    private String getDSInsert(String name, String goal) {

        return "INSERT INTO oskari_statistical_datasource (locale, config, plugin) " +
            "VALUES ('{\"en\":{\"name\":\"" + name + "\"}}', '{ \"goal\": \"" + goal + "\" }', 'UNSD');";
    }

    private String getRegionsetLinkInsert(String name) {
        return "INSERT INTO oskari_statistical_layer(datasource_id, layer_id, config) " +
            "VALUES(" +
                "(SELECT id FROM oskari_statistical_datasource " +
                    "WHERE locale like '%" + name + "%'), " +
                "(SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = '" + layer + "'), " +
                "'{}');";
    }
}
