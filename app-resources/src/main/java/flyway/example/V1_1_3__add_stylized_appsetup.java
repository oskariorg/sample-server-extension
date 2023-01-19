package flyway.example;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

public class V1_1_3__add_stylized_appsetup extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        // add applications based on json under /src/main/resources/json/apps/
        AppSetupHelper.create(context.getConnection(), "/json/apps/geoportal-3067_stylized.json");
    }
}
