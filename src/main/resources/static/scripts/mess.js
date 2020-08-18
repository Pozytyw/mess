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
    var cw = $( ".conversation .icon" ).width();
    $( ".conversation .icon" ).css({'height':cw+'px'});

    //prevent find input form
    $(".search form").on('submit', function (e) {
            e.preventDefault();
    });
});

$( window ).resize(function() {
    //set icon height to width
    var cw = $( ".conversation .icon" ).width();
    $( ".conversation .icon" ).css({'height':cw+'px'});
}