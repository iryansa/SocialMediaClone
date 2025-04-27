<?php

header('Content-Type: application/json');
error_reporting(E_ALL);
ini_set('display_errors', 1); // Only show critical errors
// Database connection
$conn = new mysqli("localhost", "root", "", "connectme_db");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Handle POST Data
$userId = $_POST['userId'];
$name = $_POST['name'];
$username = $_POST['username'];
$bio = $_POST['bio'];
$profilePicture = null;

if (isset($_FILES['profile_picture'])) {
    $profilePicture = file_get_contents($_FILES['profile_picture']['tmp_name']);
}

// Basic validation
if (empty($userId) || empty($name) || empty($username)) {
    echo json_encode(["status" => "error", "message" => "Please fill all required fields."]);
    exit();
}

// Update Query
if ($profilePicture) {
    $sql = "UPDATE users SET name = ?, username = ?, bio = ?, profile_picture = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssi", $name, $username, $bio, $profilePicture, $userId);
} else {
    $sql = "UPDATE users SET name = ?, username = ?, bio = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssi", $name, $username, $bio, $userId);
}

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Profile updated successfully."]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to update profile."]);
}

$stmt->close();
$conn->close();
?>
