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
$identifier = $_POST['identifier']; // username or email
$password = $_POST['password'];

if (empty($identifier) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "Please fill all fields."]);
    exit();
}

// Check if user exists (by username or email)
$sql = "SELECT * FROM users WHERE username = ? OR email = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $identifier, $identifier);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();
    
    // Verify password
    if (password_verify($password, $user['password'])) {
        echo json_encode([
            "status" => "success",
            "message" => "Login successful",
            "user" => [
                "id" => $user['id'],
                "username" => $user['username'],
                "name" => $user['name'],
                "email" => $user['email'],
                // "profile_picture" => $user['profile_picture'],
                // "bio" => $user['bio']
                "profile_picture" => null,
                "bio" => null
            ]
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Invalid credentials."]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "User not found."]);
}

$stmt->close();
$conn->close();
?>
