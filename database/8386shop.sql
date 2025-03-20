-- Tạo Database
CREATE DATABASE IF NOT EXISTS 8386shop_new;
USE 8386shop_new;
drop database 8386shop_new;



-- ============= TẠO BẢNG =============

-- Bảng Users: Quản lý thông tin người dùng
CREATE TABLE IF NOT EXISTS Users (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullName VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    avatar VARCHAR(255),
    userCreatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userUpdatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    userStatus ENUM('active', 'suspended') DEFAULT 'active',
    userRole ENUM('admin', 'manager', 'employee', 'customer') DEFAULT 'customer'
);

-- Bảng Brand: Quản lý thương hiệu sản phẩm
CREATE TABLE IF NOT EXISTS Brand (
    BrandID INT AUTO_INCREMENT PRIMARY KEY,
    BrandName VARCHAR(255) NOT NULL UNIQUE,
    Country VARCHAR(100),
    ImageURL VARCHAR(255)  -- Thêm cột ImageURL
);


-- Bảng Product: Quản lý sản phẩm
CREATE TABLE IF NOT EXISTS Product (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    BrandID INT NOT NULL,
    Name VARCHAR(255) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Stock INT NOT NULL,
    Description TEXT,
    ImageURL VARCHAR(255),
    ProductCreatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ProductUpdatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (BrandID) REFERENCES Brand(BrandID) ON DELETE CASCADE
-- productStatus ENUM('available', 'unavailable') DEFAULT 'available'
);

ALTER TABLE Product
ADD COLUMN productStatus ENUM('available', 'unavailable') DEFAULT 'available';

-- Bảng ProductDetail: Chi tiết thuộc tính của sản phẩm
CREATE TABLE IF NOT EXISTS ProductDetail (
    DetailID INT AUTO_INCREMENT PRIMARY KEY,
    ProductID INT NOT NULL,
    AttributeName VARCHAR(255) NOT NULL,
    AttributeValue VARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE
);

-- Bảng Orders: Quản lý đơn hàng
CREATE TABLE IF NOT EXISTS Orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TotalAmount DECIMAL(10, 2) NOT NULL,
    Status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (CustomerID) REFERENCES Users(userID) ON DELETE CASCADE
);

-- Bảng OrderDetail: Chi tiết đơn hàng
CREATE TABLE IF NOT EXISTS OrderDetail (
    OrderDetailID INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE
);

-- Bảng ProductReview: Đánh giá sản phẩm
CREATE TABLE IF NOT EXISTS ProductReview (
    ReviewID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    ProductID INT NOT NULL,
    Rating INT NOT NULL CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    ReviewDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE
);

-- ============= CHÈN DỮ LIỆU =============

-- Dữ liệu cho bảng Users
INSERT INTO Users (username, password, fullName, email, phone, address, avatar, userStatus, userRole) 
VALUES 
("admin", "admin", "ADMIN", "admin@gmail.com", "0123456789", "Đà Nẵng", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "admin"),
("admin1", "admin", "ADMIN1", "admin1@gmail.com", "012345678910", "Đà Nẵng", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "admin"),
("8386", "8386", "8386", "8386@gmail.com", "0142399999", "Đà Nẵng", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "manager"),
("abcxyz", "abcxyz", "ABC XYZ", "abcxyz@gmail.com", "09998887776", "Sài Gòn", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "manager"),
("emp1", "emp1", "Tuấn", "emp1@gmail.com", "011223344", "Hà Nội", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "employee"),
("emp2", "emp2", "Thiện", "emp2@gmail.com", "0112233443", "Hội An", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "employee"),
("cus1", "cus1", "Quang", "cus1@gmail.com", "044556677", "Huế", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "customer"),
("cus2", "cus2", "Ngân", "cus2@gmail.com", "0445566773", "Thành Phố Hồ Chí Minh", "https://img.hoidap247.com/picture/question/20201024/large_1603547313777.jpg?v=0", "active", "customer");

-- Dữ liệu cho bảng Brand
INSERT INTO Brand (BrandName, Country,ImageURL) 
VALUES 
("ASUS", "Taiwan","https://i.pinimg.com/736x/86/e0/50/86e05042b9fae73b0c12517ee5cef558.jpg"),
("ACER", "Taiwan","https://i.pinimg.com/736x/69/47/b9/6947b9b5bef862437e5313aa78442a1d.jpg"),
("MSI", "Taiwan","https://i.pinimg.com/736x/50/37/85/50378573ae6e10c00c0c63eb0edf98c8.jpg"),
("LENOVO", "China","https://i.pinimg.com/736x/28/41/19/284119f1dee57574f8ff5fd308d7a74b.jpg"),
("GIGABYTE", "Taiwan","https://i.pinimg.com/736x/55/f3/42/55f342fcf288825e3e3a9752eb503897.jpg"),
("HP", "USA","https://i.pinimg.com/736x/cd/06/95/cd0695814f175babfbcefe0e1b96fa38.jpg"),
("Dell", "USA","https://i.pinimg.com/736x/ea/04/68/ea0468c87f90885acbdb057accfa7eeb.jpg"),
("Apple", "USA","https://i.pinimg.com/736x/d8/8f/32/d88f3243954f744b4b212ef93581a124.jpg");

-- Dữ liệu cho bảng Product
INSERT INTO Product (BrandID, Name, Price, Stock, Description, ImageURL) 
VALUES 
(1, "Asus ROG Zephyrus G16 2023 GU603VU N3898W", 41790000, 10, "Intel Core i7-13620H, Ram 16GB, SSD 512GB, Màn hình 15.6' FHD+ 165Hz, Hệ điều hành Win11", "https://laptopaz.vn/media/product/2962_12172_asus_rog_zephyrus_g16_gu603__1.jpg"),
(1, "Asus Vivobook S14 OLED S5406 2024", 24990000, 10, "Intel Core Ultra 5 125H, Ram 16GB, SSD 1TB, Màn hình 14' 3k OLED, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3218_asus_vivobook_s_14_oled_2024.jpg"),
(1, "Asus Gaming Vivobook K3605ZU-RP296W", 24790000, 10, "Intel Core i5-12500H, Ram 16GB, SSD 512GB, Màn hình 16' FHD+ 144Hz, VGA RTX 4050 6GB, Hệ điều hành Win10", "https://laptopaz.vn/media/product/3364_asus_gaming_vivobook_k3605.jpg"),



(2, "Acer Predator Helios Neo 2024 PHN16-72-91RF", 33990000, 10, "Intel Core i9-14900HX, Ram 16GB, SSD 1TB, Màn hình 16' 2K+ 240Hz, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3229_acer_predator_helios_neo_16_2024_front_thumbnail.png"),
(2, "Acer Predator Helios 16 2023 PH16-71-74UU", 32890000, 10, "Intel Core i7-13700HX, Ram 16GB, SSD 1TB, Màn hình 16' 2K+ 165Hz, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3110_2782_2764_5.png"),
(2, "Acer Nitro 5 Tiger 2022 AN515-58", 25890000, 20, "Intel Core i7 - 12650H, Ram 16GB, SSD 1TB, Màn hình 15.6' FHD 144Hz, VGA RTX 4060 6GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3121_3081_2978_2687_2321_5.jpg"),

(3, "MSI Bravo 15 B7ED 010VN", 15490000, 10, "AMD R5 7535HSH, Ram 16GB, SSD 512GB, Màn hình 15.6' FHD 144Hz, VGA RX6550M 4GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3039_msi_bravo_15_b7ed_010vn.jpg"),
(3, "MSI GF63 12UCX-841VN", 15690000, 10, "AMD R5 7535HS, Ram 8GB, SSD 512GB, Màn hình 15.6' FHD 144Hz, RTX 2050 4GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3215_3029_laptop_msi_gf63_thin_12ucx_841vn_fd16fbe6.jpg"),
(3, "MSI Cyborg 15 A12VF 267VN", 27690000, 10, "Intel Core i5-12450H, Ram 8GB, SSD 512GB, Màn hình 15.6' FHD 144Hz, VGA RTX 4060 8GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/2976_12429_msi_cyborg_15_a12v__3.jpg"),

(4, "Lenovo ThinkPad P16", 48490000, 10, "Intel Core i7-12850HX, Ram 64GB, SSD 1TB, Màn hình 16' 2K+, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3206_2719_6.png"),
(4, "Lenovo Legion Y9000P 2024", 41890000, 10, "Intel Core i9-14900HX, Ram 16GB, SSD 1TB, Màn hình 16' 2K+ 240Hz, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3146_legion_y9000p_2024.jpg"),
(4, "Lenovo Legion 5 2024 82Y300X0VN", 40690000, 10, "Intel Core i7-14650HX, Ram 16GB, SSD 1TB, Màn hình 16' 2K+ 165Hz, Hệ điều hành Win11", "https://laptopaz.vn/media/product/3369_legion_5_2024.jpg"),

(5, "Gigabyte G5 KE-52VN263SH", 25990000, 10, "Intel Core i5-12500H, Ram 16GB, SSD 1TB, Màn hình 15.6' FHD 140Hz, VGA RTX 3060, Hệ điều hành Win11", "https://laptopaz.vn/media/product/2670_thi___t_k____ch__a_c___t__n__5_.png"),
(5, "Gigabyte G5 MF-E3VN313SH", 25990000, 7, "Intel Core i5-12500H, Ram 16GB, SSD 512GB, Màn hình 15.6' FHD 144Hz, VGA RTX 4060 8GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/2868_giga_g5_12th_1.jpg"),
(5, "Gigabyte G5 MF-E3VN333SH", 25790000, 9, "Intel Core i5-12500H, Ram 16GB, SSD 512GB, Màn hình 15.6' FHD 144Hz, VGA RTX 4060 8GB, Hệ điều hành Win11", "https://laptopaz.vn/media/product/2869_13253_gigabyte_g5_kf_3.jpg"),

(6, "HP Gaming VICTUS 16-r0127TX 8C5P6PA", 29990000, 10, "Intel Core i7-13700H, Ram 16GB, SSD 512GB, RTX 4060 8GB, Màn hình 16.1' FHD+ 144Hz, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/hp_victus_16_r0127tx_8c5n2pa_936ef41418.png"),
(6, "HP Pavilion 15-eg3095TU 8C5L4PA", 16990000, 10, "Intel Core i5-1335U, Ram 8GB, SSD 512GB, Intel Iris Xe Graphics, Màn hình 15.6' FHD, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/2023_9_25_638312550720868529_hp-pavilion-15-eg3095tu-bac-4.jpg"),
(6, "HP Envy x360 14-fc0087TU", 33790000, 10, "Core Ultra 7-155U, Ram 16GB, SSD 1TB, Intel UHD Graphics, Màn hình 14' OLED 120Hz, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/hp_envy_x360_14_blue_ffb8a04fdd.png"),

(7, "Dell Gaming G15 5535 R7H165W11GR4060", 32990000, 10, "AMD Ryzen 7 7840HS, Ram 16GB, SSD 512GB, RTX 4060 8GB, Màn hình 15.6' QHD 165Hz, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/2021_9_17_637674992633725381_dell-gaming-g15-5515-r7-5800h-xam-1.jpg"),
(7, "Dell Inspiron N3520 i5 1235U_N5I5203W1", 16490000, 10, "Core i5 1235U, Ram 16GB, SSD 512GB, Intel Iris Xe Graphics, Màn hình 15.6' LED 120Hz, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/dell_inspiron_15_3520_black_d3c2a2c3c6.png"),
(7, "Dell Vostro 15 V3530", 17990000, 10, "Intel Core i3-1305U, Ram 8GB, SSD 256GB, Intel UHD Graphics, Màn hình 15.6' FHD, Win 11", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/dell_vostro_3530_gray_fd6a71f2fd.png"),

(8, "MacBook Pro 14 2023 M3 Max", 79490000, 10, "Apple M3 Max 14-core CPU, Ram 36GB, SSD 1TB, GPU 30-core, Màn hình 14.2' Liquid Retina XDR, MacOS", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/2023_11_1_638344482098373514_macbook-pro-14-2023-m3-pro-max-den%20(1).jpg"),
(8, "MacBook Pro 16 M3 Pro 2023", 57990000, 10, "Apple M3 Pro 12-core CPU, Ram 18GB, SSD 512GB, GPU 18-core, Màn hình 16.2' Liquid Retina XDR, MacOS", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/2023_11_1_638344451703828926_macbook-pro-16-2023-m3-pro-max-bac%20(1).jpg"),
(8, "MacBook Air 15 M2 2023", 39990000, 10, "Apple M2 8-core CPU, Ram 16GB, SSD 512GB, GPU 10-core, Màn hình 15.3' Liquid Retina, MacOS", "https://cdn2.fptshop.com.vn/unsafe/384x0/filters:quality(100)/2023_6_6_638216429146509645_macbook-air-m2-2023-15-inch-xanh-2.jpg");

	
-- Dữ liệu cho bảng ProductDetail
INSERT INTO ProductDetail (ProductID, AttributeName, AttributeValue)
VALUES
-- Asus Products
(1, "CPU", "Intel Core i7-13620H"),
(1, "RAM", "16GB"),
(1, "Ổ cứng", "SSD 512GB"),
(1, "Màn hình", "15.6' FHD+ 165Hz"),
(1, "Hệ điều hành", "Win11"),

(2, "CPU", "Intel Core Ultra 5 125H"),
(2, "RAM", "16GB"),
(2, "Ổ cứng", "SSD 1TB"),
(2, "Màn hình", "14' 3k OLED"),
(2, "Hệ điều hành", "Win11"),

(3, "CPU", "Intel Core i5-12500H"),
(3, "RAM", "16GB"),
(3, "Ổ cứng", "SSD 512GB"),
(3, "Card đồ họa", "RTX 4050 6GB"),
(3, "Màn hình", "16' FHD+ 144Hz"),

-- Acer Products
(4, "CPU", "Intel Core i9-14900HX"),
(4, "RAM", "16GB"),
(4, "Ổ cứng", "SSD 1TB"),
(4, "Màn hình", "16' 2K+ 240Hz"),
(4, "Hệ điều hành", "Win11"),

(5, "CPU", "Intel Core i7-13700HX"),
(5, "RAM", "16GB"),
(5, "Ổ cứng", "SSD 1TB"),
(5, "Màn hình", "16' 2K+ 165Hz"),
(5, "Hệ điều hành", "Win11"),

(6, "CPU", "Intel Core i7-12650H"),
(6, "RAM", "16GB"),
(6, "Ổ cứng", "SSD 1TB"),
(6, "Card đồ họa", "RTX 4060 6GB"),
(6, "Màn hình", "15.6' FHD 144Hz"),

-- MSI Products
(7, "CPU", "AMD R5 7535HSH"),
(7, "RAM", "16GB"),
(7, "Ổ cứng", "SSD 512GB"),
(7, "Card đồ họa", "RX6550M 4GB"),
(7, "Màn hình", "15.6' FHD 144Hz"),

(8, "CPU", "AMD R5 7535HS"),
(8, "RAM", "8GB"),
(8, "Ổ cứng", "SSD 512GB"),
(8, "Card đồ họa", "RTX 2050 4GB"),
(8, "Màn hình", "15.6' FHD 144Hz"),

(9, "CPU", "Intel Core i5-12450H"),
(9, "RAM", "8GB"),
(9, "Ổ cứng", "SSD 512GB"),
(9, "Card đồ họa", "RTX 4060 8GB"),
(9, "Màn hình", "15.6' FHD 144Hz"),

-- Lenovo Products
(10, "CPU", "Intel Core i7-12850HX"),
(10, "RAM", "64GB"),
(10, "Ổ cứng", "SSD 1TB"),
(10, "Màn hình", "16' 2K+"),
(10, "Hệ điều hành", "Win11"),

(11, "CPU", "Intel Core i9-14900HX"),
(11, "RAM", "16GB"),
(11, "Ổ cứng", "SSD 1TB"),
(11, "Màn hình", "16' 2K+ 240Hz"),
(11, "Hệ điều hành", "Win11"),

(12, "CPU", "Intel Core i7-14650HX"),
(12, "RAM", "16GB"),
(12, "Ổ cứng", "SSD 1TB"),
(12, "Màn hình", "16' 2K+ 165Hz"),
(12, "Hệ điều hành", "Win11"),

-- Gigabyte Products
(13, "CPU", "Intel Core i5-12500H"),
(13, "RAM", "16GB"),
(13, "Ổ cứng", "SSD 1TB"),
(13, "Card đồ họa", "RTX 3060"),
(13, "Màn hình", "15.6' FHD 140Hz"),

(14, "CPU", "Intel Core i5-12500H"),
(14, "RAM", "16GB"),
(14, "Ổ cứng", "SSD 512GB"),
(14, "Card đồ họa", "RTX 4060 8GB"),
(14, "Màn hình", "15.6' FHD 144Hz"),

(15, "CPU", "Intel Core i5-12500H"),
(15, "RAM", "16GB"),
(15, "Ổ cứng", "SSD 512GB"),
(15, "Card đồ họa", "RTX 4060 8GB"),
(15, "Màn hình", "15.6' FHD 144Hz"),

-- HP Products
(16, "CPU", "Intel Core i7-13700H"),
(16, "RAM", "16GB"),
(16, "Ổ cứng", "SSD 512GB"),
(16, "Card đồ họa", "RTX 4060 8GB"),
(16, "Màn hình", "16.1' FHD+ 144Hz"),

(17, "CPU", "Intel Core i5-1335U"),
(17, "RAM", "8GB"),
(17, "Ổ cứng", "SSD 512GB"),
(17, "Card đồ họa", "Intel Iris Xe Graphics"),
(17, "Màn hình", "15.6' FHD"),

(18, "CPU", "Core Ultra 7-155U"),
(18, "RAM", "16GB"),
(18, "Ổ cứng", "SSD 1TB"),
(18, "Card đồ họa", "Intel UHD Graphics"),
(18, "Màn hình", "14' OLED 120Hz"),

-- Dell Products
(19, "CPU", "AMD Ryzen 7 7840HS"),
(19, "RAM", "16GB"),
(19, "Ổ cứng", "SSD 512GB"),
(19, "Card đồ họa", "RTX 4060 8GB"),
(19, "Màn hình", "15.6' QHD 165Hz"),

(20, "CPU", "Core i5 1235U"),
(20, "RAM", "16GB"),
(20, "Ổ cứng", "SSD 512GB"),
(20, "Card đồ họa", "Intel Iris Xe Graphics"),
(20, "Màn hình", "15.6' LED 120Hz"),

(21, "CPU", "Intel Core i3-1305U"),
(21, "RAM", "8GB"),
(21, "Ổ cứng", "SSD 256GB"),
(21, "Card đồ họa", "Intel UHD Graphics"),
(21, "Màn hình", "15.6' FHD"),

-- Apple Products
(22, "CPU", "Apple M3 Max 14-core"),
(22, "RAM", "36GB"),
(22, "Ổ cứng", "SSD 1TB"),
(22, "Card đồ họa", "GPU 30-core"),
(22, "Màn hình", "14.2' Liquid Retina XDR"),

(23, "CPU", "Apple M3 Pro 12-core"),
(23, "RAM", "18GB"),
(23, "Ổ cứng", "SSD 512GB"),
(23, "Card đồ họa", "GPU 18-core"),
(23, "Màn hình", "16.2' Liquid Retina XDR"),

(24, "CPU", "Apple M2 8-core"),
(24, "RAM", "16GB"),
(24, "Ổ cứng", "SSD 512GB"),
(24, "Card đồ họa", "GPU 10-core"),
(24, "Màn hình", "15.3' Liquid Retina");

-- Dữ liệu cho bảng Orders
INSERT INTO Orders (CustomerID, TotalAmount, Status)
VALUES
(1, 41790000, "Completed"),
(2, 24990000, "Pending"),
(3, 33990000, "Processing"),
(4, 15490000, "Completed"),
(5, 48490000, "Pending"),
(6, 25990000, "Completed");

-- Dữ liệu cho bảng OrderDetail
INSERT INTO OrderDetail (OrderID, ProductID, Quantity, UnitPrice)
VALUES
(1, 1, 1, 41790000),
(2, 2, 1, 24990000),
(3, 4, 1, 33990000),
(4, 7, 1, 15490000),
(5, 10, 1, 48490000),
(6, 13, 1, 25990000);

-- Dữ liệu cho bảng ProductReview
INSERT INTO ProductReview (CustomerID, ProductID, Rating, Comment)
VALUES
(7, 1, 5, "Sản phẩm rất tốt, đáng tiền!"),
(8, 3, 4, "Hiệu năng ổn định, nhưng hơi nóng."),
(1, 1, 5, "Laptop gaming tuyệt vời, hiệu năng mạnh mẽ"),
(2, 2, 4, "Màn hình OLED đẹp, pin tốt, nhưng hơi đắt"),
(3, 4, 5, "CPU thế hệ mới, chơi game rất mượt"),
(4, 7, 4, "Giá tốt, phù hợp học tập và làm việc"),
(5, 10, 5, "Cấu hình cao, xử lý đồ họa tuyệt vời"),
(6, 13, 4, "Card đồ họa mạnh, tản nhiệt tốt");

-- Bảng ProductSold: Quản lý thông tin bán hàng
CREATE TABLE IF NOT EXISTS ProductSold (
    soldId INT AUTO_INCREMENT PRIMARY KEY,  -- ID bán hàng tự động tăng
    productId INT NOT NULL,                 -- ID sản phẩm (khóa ngoại)
    quantity INT NOT NULL,                  -- Số lượng sản phẩm bán
    dateSold DATETIME DEFAULT CURRENT_TIMESTAMP,  -- Thời gian bán (mặc định là thời gian hiện tại)
    userId INT NOT NULL,                    -- ID người mua (khóa ngoại)
    FOREIGN KEY (productId) REFERENCES Product(productID),  -- Khóa ngoại tham chiếu bảng Product
    FOREIGN KEY (userId) REFERENCES Users(userID)           -- Khóa ngoại tham chiếu bảng Users
);
ALTER TABLE Orders CHANGE CustomerID userID INT NOT NULL;
ALTER TABLE ProductReview CHANGE CustomerID userID INT NOT NULL;
ALTER TABLE Orders CHANGE COLUMN TotalAmount TotalPrice DECIMAL(10, 2) NOT NULL;
ALTER TABLE Orders CHANGE COLUMN Status orderStatus ENUM('Pending', 'Processing', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending';
ALTER TABLE Orders MODIFY COLUMN TotalPrice DECIMAL(15, 2);
ALTER TABLE OrderDetail CHANGE COLUMN UnitPrice Price DECIMAL(10, 2) NOT NULL;
select * from users;
