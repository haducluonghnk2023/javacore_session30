create database userManagement;
use userManagement;
CREATE TABLE Employee (
    employee_id VARCHAR(5) PRIMARY KEY,
    employee_name VARCHAR(150) NOT NULL,
    employee_email VARCHAR(255) UNIQUE NOT NULL,
    employee_phone VARCHAR(15) UNIQUE NOT NULL,
    employee_sex ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    employee_salary_level DOUBLE NOT NULL,
    employee_salary DOUBLE NOT NULL,
    employee_birthday DATE NOT NULL,
    employee_address TEXT NOT NULL,
    employee_status ENUM('ACTIVE', 'INACTIVE', 'ONLEAVE', 'POLICYLEAVE') NOT NULL
);


CREATE TABLE Department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    department_description VARCHAR(255),
    status BOOLEAN NOT NULL
);

CREATE TABLE users (
     username VARCHAR(50) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL
);

insert into users(username, password) values ('admin', 'admin123');

ALTER TABLE Employee
    ADD COLUMN department_id INT,
    ADD CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
            REFERENCES Department(department_id)
            ON DELETE RESTRICT;

-- Insert 10 records into Employee table
INSERT INTO Employee (employee_id, employee_name, employee_email, employee_phone, employee_sex, employee_salary_level, employee_birthday, employee_address, employee_status)
VALUES
('E001', 'John Doe', 'john.doe@example.com', '1234567890', 'MALE', 5000.0, '1990-01-01', '123 Main St', 'ACTIVE'),
('E002', 'Jane Smith', 'jane.smith@example.com', '1234567891', 'FEMALE', 5500.0, '1992-02-02', '456 Elm St', 'ACTIVE'),
('E003', 'Alice Johnson', 'alice.johnson@example.com', '1234567892', 'FEMALE', 6000.0, '1993-03-03', '789 Oak St', 'ONLEAVE'),
('E004', 'Bob Brown', 'bob.brown@example.com', '1234567893', 'MALE', 4500.0, '1994-04-04', '321 Pine St', 'INACTIVE'),
('E005', 'Charlie White', 'charlie.white@example.com', '1234567894', 'MALE', 7000.0, '1995-05-05', '654 Maple St', 'ACTIVE'),
('E006', 'Diana Green', 'diana.green@example.com', '1234567895', 'FEMALE', 4800.0, '1996-06-06', '987 Birch St', 'POLICYLEAVE'),
('E007', 'Eve Black', 'eve.black@example.com', '1234567896', 'FEMALE', 5200.0, '1997-07-07', '159 Cedar St', 'ACTIVE'),
('E008', 'Frank Blue', 'frank.blue@example.com', '1234567897', 'MALE', 5300.0, '1998-08-08', '753 Walnut St', 'ONLEAVE'),
('E009', 'Grace Yellow', 'grace.yellow@example.com', '1234567898', 'FEMALE', 6200.0, '1999-09-09', '852 Spruce St', 'ACTIVE'),
('E010', 'Hank Red', 'hank.red@example.com', '1234567899', 'MALE', 5800.0, '2000-10-10', '951 Ash St', 'INACTIVE');

-- Insert 10 records into Department table
INSERT INTO Department (department_name, department_description, status)
VALUES
('HR', 'Handles human resources', TRUE),
('Finance', 'Manages company finances', TRUE),
('IT', 'Responsible for IT infrastructure', TRUE),
('Marketing', 'Handles marketing campaigns', TRUE),
('Sales', 'Manages sales operations', TRUE),
('Operations', 'Oversees daily operations', TRUE),
('Legal', 'Handles legal matters', TRUE),
('R&D', 'Research and development', TRUE),
('Customer Support', 'Handles customer queries', TRUE),
('Logistics', 'Manages logistics and supply chain', TRUE);



DELIMITER //

CREATE PROCEDURE GetDepartmentPagination (
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE offset_val INT;
    SET offset_val = (p_page - 1) * p_size;

    SELECT department_id, department_name, department_description, status
    FROM department
    LIMIT offset_val, p_size;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE AddDepartment (
    IN p_department_name VARCHAR(100),
    IN p_department_description TEXT,
    IN p_status BOOLEAN
)
BEGIN
    -- Check if department name already exists
    IF EXISTS (
        SELECT 1 FROM department WHERE department_name = p_department_name
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tên phòng ban đã tồn tại!';
    ELSE
        INSERT INTO department (department_name, department_description, status)
        VALUES (p_department_name, p_department_description, p_status);
    END IF;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE UpdateDepartment (
    IN p_department_id INT,
    IN p_department_name VARCHAR(100),
    IN p_department_description TEXT,
    IN p_status BOOLEAN
)
BEGIN
    -- Bạn nên kiểm tra đầu vào NULL trước khi update (tùy yêu cầu business)
    IF p_department_name IS NULL OR LENGTH(TRIM(p_department_name)) = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tên phòng ban không được để trống.';
    END IF;

    UPDATE department
    SET
        department_name = p_department_name,
        department_description = p_department_description,
        status = p_status
    WHERE department_id = p_department_id;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE SafeDeleteDepartment (
    IN p_department_id INT,
    OUT p_success BOOLEAN,
    OUT p_message VARCHAR(255)
)
BEGIN
    DECLARE emp_count INT;
    DECLARE department_exists INT;

    -- Kiểm tra phòng ban có tồn tại không
    SELECT COUNT(*) INTO department_exists
    FROM Department
    WHERE department_id = p_department_id;

    IF department_exists = 0 THEN
        SET p_success = FALSE;
        SET p_message = 'Department does not exist.';
    ELSE
        -- Đếm số lượng nhân viên thuộc phòng ban
        SELECT COUNT(*) INTO emp_count
        FROM Employee
        WHERE department_id = p_department_id;

        IF emp_count > 0 THEN
            SET p_success = FALSE;
            SET p_message = CONCAT('Cannot delete. ', emp_count, ' employees are still assigned to this department.');
        ELSE
            DELETE FROM Department WHERE department_id = p_department_id;
            SET p_success = TRUE;
            SET p_message = 'Department deleted successfully.';
        END IF;
    END IF;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE SearchDepartmentByName (
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT * FROM Department
    WHERE department_name LIKE p_name;
END //

DELIMITER ;


CREATE PROCEDURE GetTotalDepartments()
BEGIN
    SELECT COUNT(*) AS total FROM department;
END;

CALL GetDepartmentPaginationWithTotal(1, 5);



DELIMITER //
CREATE PROCEDURE FindDepartmentById(IN depId INT)
BEGIN
    SELECT * FROM Department WHERE department_id = depId;
END //
DELIMITER ;

-- employee
-- Phân trang danh sách nhân viên
-- 1. Phân trang
DELIMITER //
CREATE PROCEDURE GetEmployeesByPage (
    IN p_page INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE p_offset INT;
    SET p_offset = (p_page - 1) * p_pageSize;

    SELECT employee_id, employee_name, employee_email, employee_phone, employee_sex, employee_salary_level,employee_salary,
           employee_birthday, employee_address, employee_status, department_id
    FROM Employee
    LIMIT p_offset, p_pageSize;
END //
DELIMITER ;

-- 2. Thêm nhân viên nếu phòng ban đang hoạt động
DELIMITER $$

CREATE PROCEDURE AddEmployeeIfDepartmentActive(
    IN p_employee_id VARCHAR(5),
    IN p_employee_name VARCHAR(150),
    IN p_employee_birthday DATE,
    IN p_employee_phone VARCHAR(15),
    IN p_employee_email VARCHAR(255),
    IN p_employee_address TEXT,
    IN p_employee_salary_level DOUBLE,
    IN p_employee_sex ENUM('MALE', 'FEMALE', 'OTHER'),
    IN p_employee_status ENUM('ACTIVE', 'INACTIVE', 'ONLEAVE', 'POLICYLEAVE'),
    IN p_department_id INT,
    IN p_employee_salary DOUBLE,
    OUT p_message VARCHAR(255)
)
BEGIN
    DECLARE dept_count INT;
    DECLARE emp_count INT;

    -- Kiểm tra phòng ban
    SELECT COUNT(*) INTO dept_count
    FROM Department
    WHERE department_id = p_department_id AND status = 1;

    IF dept_count = 0 THEN
        SET p_message = 'Phòng ban không hoạt động. Không thể thêm nhân viên.';
    ELSE
        -- Kiểm tra mã nhân viên đã tồn tại chưa
        SELECT COUNT(*) INTO emp_count
        FROM Employee
        WHERE employee_id = p_employee_id;

        IF emp_count > 0 THEN
            SET p_message = 'Mã nhân viên đã tồn tại. Vui lòng nhập mã khác.';
        ELSE
            -- Thêm nhân viên
            INSERT INTO Employee (
                employee_id, employee_name, employee_birthday, employee_phone,
                employee_email, employee_address, employee_salary_level, employee_sex,
                employee_status, department_id, employee_salary
            )
            VALUES (
                       p_employee_id, p_employee_name, p_employee_birthday, p_employee_phone,
                       p_employee_email, p_employee_address, p_employee_salary_level, p_employee_sex,
                       p_employee_status, p_department_id, p_employee_salary
                   );

            SET p_message = CONCAT('Thêm nhân viên thành công với ID ', p_employee_id);
        END IF;
    END IF;
END $$

DELIMITER ;

-- 3. Cập nhật nhân viên
DELIMITER //
CREATE PROCEDURE UpdateEmployee (
    IN p_id VARCHAR(10),
    IN p_name VARCHAR(100),
    IN p_birthday DATE,
    IN p_phone VARCHAR(15),
    IN p_email VARCHAR(100),
    IN p_address VARCHAR(255),
    IN p_salary DECIMAL(10,2),
    IN p_salary_level DECIMAL(10,2),
    IN p_sex ENUM('MALE', 'FEMALE', 'OTHER'),
    IN p_status ENUM('ACTIVE', 'INACTIVE', 'ONLEAVE', 'POLICYLEAVE')
)
BEGIN
    UPDATE Employee
    SET employee_name = p_name,
        employee_birthday = p_birthday,
        employee_phone = p_phone,
        employee_email = p_email,
        employee_address = p_address,
        employee_salary_level = p_salary_level,
        employee_salary = p_salary,
        employee_sex = p_sex,
        employee_status = p_status
    WHERE employee_id = p_id;
END //
DELIMITER ;



-- 4. Soft delete
DELIMITER //
CREATE PROCEDURE SoftDeleteEmployee (
    IN p_id VARCHAR(5)
)
BEGIN
    UPDATE Employee
    SET employee_status = 'INACTIVE'
    WHERE employee_id = p_id;
END //
DELIMITER ;


-- 5. Tìm theo tên
DELIMITER //
CREATE PROCEDURE SearchEmployeeByName (
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT *
    FROM Employee
    WHERE employee_name LIKE CONCAT('%', p_name, '%');
END //
DELIMITER ;

-- 6. Tìm theo độ tuổi
DELIMITER //
CREATE PROCEDURE SearchEmployeeByAgeRange (
    IN p_min_age INT,
    IN p_max_age INT
)
BEGIN
    SELECT *
    FROM Employee
    WHERE TIMESTAMPDIFF(YEAR, employee_birthday, CURDATE()) BETWEEN p_min_age AND p_max_age;
END //
DELIMITER ;

-- 7. Sắp xếp theo lương giảm dần
DELIMITER //
CREATE PROCEDURE SortEmployeesBySalaryDesc ()
BEGIN
    SELECT * FROM Employee
    ORDER BY employee_salary_level DESC;
END //
DELIMITER ;

-- 8. Sắp xếp theo tên tăng dần
DELIMITER //
CREATE PROCEDURE SortEmployeesByNameAsc ()
BEGIN
    SELECT * FROM Employee
    ORDER BY employee_name ASC;
END //
DELIMITER ;

-- 9. Safe delete (gọi kèm message)
DELIMITER //
CREATE PROCEDURE SafeDeleteEmployee (
    IN p_id INT,
    OUT p_success BOOLEAN,
    OUT p_message VARCHAR(255)
)
BEGIN
    DECLARE emp_exists INT;

    SELECT COUNT(*) INTO emp_exists
    FROM Employee
    WHERE employee_id = p_id;

    IF emp_exists = 0 THEN
        SET p_success = FALSE;
        SET p_message = 'Không tìm thấy nhân viên.';
    ELSE
        DELETE FROM Employee WHERE employee_id = p_id;
        SET p_success = TRUE;
        SET p_message = 'Xóa nhân viên thành công.';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetTotalEmployees()
BEGIN
    SELECT COUNT(*) AS total FROM Employee;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE FindEmployeeById(IN empId VARCHAR(5))
BEGIN
    SELECT
        employee_id,
        employee_name,
        employee_email,
        employee_phone,
        employee_address,
        employee_salary_level,
        employee_salary,
        employee_sex,
        employee_birthday,
        department_id,
        employee_status
    FROM employee
    WHERE employee_id = empId;
END //
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE check_login(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    OUT is_valid BOOLEAN
)
BEGIN
    DECLARE user_count INT;

    SELECT COUNT(*) INTO user_count
    FROM users
    WHERE username = p_username AND password = p_password;

    SET is_valid = (user_count > 0);
END $$

DELIMITER ;
