<?php
$conn = mysqli_connect(
  'hostname',
  'username',
  'password',
  'dbname');

$response = array();

$getCards = "SELECT * FROM products ORDER BY purchaseQuantity DESC";
if ($gotCards = mysqli_query($conn, $getCards)) {
    while ($row = mysqli_fetch_assoc($gotCards)) {
        
        array_push($response, array('productName'=>$row['productType'], 'productSupplier'=>$row['productSupplier'], 'productStock'=>$row['productStock']));
    
    }
    
    echo json_encode($response);
}
 ?>
