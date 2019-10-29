# Sample server-extension for Oskari

This is a template that can be used as base for extending and customizing oskari-server.

Click the "Use this template" button on the repository to create a copy of the files under your username and start customizing it.

This application can be seen in http://dev.oskari.org.

## Modifying the initial application setup:
 
Initial content and configuration on the database is created with Flyway-scripts under:
 - app-resources/src/main/java/flyway/example
 - app-resources/src/main/resources/flyway/example

These migrations are run in version order and changing them will change the application that is created on an empty database.

You can also rename the "example" folder to something more appropriate for your app.
Note that this line in oskari-ext.properties will be used to find the scripts so rename "example" on it as well:

    db.additional.modules=myplaces, userlayer, example

Layers can be configured in json:

    app-resources/src/main/resources/json/layers

And referenced in app setups like in (the selectedLayers key):

    app-resources/src/main/resources/json/views/geoportal-3857.json

Compile with:

    mvn clean install
    
Replace oskari-map.war under {jetty.home}/webapps/ with the one created under webapp-map/target 

Note! If you modify the order of "views" (app setups) you might need to modify oskari-ext.properties:

    # default view is the first app setup (value is the database id)
    view.default=1

    # publish template is the second app setup
    view.template.publish=2

    # "native" projection to store the myplaces etc user-generated content:
    oskari.native.srs=EPSG:3857

To enable end-user registration configure these (more information at http://oskari.org/documentation/features/usermanagement):

    allow.registration=true
    oskari.email.sender=<sender@domain.com>
    oskari.email.host=<smtp.domain.com>

*Note!* We will be updating existing Flyway-migrations in this repository to match any
 change we see would be an improvement on the template. This is something that you _should NOT_ do on your customization
 since running modified migrations on an existing database will trigger an error. This is only done to keep the template more simple.

# Reporting issues

All Oskari-related issues should be reported here: https://github.com/oskariorg/oskari-docs/issues

## License

This work is dual-licensed under MIT and [EUPL v1.1](https://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
(any language version applies, English version is included in https://github.com/oskariorg/oskari-docs/blob/master/documents/LICENSE-EUPL.pdf).
You can choose between one of them if you use this work.

`SPDX-License-Identifier: MIT OR EUPL-1.1`

Copyright (c) 2014-present National Land Survey of Finland
