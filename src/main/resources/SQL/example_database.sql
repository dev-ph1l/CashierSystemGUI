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
    table_id INT NOT NULL,
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
-- Drinks
INSERT INTO menu_items (name, category, price, purchase_price) VALUES 
('Water 0.3L', 'non-alcoholic drink', 2.00, 1.50),
('Water 0.5L', 'non-alcoholic drink', 2.50, 1.80),
('Sparkling Water 0.3L', 'non-alcoholic drink', 2.50, 1.80),
('Sparkling Water 0.5L', 'non-alcoholic drink', 3.00, 2.00),
('Cola', 'non-alcoholic drink', 2.50, 2.00),
('Sprite', 'non-alcoholic drink', 2.50, 2.00),
('Fanta', 'non-alcoholic drink', 2.50, 2.00),
('Ice Tea Peach', 'non-alcoholic drink', 3.00, 2.20),
('Ice Tea Lemon', 'non-alcoholic drink', 3.00, 2.20),
('Energy Drink', 'non-alcoholic drink', 3.50, 2.50),
('Smoothie', 'non-alcoholic drink', 4.00, 3.00),
('Herbal Tea', 'non-alcoholic drink', 2.00, 1.50),
('Hot Chocolate', 'non-alcoholic drink', 3.50, 2.50),
('Espresso', 'non-alcoholic drink', 2.50, 1.80),
('Cappuccino', 'non-alcoholic drink', 3.50, 2.80),
('Draft Beer', 'alcoholic drink', 4.00, 3.00),
('Wheat Beer', 'alcoholic drink', 4.50, 3.20),
('Aperol Spritz', 'alcoholic drink', 6.00, 4.50),
('Tequila Shot', 'alcoholic drink', 3.00, 2.50),
('Whisky Sour', 'alcoholic drink', 7.00, 5.50),
('Red Wine', 'alcoholic drink', 5.00, 3.80),
('White Wine', 'alcoholic drink', 5.00, 3.80),
('Mojito', 'alcoholic drink', 6.00, 4.50),
('Gin Tonic', 'alcoholic drink', 6.50, 5.00),
('Cosmopolitan', 'Getränk', 6.50, 5.00),
('Caipirinha', 'Getränk', 6.50, 5.00),
('Long Island Iced Tea', 'cocktail', 7.00, 5.50),
('Pina Colada', 'Getränk', 6.50, 5.00);

-- Food
INSERT INTO menu_items (name, category, price, purchase_price) VALUES 
('Burger', 'main food', 8.00, 5.00),
('Burger with Fries', 'main food', 10.00, 6.50),
('Chicken Wings', 'main food', 7.00, 4.50),
('Nachos with Cheese', 'main food', 5.00, 3.00),
('Caesar Salad', 'main food', 6.00, 4.00),
('Mini Pizzas', 'snack', 7.50, 5.00),
('Club Sandwich', 'snack', 6.50, 4.00),
('Cheese Platter', 'snack', 8.00, 5.50),
('Peanuts', 'snack', 2.50, 1.80),
('Pretzel Sticks', 'snack', 3.00, 2.00),
('Chips', 'snack', 2.50, 1.80);


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

CREATE TABLE account (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    account_type VARCHAR(50) NOT NULL
);

ALTER TABLE orders ADD COLUMN waiter_id INT NOT NULL;

-- Set up a foreign key relationship
ALTER TABLE orders ADD CONSTRAINT fk_waiter FOREIGN KEY (waiter_id) REFERENCES account(id);

INSERT INTO waiters (username, password) VALUES 
('Alice', 'password123'), 
('Bob', 'securepass');

INSERT INTO admins (username, password) VALUES
('root', 'root');

INSERT INTO account (username, password, account_type) VALUES
('Alice', 'password123', 'WAITER'), 
('Bob', 'securepass', 'WAITER'),
('root', 'root', 'ADMIN');