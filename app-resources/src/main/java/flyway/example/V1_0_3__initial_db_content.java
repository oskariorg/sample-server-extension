package flyway.example;

import org.oskari.helpers.AppsetupHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_0_3__initial_db_content extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        // add applications based on json under /src/main/resources/json/views/
        AppsetupHelper.create(context.getConnection(), "geoportal-3857.json");
        AppsetupHelper.create(context.getConnection(), "publisher-template.json");
        AppsetupHelper.create(context.getConnection(), "geoportal-3067.json");
        AppsetupHelper.create(context.getConnection(), "embedded-3857.json");
    }
}
