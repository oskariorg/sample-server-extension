package flyway.example;

import fi.nls.oskari.db.DBHandler;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

/**
 * Created by SMAKINEN on 30.1.2018.
 */
public class V1_0_0__initial_db_content implements JdbcMigration {

    public void migrate(Connection connection)
            throws Exception {

        DBHandler.setupAppContent(connection, "app-example");
    }
}
