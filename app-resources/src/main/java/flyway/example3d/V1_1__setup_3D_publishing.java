package flyway.example3d;

import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.domain.map.view.ViewTypes;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import fi.nls.oskari.map.view.ViewService;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.util.List;

/**
 * Creates a publication template for 3D views.
 */
public class V1_1__setup_3D_publishing extends BaseJavaMigration {
    private static final String TEMPLATE_JSON = "publisher-template-3d.json";
    private static final String METADATA_TEMPLATE_KEY = "publishTemplateUuid";
    private static final String APPLICATION_3D_NAME = "geoportal-3d";

    private ViewService viewService;

    public void migrate(Context context) throws Exception {
        Connection conn = context.getConnection();
        viewService =  new AppSetupServiceMybatisImpl();

        // Create 3D publish template view
        long templateViewId = AppSetupHelper.create(conn, TEMPLATE_JSON);
        String templateViewUuid = viewService.getViewWithConf(templateViewId).getUuid();

        // Set it as the publication template for the default 3D view.
        List<Long> viewIds = AppSetupHelper.getSetupsForApplicationByType(conn, APPLICATION_3D_NAME, ViewTypes.DEFAULT);
        for (long viewId : viewIds) {
            View geoportalView = viewService.getViewWithConf(viewId);
            geoportalView.getMetadata().put(METADATA_TEMPLATE_KEY, templateViewUuid);
            viewService.updateView(geoportalView);
        }

    }
}
