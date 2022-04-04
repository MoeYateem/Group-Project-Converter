<?php
include 'simple_html_dom.php';
$html = file_get_contents('https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP');

$JsonObject=json_decode($html);
$buy=reset($JsonObject);
$buyF=end($buy);
$sell=end($JsonObject);
$sellF=end($sell);
$results=array();
$results["buy_dude"]=(json_encode($buyF[1]));
$results["sell_dude"]=($sellF[1]);
echo json_encode($buyF[1]);
echo(" ");
echo json_encode($sellF[1]);
?>