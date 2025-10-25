DELETE FROM room_upgrade_request;
DELETE FROM guest;
DELETE FROM employee;
DELETE FROM room;

INSERT INTO room (number, type, price, status) VALUES
('101', 'Single', 75.00, 'AVAILABLE'),
('102', 'Single', 75.00, 'AVAILABLE'),
('201', 'Double', 120.00, 'AVAILABLE'),
('202', 'Double', 120.00, 'BOOKED'),
('301', 'Suite', 250.00, 'BOOKED');

INSERT INTO employee (name, email, role, password) VALUES
('Admin User', 'admin@hotel.com', 'MANAGER', 'password');

INSERT INTO guest (name, email, password, room_id) VALUES
('Test Guest', 'guest@hotel.com', 'password', (SELECT id FROM room WHERE number = '301'));

