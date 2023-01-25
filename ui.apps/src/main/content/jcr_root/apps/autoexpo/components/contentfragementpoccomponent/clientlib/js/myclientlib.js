/* This function is to fetch and load the country-list */
$(document).ready(function() {
    $(".states").hide();
    $(".metros").hide();
    $(".state-list-item").empty();
    $(".metro-list-item").empty();
    $("#backtocountries").hide();
    $("#backtostates").hide();
    $.ajax({
        url: "/bin/autoexpo/cfpoc",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data, function(index, value) {
                $('.country-list').append("<li class='country-list-item'><a class='country-list-item-name' href='#' id='" + value.countryCode + "' >" + value.countryName + "</a></li>");
            });
        }
    });

});


/* This function is to list out the state-list to the selected country */
$(document).on('click', '.country-list-item-name', function() {
    var selectedCountryCode = $(this).attr('id');
    $.ajax({
        url: "/bin/autoexpo/cfpoc",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data, function(index, value) {
                if (value.countryCode === selectedCountryCode) {
                    if (value.hasOwnProperty('metroList')) {
                        $('.states').show();
                        $(".metros").hide();
                        $(".country-list-item").empty();
                        $(".metro-list-item").empty();
                        $("#backtocountries").show();
                        $("#backtostates").hide();
                        $("#backtocountries").append("<a href='#' class='backtocountrylist' id='" + selectedCountryCode + "'>Back</a><br/><br/>");
                        $.each(value.metroList, function(key, val) {
                            var cntrystatecode = selectedCountryCode + "-" + val.stateCode;
                            $('.state-list').append("<li class='state-list-item'><a class='state-list-item-name' href='#' id='" + cntrystatecode + "' >" + val.stateName + "</a></li>");
                        });
                    }
                }
            });
        }
    });
});
/* This function is to listout the District-list to the selected state-list */
$(document).on('click', '.state-list-item-name', function() {
    var selectedStateCode = $(this).attr('id');
    const myArray = selectedStateCode.split("-");
    var countryCode = myArray[0];
    var stateCode = myArray[1];
    $.ajax({
        url: "/bin/autoexpo/cfpoc",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data, function(index, value) {
                if (value.countryCode === countryCode) {
                    if (value.hasOwnProperty('metroList')) {
                        $.each(value.metroList, function(key, val) {
                            if ((val.stateCode != "undefined" || val.stateCode != "") && (val.stateCode === stateCode)) {
                                if (val.hasOwnProperty('districtList')) {
                                    $(".countries").hide();
                                    $(".states").hide();
                                    $(".metros").show();
                                    $(".country-list-item").empty();
                                    $(".state-list-item").empty();
									$("#backtocountries").hide();
                                    $("#backtostates").show();
                                    $("#backtostates").append("<a href='#' class='backtostatelist' id='" + selectedStateCode + "'>Back</a><br/><br/>");
                                    $.each(val.districtList, function(key, val) {
                                        var cntrystatecode = countryCode + "-" + stateCode + "-" + val.districtCode;
                                        $('.metro-list').append("<li class='metro-list-item'><a class='metro-list-item-name' href='#' id='" + cntrystatecode + "' >" + val.districtName + "</a></li>");
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    });

});

/* This function is to go back from the District-list to the state-list */
$(document).on('click', '.backtostatelist', function() {
    var selectedCountryCode = $(this).attr('id');
    const myArray = selectedCountryCode.split("-");
    var countryCode = myArray[0];
    var stateCode = myArray[1];
    $.ajax({
        url: "/bin/autoexpo/cfpoc",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data, function(index, value) {
                if (value.countryCode === countryCode) {
                    if (value.hasOwnProperty('metroList')) {
                        $(".countries").hide();
                        $('.states').show();
                        $(".metros").hide();
                        $(".country-list-item").empty();
                        $(".metro-list-item").empty();
                        $("#backtocountries").show();
    					$("#backtostates").hide();
                        $.each(value.metroList, function(key, val) {
                            var cntrystatecode = selectedCountryCode + "-" + val.stateCode;
                            $('.state-list').append("<li class='state-list-item'><a class='state-list-item-name' href='#' id='" + cntrystatecode + "' >" + val.stateName + "</a></li>");
                        });
                    }
                }
            });
        }
    });
});


/* This function is to go back from the state-list to the country-list */
$(document).on('click', '.backtocountrylist', function() {
    $(".countries").show();
    $('.states').hide();
    $(".metros").hide();
    $(".state-list-item").empty();
    $(".metro-list-item").empty();
    $("#backtocountries").hide();
    $("#backtostates").hide();
    $.ajax({
        url: "/bin/autoexpo/cfpoc",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data, function(index, value) {
                $('.country-list').append("<li class='country-list-item'><a class='country-list-item-name' href='#' id='" + value.countryCode + "' >" + value.countryName + "</a></li>");
            });
        }
    });

});