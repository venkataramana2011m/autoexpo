$(document).ready(function() {
      
    $.ajax({
        url: "/bin/autoexpo/brandlisting",
        method: "GET",
        success: function(myresponse) {

            var trgtli='<ul class="mybrand-ul">';			
            $.each(myresponse.DamAssetList,function(key, value){
				trgtli = trgtli + '<li style="width:16.666666666666668%" class="mybrand-li">';
                trgtli = trgtli + '<a class="mybrand-a" title="'+ value["title"] +'" href="'+ value["pagePath"] +'">';
                trgtli = trgtli + '<div class="mybrand-div">';
                trgtli = trgtli + '<div class="mybrand-img-div">';
                trgtli = trgtli + '<img class="mybrand-img" src="' + value["imgPath"]+ '?v=1629973193722&amp;q=75" alt="'+ value["title"] +'" title="'+ value["title"] +'">';
                trgtli = trgtli + '</div>';
                trgtli = trgtli + '</div>';
                trgtli = trgtli + '<div class="mybrand-title">'+ camelCase(value["title"]) +'</div>';
				trgtli = trgtli + '</a>';
                trgtli = trgtli + '</li>';

            });
			trgtli = trgtli + '</ul>';
            $('#brandlist').html(trgtli);
        },
        failure: function(myresponse) {
            CQ.Notification.notifyFromResponse(myresponse) ;
        }
    });
    
});
function camelCase(str) {
			return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index)
			{
				return index == 1 ? word.toLowerCase() : word.toUpperCase();
			}).replace(/\s+/g, '');
		}