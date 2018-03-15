<?php
//include "connectdb.inc.php";
$resultArray = array();
$today = date("d.m.y");

$conn = mysqli_connect("localhost",$_POST['userdb'],$_POST['passworddb']);
$objDB = mysqli_select_db($conn,"bangna_queue");
mysqli_query($conn,"SET NAMES UTF8");
$sql = "Select * From t_queue Where staff_id = '".$_POST['staff_id']."' and status_queue = '1' Order By queue_id";
if ($result=mysqli_query($conn,$sql)){
	$intNumRows = mysqli_num_rows($result);
	if($intNumRows>0){
		$tmp = array();
		$tmp["queue_id"] = $row["queue_id"];
	    $tmp["staff_id"] = $row["staff_id"];
	    $tmp["queue_date"] = $row["queue_date"];
	    $tmp["row_1"] = $row["row_1"];
	    $tmp["queue_active"] = $row["queue_active"];
	    $tmp["status_queue"] = $row["status_queue"];
	    $tmp["staff_name"] = $row["staff_name"];
	    $tmp["date_begin"] = $row["date_begin"];
	    $tmp["date_finish"] = $row["date_finish"];
	    $tmp["remark"] = "";
		array_push($resultArray,$tmp);
		array_push($resultArray,$tmp);
	}else{
		$tmp = array();
		$tmp["queue_id"] = "0";
		$tmp["row_1"] = "0";
		$tmp["remark"] = "";
		array_push($resultArray,$tmp);
	}
}else{
	$tmp = array();
	$tmp["queue_id"] = "0";
	$tmp["row_1"] = "0";
	$tmp["remark"] = $today;
	array_push($resultArray,$tmp);
}

header('Content-Type: application/json');
echo json_encode($resultArray);
?>