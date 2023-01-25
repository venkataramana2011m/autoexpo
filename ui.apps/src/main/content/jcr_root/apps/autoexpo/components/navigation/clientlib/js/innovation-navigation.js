$(document).ready(function() {
    var navLinks = document.getElementById("navLinks");
    function showMenu(){
        navLinks.style.right="0";
    }
    function hideMenu(){
        navLinks.style.right="-200px";
    }
    $.ajax({
        url: "/bin/autoexpo/navigation",
        method: "GET",
        success: function(myresponse) {
            var trgtli='<i class="fa fa-times" onclick="hideMenu()"></i>';
			trgtli = trgtli + '<ul>';
            $.each(myresponse.PageList,function(key, value){
                var trgtPageTitle="English";
                var pageTitle;
                if(value["pageTitle"].toLowerCase() !=="new cars" && value["pageTitle"].toLowerCase() !=="order tracking" && value["pageTitle"].toLowerCase() !=="add to cart" ){
                    if(trgtPageTitle.toLowerCase()===value["pageTitle"].toLowerCase() || value["pageTitle"].toLowerCase()==="en"){
                        pageTitle = "Home";
                        trgtli = trgtli + "<li><a href='"+value["pagePath"]+".html'>" + pageTitle.toUpperCase() + "</a></li>";
                    }else if(value["pageTitle"].toLowerCase()==="Rewards and benefits"){
                        trgtli = trgtli + "<li><a href='/content/autoexpo/us/en/rewards-benefits/rewards-and-benefits.html'>" + pageTitle.toUpperCase() + "</a></li>";
                    }
                    else{
                        pageTitle = value["pageTitle"];
                        trgtli = trgtli + "<li><a href='"+value["pagePath"]+".html'>" + pageTitle.toUpperCase() + "</a></li>";
                    }
                }


            });
            trgtli = trgtli + "<li><a href='/content/autoexpo/us/en/order-tracking/tracking-page.html'>ORDER TRACKING</a></li>";
			trgtli = trgtli + "<li><div id='cart' class='d-none'></div><a href='/content/autoexpo/us/en/add-to-cart/cart.html' class='cart position-relative d-inline-flex' aria-label='View your shopping cart'>";
            trgtli = trgtli + "<svg class='V3C5bO' width='14' height='14' viewBox='0 0 16 16' xmlns='http://www.w3.org/2000/svg'><path class='_1bS9ic' d='M15.32 2.405H4.887C3 2.405 2.46.805 2.46.805L2.257.21C2.208.085 2.083 0 1.946 0H.336C.1 0-.064.24.024.46l.644 1.945L3.11 9.767c.047.137.175.23.32.23h8.418l-.493 1.958H3.768l.002.003c-.017 0-.033-.003-.05-.003-1.06 0-1.92.86-1.92 1.92s.86 1.92 1.92 1.92c.99 0 1.805-.75 1.91-1.712l5.55.076c.12.922.91 1.636 1.867 1.636 1.04 0 1.885-.844 1.885-1.885 0-.866-.584-1.593-1.38-1.814l2.423-8.832c.12-.433-.206-.86-.655-.86' fill='#fff'></path></svg>";
            trgtli = trgtli + "<span id='mycart' class='cart-basket d-flex align-items-center justify-content-center'>0</span><input type='hidden' id='hiddencartvalue' /></a></li>";
			trgtli = trgtli + '</ul>';
            $('#navLinks').html(trgtli);
        },
        failure: function(myresponse) {
            CQ.Notification.notifyFromResponse(myresponse) ;
        }
    });

});
