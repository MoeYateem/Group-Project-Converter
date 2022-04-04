<?php

include("database_info.php");

$amount = $_POST["amount"];
$currency = $_POST["currency"];
$rate = $_POST["rate"];

$query = $mysqli->prepare("INSERT INTO Transactions (amount, currency, rate) VALUES (?, ?, ?)");
$query->bind_param("iss", $amount, $currency, $rate);
$query->execute();
$response = [];
$response["status"] = "Mabrouk!";
$json_response = json_encode($response);
echo $json_response;


?>