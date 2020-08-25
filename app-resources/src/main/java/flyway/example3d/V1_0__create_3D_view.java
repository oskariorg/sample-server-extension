package flyway.example3d;

import fi.nls.oskari.db.LayerHelper;
import fi.nls.oskari.db.ViewHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * Creates a new 3D view
 */
public class V1_0__create_3D_view extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
         // insert layers based on json under /src/main/resources/json/layers/
        LayerHelper.setupLayer("hki-3d-model.json");
        LayerHelper.setupLayer("hki-3d-buildings.json");
        LayerHelper.setupLayer("nasa-features.json");
        LayerHelper.setupLayer("nasa-labels.json");
        LayerHelper.setupLayer("nasa-water-mask.json");

        // add applications based on json under /src/main/resources/json/views/
        ViewHelper.insertView(context.getConnection(), "geoportal-3d.json");
    }
}