  $(document).ready(function(){
       $.ajax({
            url: "/bin/autoexpo/productgallery?trgtGalleryPath="+ $("#trgtselectedpath").val(),
            method: "GET",
            success: function(myresponse) {
                var trgtli='<table>';
                trgtli = trgtli + '<tr>';
                $.each(myresponse.DamAssetList,function(key, value){
                   trgtli = trgtli + '<td style="width:100px;"><img src="'+value["path"]+'" style="width:100px; height:100px; border:1px solid #A7D7F9; border-radius:6px;box-shadow: 5px 5px 5px gray;" onclick="fnshowImg(this.src);"/></td>';
           		   document.getElementById("myimg-preview").src=value["path"];
                });
                trgtli = trgtli + '</tr>';
                trgtli = trgtli + '</table>';
                $("#myajaxResponse").html(trgtli);
                console.log("success");
            },
            failure: function(myresponse) {
                CQ.Notification.notifyFromResponse(myresponse) ;
            }
       });
 
    });

    function fnshowImg(trgtsrc){
     document.getElementById("myimg-preview").src = trgtsrc;

    }

    function increaseValue() {
      var value = parseInt(document.getElementById('number').value, 10);
      value = isNaN(value) ? 0 : value;
      value++;
      document.getElementById('number').value = value;
    }

    function decreaseValue() {
      var value = parseInt(document.getElementById('number').value, 10);
      value = isNaN(value) ? 0 : value;
      value < 1 ? value = 1 : '';
      value--;
      document.getElementById('number').value = value;
    }

	function increaseCartValue() {
        console.log('test');
        var value =  parseInt(document.getElementById('mycart').innerHTML, 10);
        value = isNaN(value) ? 0 : value;
        value++;
        if(document.getElementById('addtocart').innerHTML !="Remove from Cart"){
			document.getElementById('addtocart').innerHTML="Remove from Cart";
            document.getElementById('mycart').innerHTML = document.getElementById('number').value;
      		document.getElementById('hiddencartvalue').value=document.getElementById('mycurrpage').value;
        }else{
            document.getElementById('mycart').innerHTML =0;
            document.getElementById('number').value=0;
            document.getElementById('addtocart').innerHTML="Add to Cart";
      		document.getElementById('hiddencartvalue').value="";
        }

    }