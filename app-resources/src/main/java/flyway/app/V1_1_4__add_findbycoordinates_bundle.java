package flyway.app;

import fi.nls.oskari.domain.map.view.Bundle;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.util.List;

public class V1_1_4__add_findbycoordinates_bundle extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        Bundle findByCoordinatesBundle = new Bundle("findbycoordinates");
        Connection connection = context.getConnection();
        List<Long> appsetupIds = AppSetupHelper.getSetupsForType(connection,"DEFAULT");
        AppSetupHelper.addOrUpdateBundleInApps(connection, findByCoordinatesBundle, appsetupIds);
    }
}
