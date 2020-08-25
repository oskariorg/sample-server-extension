package flyway.example;

import fi.nls.oskari.db.ViewHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_0_3__initial_db_content extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        // add applications based on json under /src/main/resources/json/views/
        ViewHelper.insertView(context.getConnection(), "geoportal-3857.json");
        ViewHelper.insertView(context.getConnection(), "publisher-template.json");
        ViewHelper.insertView(context.getConnection(), "geoportal-3067.json");
        ViewHelper.insertView(context.getConnection(), "embedded-3857.json");
    }
}
