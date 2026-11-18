package flyway.app;

import fi.nls.oskari.domain.map.view.Bundle;
import fi.nls.oskari.domain.map.view.ViewTypes;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.helpers.AppSetupHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds or updates the mainnavigation bundle in default appsetups.
 */
public class V2_4_0__add_mainnavigation_bundle extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        if (!isBundleRegistered(connection)) {
            throw new SQLException("Cannot migrate mainnavigation bundle because it is not registered in oskari_bundle");
        }
        List<Long> appsetupIds = AppSetupHelper.getSetupsForType(connection, ViewTypes.DEFAULT);
        for (Long appsetupId : appsetupIds) {
            addMainNavigationBundle(connection, appsetupId);
        }
    }

    private boolean isBundleRegistered(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM oskari_bundle WHERE name = ?")) {
            statement.setString(1, "mainnavigation");
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }

    private void addMainNavigationBundle(Connection connection, long appsetupId) throws SQLException {
        Bundle mapfullBundle = AppSetupHelper.getAppBundle(connection, appsetupId, "mapfull");
        if (mapfullBundle == null) {
            return;
        }

        List<Integer> seqNos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT seqno FROM oskari_appsetup_bundles WHERE appsetup_id = ? AND seqno > ? ORDER BY seqno DESC")) {
            statement.setLong(1, appsetupId);
            statement.setInt(2, mapfullBundle.getSeqNo());
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    seqNos.add(results.getInt("seqno"));
                }
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE oskari_appsetup_bundles SET seqno = ? WHERE appsetup_id = ? AND seqno = ?")) {
            for (int seqNo : seqNos) {
                statement.setInt(1, seqNo + 1);
                statement.setLong(2, appsetupId);
                statement.setInt(3, seqNo);
                if (statement.executeUpdate() != 1) {
                    throw new SQLException("Expected to update one bundle seqno " + seqNo
                            + " in appsetup " + appsetupId);
                }
            }
        }

        if (!AppSetupHelper.appContainsBundle(connection, appsetupId, "mainnavigation")) {
            AppSetupHelper.addBundleToApp(connection, appsetupId, "mainnavigation");
        }
        Bundle mainNavigationBundle = AppSetupHelper.getAppBundle(connection, appsetupId, "mainnavigation");
        mainNavigationBundle.setSeqNo(mapfullBundle.getSeqNo() + 1);
        AppSetupHelper.updateAppBundle(connection, appsetupId, mainNavigationBundle);
    }
}
