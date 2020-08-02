// on window load
$(window).on('load', function () {
    $( ".conversations_list .conversation" ).click(function (event){
        showConv(this.id);
    });

    $( ".back_icon " ).click(function (event){
        showConvList();
    });
});