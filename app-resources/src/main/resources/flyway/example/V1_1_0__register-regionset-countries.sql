INSERT INTO oskari_maplayer(type, url,
                    name, dataprovider_id,
                    locale,
                    attributes, internal, srs_name)
VALUES(
    'statslayer', 'resources://regionsets/ne_110m_admin_0_countries.json',
    'ne_110m_countries', (SELECT MAX(id) FROM oskari_dataprovider),
    '{ "en" : {
        "name":"Countries"
    }}',
    '{
        "statistics" : {
            "featuresUrl":"resources://regionsets/ne_110m_admin_0_countries.json",
            "regionIdTag":"iso_a2",
            "nameIdTag":"name"
        }
    }', true, 'EPSG:3857');


INSERT INTO oskari_maplayer_group(locale, parentid, selectable, order_number)
VALUES(
    '{"fi":{"name":"Tilastointiyksiköt"}, "sv":{"name":"Statistiska enheter"}, "en":{"name":"Statistical units"}}',
    -1,
    true,
    1000000);

INSERT INTO oskari_maplayer_group_link(maplayerid, groupid, order_number)
VALUES (
    (SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = 'ne_110m_countries'),
    (SELECT id FROM oskari_maplayer_group WHERE locale LIKE '%"en":{"name":"Statistical units"}%'),
    1000001
);
