// on window load
$(window).on('load', function () {
    //set animation when mouse over search bar
    $( ".search" ).mouseenter(function () {
        $( ".ab" ).css("animation-name","boardAnimation")
    });

    $( ".search input" ).focus(function () {
        $( ".ab" ).css("animation-name","boardAnimation")
    });

    //stop animation when mouse out of search bar, but if input isn't focus
    $( ".search" ).mouseout(function () {
        if(!$( ".search input" ).is(":focus"))
            $( ".ab" ).css("animation-name","")
    });

    $( ".search input" ).blur(function () {
        $( ".ab" ).css("animation-name","")
    });

    //set icon height to width
    var cw = $( ".conversation .icon" ).width();
    $( ".conversation .icon" ).css({'height':cw+'px'});

    //prevent find input form
    $(".search form").on('submit', function (e) {
            e.preventDefault();
    });
});