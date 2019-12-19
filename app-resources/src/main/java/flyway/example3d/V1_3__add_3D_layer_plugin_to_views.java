package flyway.example3d;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.View;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.view.AppSetupServiceMybatisImpl;
import fi.nls.oskari.map.view.ViewException;
import fi.nls.oskari.map.view.ViewService;
import fi.nls.oskari.util.FlywayHelper;
import fi.nls.oskari.util.JSONHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class V1_3__add_3D_layer_plugin_to_views implements JdbcMigration {

    private static final Logger LOG = LogFactory.getLogger(V1_3__add_3D_layer_plugin_to_views.class);
    private static final String MAP_BUNDLE_NAME = "mapfull";
    private static final String TILES_3D_LAYER_PLUGIN_ID = "Oskari.mapframework.mapmodule.Tiles3DLayerPlugin";

    private ViewService viewService = null;

    public void migrate(Connection connection) throws SQLException, ViewException {
        viewService =  new AppSetupServiceMybatisImpl();
        updateDefaultAndUserViews(connection);
    }

    private void updateDefaultAndUserViews(Connection connection) throws SQLException, ViewException {
        List<Long> viewIds = FlywayHelper.getUserAndDefaultViewIds(connection);
        for(Long viewId : viewIds) {
            View modifyView = viewService.getViewWithConf(viewId);
            Bundle mapBundle = modifyView.getBundleByName(MAP_BUNDLE_NAME);
            if (mapBundle == null) {
                LOG.warn("Adding 3D layer plugin to 2D views. Mapfull bundle not found for view: ", viewId);
                continue;
            }
            JSONObject config = JSONHelper.createJSONObject(mapBundle.getConfig());
            if (config == null) {
                LOG.warn("Adding 3D layer plugin to 2D views. Map config not found for view: ", viewId);
                continue;
            }
            JSONArray plugins = config.optJSONArray("plugins");
            if (plugins == null) {
                LOG.warn("Adding 3D layer plugin to 2D views. Map plugins not found for view: ", viewId);
                continue;
            }
            boolean contains3DLayerPlugin = false;
            for (int i = 0; i < plugins.length(); i++) {
                JSONObject plugin = plugins.optJSONObject(i);
                if (plugin == null) {
                    continue;
                }
                String id = plugin.optString("id");
                if (id != null && id.equals(TILES_3D_LAYER_PLUGIN_ID)) {
                    contains3DLayerPlugin = true;
                    break;
                }
            }
            if (contains3DLayerPlugin) {
                continue;
            }
            // Add 3D tiles layer plugin
            plugins.put(JSONHelper.createJSONObject("id", TILES_3D_LAYER_PLUGIN_ID));
            mapBundle.setConfig(config.toString());
            viewService.updateBundleSettingsForView(viewId, mapBundle);
        }
    }
}
