$(document).ready(function() {
	$.ajax({
        url: "/bin/autoexpo/viewcartlist?trgtPagePath="+ $("#hiddencartvalue").val(),
        method: "GET",
        success: function(myresponse) {
        	$("#mycartitems").append("<div class='product'/>");
                    $.each(myresponse.CartItemList,function(key, value){
                    	$("#mycartitems").append($("<div class='product-image'/>").html("<img src='" + value["productImage"] +"'>"));
                        $("#ajaxResponse").append($("<div class='product-details'/>").html("<div class='product-title'>" + value["productName"] +"</div>"));
                        $("#ajaxResponse").append($("<div class='product-price'/>").html(value["productPrice"] ));
                        $("#ajaxResponse").append($("<div class='product-quantity'/>").html("<input type='number' value='" + parseInt(value["productQuantity"]) +"' min='1'>"));
                        $("#ajaxResponse").append($("<div class='product-removal'/>").html("<button class='remove-product'>Remove</button>"));
        				$("#ajaxResponse").append($("<div class='product-line-price'/>").html(value["productPrice"] ));
                    });  
        },
        failure: function(myresponse) {
            CQ.Notification.notifyFromResponse(myresponse) ;
        }
    });
    
});
