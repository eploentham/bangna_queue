<?php
//include "connectdb.inc.php";

//$conn = new ConnectDB();
$resultArray = array();
//$resultArray["area"] = array();
$objConnect = mysql_connect("localhost",$_POST['userdb'],$_POST['passworddb']);
$objDB = mysql_select_db("bangna_queue");
mysql_query("SET NAMES UTF8");
$objQuery = mysql_query("Select * From b_staff Where active = '1' and position_id = '1' Order By sort1");
$intNumRows = mysql_num_rows($objQuery);
while($row = mysql_fetch_array($objQuery)){
	//$arrCol = array();
	$tmp = array();
	$tmp["staff_id"] = $row["staff_id"];
    $tmp["staff_code"] = $row["staff_code"];
    $tmp["staff_fname_t"] = $row["staff_fname_t"];
    $tmp["staff_fname_e"] = $row["staff_fname_e"];
    $tmp["staff_lname_t"] = $row["staff_lname_t"];
    $tmp["staff_lname_e"] = $row["staff_lname_e"];
    $tmp["staff_active"] = $row["staff_active"];
    $tmp["staff_remark"] = $row["staff_remark"];
    $tmp["staff_username"] = $row["staff_username"];
    $tmp["staff_password"] = $row["staff_password"];
    $tmp["priority"] = $row["priority"];
    $tmp["tele"] = $row["tele"];
    $tmp["mobile"] = $row["mobile"];
    $tmp["fax"] = $row["fax"];
    $tmp["email"] = $row["email"];
    $tmp["sort1"] = $row["sort1"];
    $tmp["position_id"] = $row["sort1"];
    $tmp["id_card"] = $row["id_card"];
    $tmp["tax_id"] = $row["tax_id"];
	//for($i=0;$i<$intNumField;$i++)
	//{
	//	$arrCol[mysql_field_name($objQuery,$i)] = $obResult[$i];
	//}
	array_push($resultArray,$tmp);
}
mysql_close($objConnect);
	//$pid = $_POST['order_id'];
	//$name = $_POST['foods_code'];
	//$price = $_POST['price'];
	//$description = $_POST['description'];
	//$conn->getArea();
header('Content-Type: application/json');
echo json_encode($resultArray);
?>