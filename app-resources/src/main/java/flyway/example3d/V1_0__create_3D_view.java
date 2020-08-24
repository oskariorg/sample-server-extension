package flyway.example3d;

import fi.nls.oskari.db.DBHandler;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * Creates a new 3D view
 */
public class V1_0__create_3D_view extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
         // run setup based on json under /src/main/resources/setup/app-example-3d.json
        DBHandler.setupAppContent(context.getConnection(), "app-example-3d.json");
    }
}