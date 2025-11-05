INSERT INTO vehicules (type, capacite_max_kg, capacite_max_m3, etat, date_ajout)
VALUES
    ('CAMION', 8000, 12.5, 'DISPONIBLE', '2025-10-29'),
    ('FOURGON', 2000, 6.0, 'DISPONIBLE', '2025-10-29'),
    ('MOTO', 150, 0.4, 'DISPONIBLE', '2025-10-29'),
    ('VOITURE', 500, 2.5, 'DISPONIBLE', '2025-10-29');


INSERT INTO warehouse (adresse, gps_lat, gps_long, horaire_ouverture, horaire_fermeture)
VALUES ('Fès',  34.0333, -5.0000, '08:00', '18:00');

INSERT INTO delivery (adresse, creneau_pref, gps_lat, gps_lon, poids_kg, volume_m3, status)
VALUES
    ('Rabat', 'MATIN', 34.0209, -6.8416, 120, 1.2, 'PENDING'),
    ('Fès', 'APRES_MIDI', 34.0333, -5.0000, 300, 3.1, 'PENDING'),
    ('Tanger', 'SOIR', 35.7595, -5.8339, 90, 0.9, 'PENDING'),
    ('Marrakech', 'MATIN', 31.6295, -7.9811, 180, 1.6, 'PENDING'),
    ('Casablanca', 'APRES_MIDI', 33.5731, -7.5898, 250, 2.8, 'PENDING');

INSERT INTO delivery (adresse, creneau_pref, gps_lat, gps_lon, poids_kg, volume_m3, status)
VALUES
    ('Agadir', 'MATIN', 30.4278, -9.5981, 140, 1.1, 'PENDING'),
    ('Oujda', 'APRES_MIDI', 34.6814, -1.9086, 220, 1.9, 'PENDING'),
    ('Tetouan', 'SOIR', 35.5785, -5.3684, 95, 0.7, 'PENDING'),
    ('Kenitra', 'MATIN', 34.2610, -6.5802, 160, 1.4, 'PENDING'),
    ('El Jadida', 'APRES_MIDI', 33.2316, -8.5007, 210, 1.8, 'PENDING'),
    ('Safi', 'SOIR', 32.2994, -9.2372, 130, 1.0, 'PENDING'),
    ('Nador', 'MATIN', 35.1681, -2.9335, 110, 0.8, 'PENDING'),
    ('Hoceima', 'APRES_MIDI', 35.2517, -3.9372, 175, 1.3, 'PENDING'),
    ('Laayoune', 'SOIR', 27.1253, -13.1625, 260, 2.2, 'PENDING'),
    ('Dakhla', 'MATIN', 23.6848, -15.9574, 300, 2.5, 'PENDING');

INSERT INTO tour (date, vehicule_id, warehouse_id)
VALUES
    ('2025-10-30', 1, 1),
    ('2025-10-31', 2, 1);

INSERT INTO delivery (adresse, creneau_pref, gps_lat, gps_lon, poids_kg, volume_m3, status, tour_id)
VALUES
    ('Sefrou', 'MATIN', 33.8305, -4.8353, 90, 0.7, 'PENDING', NULL),
    ('Ifrane', 'APRES_MIDI', 33.5225, -5.1080, 120, 1.0, 'PENDING', 1),
    ('Meknes', 'SOIR', 33.8935, -5.5473, 150, 1.2, 'PENDING', NULL),
    ('Azrou', 'MATIN', 33.4366, -5.2210, 80, 0.6, 'PENDING', 2);
