package flyway.example3d;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.ViewTypes;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.FlywayHelper;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class V1_2__setup_dimension_change implements JdbcMigration {
    private static final Logger LOG = LogFactory.getLogger(V1_2__setup_dimension_change.class);
    private static final String BUNDLE_NAME = "dimension-change";
    private static final String APPLICATION_3D_NAME = "geoportal-3d";
    private static final String APPLICATION_2D_NAME = "geoportal";
    private static final String VIEW_FINLAND = "Finland";
    private static final String FINLAND_PAGE_NAME = "geoportal_white";
    private static final String VIEW_WORLD = "World";
    private static final String WORLD_PAGE_NAME = "geoportal";


    public void migrate(Connection conn) throws SQLException {
        // Set World 3D view uuid for World default and user appsetups
        List<Long> viewIds = getViewIds(conn, WORLD_PAGE_NAME, APPLICATION_2D_NAME);
        String uuid = getViewUuid( conn, WORLD_PAGE_NAME, APPLICATION_3D_NAME);
        setBundleForViews(conn, viewIds, uuid);
        // Set World 2D view uuid for World 3D default appsetup
        List<Long> viewIds = getViewIds(conn, WORLD_PAGE_NAME, APPLICATION_3D_NAME);
        String uuid = getViewUuid( conn, WORLD_PAGE_NAME, APPLICATION_2D_NAME);
        setBundleForViews(conn, viewIds, uuid);
        // Set Finland 3D view uuid for Finland default and user appsetups
        viewIds = getViewIds(conn, FINLAND_PAGE_NAME, APPLICATION_2D_NAME);
        uuid = getViewUuid( conn, FINLAND_PAGE_NAME, APPLICATION_3D_NAME);
        setBundleForViews(conn, viewIds, uuid);
        // Set Finland 2D view uuid for Finland 3D default appsetup
        viewIds = getViewIds(conn, FINLAND_PAGE_NAME, APPLICATION_3D_NAME );
        uuid = getViewUuid( conn, FINLAND_PAGE_NAME, APPLICATION_2D_NAME);
        setBundleForViews(conn, viewIds, uuid);
    }
    private void setBundleForViews (Connection conn, List<Long> ids, String uuid) throws SQLException {
        String conf = JSONHelper.getStringFromJSON(
                JSONHelper.createJSONObject("uuid", uuid)
                ,"{}");
        for (Long id : ids) {
            if (!FlywayHelper.viewContainsBundle(conn, BUNDLE_NAME, id)) {
                FlywayHelper.addBundleWithDefaults(conn, id, BUNDLE_NAME);
            }
            Bundle bundle = FlywayHelper.getBundleFromView(conn, BUNDLE_NAME, id);
            bundle.setConfig(conf);
            FlywayHelper.updateBundleInView(conn, bundle, id);
        }
        LOG.info("Set view uuid:", uuid, "to dimension change bundle config. Updated views:", ids);
    }

    private List<Long> getViewIds (Connection conn, String pageName, String appName) throws SQLException {
        List<Long> ids = new ArrayList<>();
        final String sql = "SELECT id FROM portti_view WHERE page=? AND application=? AND type IN (?,?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pageName);
            statement.setString(2, appName);
            statement.setString(3, ViewTypes.DEFAULT);
            statement.setString(4, ViewTypes.USER);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("id"));
                }
            }
        }
        return ids;
    }
    private String getViewUuid(Connection conn, String pageName, String appName) {
        final String sql = "SELECT uuid FROM portti_view WHERE page=? and application=? and type=? limit 1";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pageName);
            statement.setString(2, appName);
            statement.setString(3, ViewTypes.DEFAULT);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    return rs.getString("uuid");
                }
            }
        } catch (Exception e) {
            LOG.error(e, "Error getting default view uuid for app: " + appName + " and page: " + pageName);
        }
        return null;
    }
}
