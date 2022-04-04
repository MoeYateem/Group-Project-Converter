<?php

include("database_info.php");

$query = $mysqli->prepare("SELECT * FROM Transactions");
$query->execute();

$array = $query->get_result();

$response = [];

while($c = $array->fetch_assoc()){
    $response[] = $c;
}

$json_response = json_encode($response);
echo $json_response;

?>