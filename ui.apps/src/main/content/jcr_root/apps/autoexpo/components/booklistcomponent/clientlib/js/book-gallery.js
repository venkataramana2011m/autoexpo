$(document).ready(function() {      
    $.ajax({
        url: "/bin/autoexpo/booklisting",
        method: "GET",
        success: function(myresponse) {
            var trgtli='';
			
            $.each(myresponse.results,function(key, value){
				trgtli = trgtli + "<div class='card'>";
				trgtli = trgtli + "<div class='image'>";
				trgtli = trgtli + "<img src='https://cdn.pixabay.com/photo/2018/01/09/03/49/the-natural-scenery-3070808_1280.jpg' />";
                trgtli = trgtli + '</div>';
				trgtli = trgtli + "<div class='title'><h4>" + value["display_name"] + "</h4></div>";
				trgtli = trgtli + "<div class='des'><p>You can Add Desccription Here...</p><button>Read More...</button></div>";
                trgtli = trgtli + '</div>';
                
            });
            $('#booklist').html(trgtli);
        },
        failure: function(myresponse) {
            CQ.Notification.notifyFromResponse(myresponse) ;
        }
    });
    
});