
-- add new bundle with content shown in guided tour;
INSERT INTO portti_bundle (name) VALUES ('sample-info');

-- Register application specific bundles (these will be added to oskari-server core migration on 1.56)
INSERT INTO portti_bundle (name) VALUES ('layerlist');
INSERT INTO portti_bundle (name) VALUES ('admin-layereditor');