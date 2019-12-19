package flyway.example3d;

import fi.nls.oskari.db.ViewHelper;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import fi.nls.oskari.map.view.ViewService;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a publication template for 3D views.
 */
public class V1_1__setup_3D_publishing implements JdbcMigration {

    private static final String TEMPLATE_JSON = "publisher-template-3d.json";
    private static final String METADATA_TEMPLATE_KEY = "publishTemplateUuid";
    private static final String APPLICATION_3D_NAME = "geoportal-3d";
    private static final String DEFAULT_VIEW_TYPE = "DEFAULT";

    private ViewService viewService;

    public void migrate(Connection conn) throws Exception {

        viewService =  new AppSetupServiceMybatisImpl();

        // Create 3D publish template view
        long templateViewId = ViewHelper.insertView(conn, TEMPLATE_JSON);
        String templateViewUuid = viewService.getViewWithConf(templateViewId).getUuid();

        // Set it as the publication template for the default 3D views.
        for (String viewId : get3DViewsUuids(conn)) {
            View geoportalView = viewService.getViewWithConfByUuId(viewId);
            geoportalView.getMetadata().put(METADATA_TEMPLATE_KEY, templateViewUuid);
            viewService.updateView(geoportalView);
        }

    }
    public static List<String> get3DViewsUuids(Connection conn) throws Exception {
        List<String> uuids = new ArrayList<>();
        final String sql = "SELECT uuid FROM portti_view WHERE application=? and type=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, APPLICATION_3D_NAME);
            statement.setString(2, DEFAULT_VIEW_TYPE);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    uuids.add(rs.getString("uuid"));
                }
            }
            return uuids;
        }
    }
}