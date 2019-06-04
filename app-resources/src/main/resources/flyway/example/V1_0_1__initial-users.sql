-- insert only admin user;
INSERT INTO oskari_users(user_name, first_name, last_name, uuid) VALUES('admin', 'Oskari', 'Admin', 'asdf-asdf-asdf-asdf-asdf');

INSERT INTO oskari_role_oskari_user(user_id, role_id) VALUES((SELECT id FROM oskari_users WHERE user_name = 'admin'), (SELECT id FROM oskari_roles WHERE name = 'User'));
INSERT INTO oskari_role_oskari_user(user_id, role_id) VALUES((SELECT id FROM oskari_users WHERE user_name = 'admin'), (SELECT id FROM oskari_roles WHERE name = 'Admin'));

-- use admin/oskari credentials for admin user (user_name in oskari_users must match login on oskari_jaas_users);
INSERT INTO oskari_jaas_users(login, password) VALUES('admin', '$2a$10$sWFKpoLsaOZ1Jo09b7oOe.B6Ndw8jNvmriO4I2GEcRUl9xVJIn496');

-- For selenium tests
INSERT INTO oskari_users(user_name, first_name, last_name, uuid) VALUES('testirobot', 'Test', 'Robot', 'beep-bee-boop-pip-doo-weep');
INSERT INTO oskari_role_oskari_user(user_id, role_id) VALUES((SELECT id FROM oskari_users WHERE user_name = 'testirobot'), (SELECT id FROM oskari_roles WHERE name = 'User'));
INSERT INTO oskari_jaas_users(login, password) VALUES('testirobot', '$2a$10$0GVF4vyhSqIQyvjLzOHTx.dyf9KkM5xPH2dePGaFkPxNsX2Szu9EC');
