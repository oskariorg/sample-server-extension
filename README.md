![alt text](oskari_logo_rgb_horizontal.svg "Oskari logo")

# Template for Oskari-based server-side application

This repository is an template for building an Oskari-based Java web applications that work with the [https://github.com/oskariorg/sample-application] UI.

For customizing your own application, click the "Use this template" button on the repository to create a copy of the files under your username.

For fully functioning web application you will also need a frontend application. A template for creating your own Oskari-based frontend service can be found in [https://github.com/oskariorg/sample-application].

These two combined can be used for creating Oskari-based applications such as featured in [https://dev.oskari.org].

## Setup

Here are the steps to setup the build environment:

1. Make sure you have the command line programs `git`, `mvn` (Maven) and `javac` (Java JDK)
2. Clone the application repository (this one or using the GitHub template to make your own copy): `git clone https://github.com/oskariorg/sample-server-extension.git`
3. Run `mvn clean install` on the `sample-server-extension` folder

Versions:
- `mvn` version 3.6.3 (version shouldn't matter much)
- `git` version shouldn't matter
- `java` version 17 or newer

## Creating your own Oskari application
 
Initial content and configuration on the database is created with Flyway-scripts under these folders:
 - `app-resources/src/main/java/flyway/app`
 - `app-resources/src/main/resources/flyway/app`

There are similar migrations located on the `oskari-server` repository, but these are the ones that you can modify to make the application include the content and functionalities you need. These migrations are run in version order and changing them will change the application that is created on an empty database. To run them again you can just drop the database and restart the application. To change the content afterwards you can add migrations that have larger version numbers. These will be run on server startup.

You can also rename the "app" folder to something more appropriate for your app.
Note that this line in `oskari-ext.properties` will be used to find the migration module scripts so rename `app` on it as well:

```properties
db.additional.modules=myplaces, userlayer, app
```

You can also add a new migration modules if you need to, just remember to use the naming convention for migrations (migration file naming comes from the library flywaydb) and add the module reference that is just the folder after `/flyway/` on the path.

Initial layers can be configured with JSON files like ones on the folder `app-resources/src/main/resources/json/layers`. Layers can also be added later with the admin user interface, but you will need to define layers with JSON if you want to reference them as default layers when defining app setups like in the `app-resources/src/main/resources/json/apps/geoportal-3857.json` file (the selectedLayers reference layer JSON file).

After making the changes:

1. Compile again with `mvn clean install`
2. Replace the war-file under Tomcat (or some other server)
3. Restart the server

The main configuration file of Oskari is the `oskari-ext.properties` (located usually on the servers classpath) and you change what app setup is shown by default by modifying the file (Note! If you modify the order of "views" (app setups) you might need to modify `oskari-ext.properties`):

```properties
# default view is the first app setup (value is the database id)
view.default=1

# publish template is the second app setup
view.template.publish=2

# "native" projection to store the myplaces etc user-generated content:
oskari.native.srs=EPSG:3857
```

To enable end-user registration configure these (more information at https://oskari.org/documentation/docs/latest/7-Operating-instructions#How-to-use-user-authentication):

```properties
allow.registration=true
oskari.email.sender=<sender@domain.com>
oskari.email.host=<smtp.domain.com>
```

*Note!* We will be updating existing Flyway-migrations in this repository to match any
 change we see would be an improvement on the template. This is something that you _should NOT_ do on your customization since running modified migrations on an existing database will result in an error. This is only done to keep the template more simple.

# Reporting issues

All Oskari-related issues should be reported here: https://github.com/oskariorg/oskari-documentation/issues

## License

This work is dual-licensed under MIT and [EUPL v1.1](https://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
(any language version applies, English version is included in https://github.com/oskariorg/oskari-docs/blob/master/documents/LICENSE-EUPL.pdf).
You can choose between one of them if you use this work.

`SPDX-License-Identifier: MIT OR EUPL-1.1`

Copyright (c) 2014-present National Land Survey of Finland
