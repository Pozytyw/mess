// on window load
$(window).on('load', function () {
    //show conversation
    $( ".conversations_list .conversation" ).click(function (event){
        showConv(this.id);
    });

    //show conv list and close conversation_box
    $( ".back_icon " ).click(function (event){
        showConvList();
    });

    //showing settings div on click cog
    $( ".cog" ).click(function (event){
        $( ".settings" ).fadeIn();
        //resize auto_height elem in settings
        $( ".settings .auto_height" ).each(function (index){
            var cw = $(this).width();
            $(this).css({'height':cw+'px'});
        })
    });

    //hide settings div
    $( ".close_settings" ).click(function (event){
        $( ".settings" ).fadeOut();
    });
});