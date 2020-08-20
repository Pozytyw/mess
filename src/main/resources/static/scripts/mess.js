// on window load
$( window ).on('load', function () {
    $( ".search input" ).focus(function () {
        $( ".found" ).css("animation-name","boardAnimation")
    });

    //stop animation when mouse out of search bar, but if input isn't focus
    $( ".search" ).mouseout(function () {
        if(!$( ".search input" ).is(":focus")){
            $( ".found" ).css("animation-name","")
        }
    });

    $( ".search input" ).blur(function () {
        $( ".found" ).css("animation-name","")
    });

    //set icon height to width
    //for all element with .auto_height class
    $( ".auto_height" ).each(function (index){
        var cw = $(this).width();
        $(this).css({'height':cw+'px'});
    })

    //on load hide settings
    $( ".settings" ).hide();

    //prevent find input form
    $(".search form").on('submit', function (e) {
            e.preventDefault();
    });
});

$( window ).resize(function() {
    //set icon height to width
    //for all element with .auto_height class
    $( ".auto_height" ).each(function (index){
        var cw = $(this).width();
        $(this).css({'height':cw+'px'});
    })
});