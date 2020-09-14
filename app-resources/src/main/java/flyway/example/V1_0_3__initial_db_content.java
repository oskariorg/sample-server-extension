package flyway.example;

import org.oskari.helpers.AppSetupHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_0_3__initial_db_content extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        // add applications based on json under /src/main/resources/json/views/
        AppSetupHelper.create(context.getConnection(), "/json/apps/geoportal-3857.json");
        AppSetupHelper.create(context.getConnection(), "/json/apps/publisher-template.json");
        AppSetupHelper.create(context.getConnection(), "/json/apps/geoportal-3067.json");
        AppSetupHelper.create(context.getConnection(), "/json/apps/embedded-3857.json");
    }
}
