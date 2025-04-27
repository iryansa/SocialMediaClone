<?php
header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1);
// Connection to database
$conn = new mysqli("localhost", "root", "", "connectme_db"); // change database name

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get data from POST request
$username = $_POST['username'];
$name = $_POST['name'];
$email = $_POST['email'];
$password = $_POST['password']; // raw password

// Validate
if (empty($username) || empty($name) || empty($email) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "Please fill all required fields."]);
    exit();
}

// Hash password
$hashedPassword = password_hash($password, PASSWORD_BCRYPT);

// Insert user
$sql = "INSERT INTO users (username, name, email, password) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssss", $username, $name, $email, $hashedPassword);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "User registered successfully."]);
} else {
    echo json_encode(["status" => "error", "message" => "Registration failed. Maybe username/email already exists."]);
}

$stmt->close();
$conn->close();
?>
