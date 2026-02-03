
INSERT INTO order_request (user_name, order_date) VALUES ('Jan Kowalski', CURRENT_TIMESTAMP);
INSERT INTO order_request (user_name, order_date) VALUES ('Marek Nowak', CURRENT_TIMESTAMP);
INSERT INTO order_request (user_name, order_date) VALUES ('Anna Wisniewska', CURRENT_TIMESTAMP);

INSERT INTO order_item (tire_id, quantity, tire_data_at_purchase, order_id)
VALUES (1, 4, 'Yokohama Advan Sport V105 225/45R17', 1);

INSERT INTO order_item (tire_id, quantity, tire_data_at_purchase, order_id)
VALUES (2, 2, 'Bridgestone Potenza S001 205/55R16', 2);

INSERT INTO order_item (tire_id, quantity, tire_data_at_purchase, order_id)
VALUES (1, 2, 'Yokohama Advan Sport V105 225/45R17', 3);

INSERT INTO order_item (tire_id, quantity, tire_data_at_purchase, order_id)
VALUES (3, 4, 'Debica Presto 195/65R15', 3);