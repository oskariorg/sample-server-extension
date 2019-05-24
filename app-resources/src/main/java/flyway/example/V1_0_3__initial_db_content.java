package flyway.example;

import fi.nls.oskari.db.DBHandler;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V1_0_3__initial_db_content implements JdbcMigration {

    public void migrate(Connection connection)
            throws Exception {
        // run setup based on json under /src/main/resources/setup/app-example.json
        DBHandler.setupAppContent(connection, "app-example");
    }
}
