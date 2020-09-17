package flyway.example3d;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class V1_2__setup_dimension_change extends BaseJavaMigration {
    private static final String BUNDLE_NAME = "dimension-change";
    private static final String APPLICATION_3D_NAME = "geoportal-3d";
    private static final String APPLICATION_2D_NAME = "geoportal";

    public void migrate(Context context) throws SQLException {
        Connection conn = context.getConnection();
        // Set 3D view's uuid for 2D default and user appsetups
        List<Long> viewIds2d = AppSetupHelper.getSetupsForUserAndDefaultType(conn, APPLICATION_2D_NAME);
        String uuid3d = AppSetupHelper.getUuidForDefaultSetup(conn, APPLICATION_3D_NAME);
        AppSetupHelper.addOrUpdateBundleInApps(conn, generateDimensionChange(uuid3d), viewIds2d);

        // Set default 2D view's uuid for 3D default appsetup
        List<Long> viewIds3d = AppSetupHelper.getSetupsForUserAndDefaultType(conn, APPLICATION_3D_NAME);
        String uuid2d =  AppSetupHelper.getUuidForDefaultSetup( conn, APPLICATION_2D_NAME);
        AppSetupHelper.addOrUpdateBundleInApps(conn, generateDimensionChange(uuid2d), viewIds3d);
    }

    private Bundle generateDimensionChange(String uuid) {
        Bundle bundle = new Bundle(BUNDLE_NAME);
        String conf = JSONHelper.getStringFromJSON(
                JSONHelper.createJSONObject("uuid", uuid)
                ,"{}");
        bundle.setConfig(conf);
        return bundle;
    }
}
