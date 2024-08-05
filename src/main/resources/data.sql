INSERT INTO account VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc725e', 'admin', crypt('admin', gen_salt('bf')));
INSERT INTO account VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc725f', 'user', crypt('user', gen_salt('bf')));
INSERT INTO museum VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc726a', 'musée du Louvre', 'Musée du Louvre, 75058 Paris CEDEX 01', '75058', 'Paris', 'Paris', '01 40 20 50 50', 'www.louvre.fr', '48.86079459806906', '2.3378156596817785');
INSERT INTO museum VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc726b', 'musée Chintreuil', '66 rue Maréchal de Lattre-de-Tassigny', '1190', 'Pont-de-Vaux', 'Ain', '03 85 51 45 75', 'www.musee-chintreuil.com/', '46.430076', '4.938125');
INSERT INTO museum VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc726c', 'musée du bois', 'place de la République', '1420', 'Mairie', 'Ain', '04 50 56 21 55', '', '45.959038', '5.831489');
INSERT INTO museum VALUES ('dc739b92-f0a0-47bf-a49e-0f6296dc726d', 'musée Picasso', '5, rue Thorigny', '75003', 'Paris', 'Paris', '01 42 71 25 21', 'https://www.museepicassoparis.fr/', '48.8597', '2.362644');
INSERT INTO visit (id, comment, museum_id, account_id) VALUES ('b32d7e60-2d3b-4fd0-af93-9ffdc3dd41a4', 'Comment 1', 'dc739b92-f0a0-47bf-a49e-0f6296dc726a', 'dc739b92-f0a0-47bf-a49e-0f6296dc725e');
INSERT INTO visit (id, comment, museum_id, account_id) VALUES ('44636ec1-3012-48be-86de-7ee8ded93b50', 'Comment 2', 'dc739b92-f0a0-47bf-a49e-0f6296dc726b', 'dc739b92-f0a0-47bf-a49e-0f6296dc725f');
INSERT INTO visit (id, comment, museum_id, account_id) VALUES ('ef3be41b-4f50-4f38-9cd3-74187ce8a2db', 'Comment 3', 'dc739b92-f0a0-47bf-a49e-0f6296dc726b', 'dc739b92-f0a0-47bf-a49e-0f6296dc725f');
