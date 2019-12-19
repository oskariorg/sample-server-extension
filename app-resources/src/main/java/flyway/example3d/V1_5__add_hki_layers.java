package flyway.example3d;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.db.LayerHelper;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V1_5__add_hki_layers implements JdbcMigration {
    private static final Logger LOG = LogFactory.getLogger(V1_5__add_hki_layers.class);
    private String[] layerFiles = {"hki-3d-model.json", "hki-3d-buildings.json"};

    public void migrate(Connection connection) throws Exception {
        for(String file : layerFiles) {
            long id = LayerHelper.setupLayer(file);
            LOG.info("Layer inserted from", file, "with id:", id);
        }
    }
}