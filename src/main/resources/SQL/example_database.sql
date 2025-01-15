-- Erstellen der Datenbank
CREATE DATABASE datenbank_boniersystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verwenden der Datenbank
USE datenbank_boniersystem;

-- Tabelle für Tische
CREATE TABLE tables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Tabelle für Menüartikel
CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    purchase_price DECIMAL(10, 2) NOT NULL
);

-- Tabelle für Bestellungen
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_id INT NOT NULL,id
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- Tabelle für Artikel in einer Bestellung
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Beispiel-Daten für Tische
INSERT INTO tables (name) VALUES ('Table 1'), ('Table 2'), ('Table 3'),('Table 4'), ('Table 5'), ('Table 6'),('Table 7'), ('Table 8'), ('Table 9'),('Table 10'), ('Table 11');

-- Beispiel-Daten für Menüartikel
INSERT INTO menu_items (name, category, price, purchase_price) VALUES
('Cola', 'Getränk', 2.50, 2.00)

CREATE TABLE waiters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

ALTER TABLE orders ADD COLUMN waiter_id INT NOT NULL;

-- Set up a foreign key relationship
ALTER TABLE orders ADD CONSTRAINT fk_waiter FOREIGN KEY (waiter_id) REFERENCES waiters(id);

INSERT INTO waiters (username, password) VALUES
('Alice', 'password123'),
('Bob', 'securepass');

INSERT INTO admins (username, password) VALUES
('root', 'root');


select * from menu_items;
select * from order_items;

truncate table menu_items;
