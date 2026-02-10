-- Insert test trainers (let database auto-generate IDs)
INSERT INTO trainers (full_name, specialization, email, phone) VALUES
('Jan Kowalski', 'Trening siłowy', 'jan.kowalski@gym.pl', '+48 123 456 789'),
('Anna Nowak', 'Fitness i aerobik', 'anna.nowak@gym.pl', '+48 234 567 890'),
('Piotr Wiśniewski', 'Yoga i stretching', 'piotr.wisniewski@gym.pl', '+48 345 678 901'),
('Maria Kowalczyk', 'CrossFit', 'maria.kowalczyk@gym.pl', '+48 456 789 012'),
('Tomasz Wójcik', 'Boks i sporty walki', 'tomasz.wojcik@gym.pl', '+48 567 890 123');

-- Insert test rooms
INSERT INTO rooms (name, capacity, location) VALUES
('Sala 1 - Fitness', 20, 'Parter'),
('Sala 2 - Siłownia', 30, 'Piętro 1'),
('Sala 3 - Yoga', 15, 'Piętro 1'),
('Sala 4 - CrossFit', 25, 'Parter'),
('Sala 5 - Boks', 12, 'Piętro 2');

-- Insert test membership types
INSERT INTO membership_types (name, description, duration_days, price) VALUES
('Karnet miesięczny', 'Dostęp do siłowni przez 30 dni', 30, 150.00),
('Karnet 3-miesięczny', 'Dostęp do siłowni przez 90 dni', 90, 400.00),
('Karnet roczny', 'Dostęp do siłowni przez 365 dni', 365, 1500.00),
('Karnet studencki', 'Dostęp dla studentów (30 dni)', 30, 100.00),
('Karnet 10 wejść', 'Karnet na 10 wejść (ważny 60 dni)', 60, 200.00);

