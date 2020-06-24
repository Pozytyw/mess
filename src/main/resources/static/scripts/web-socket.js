var stompClient = null;
var email = null;
var conversationId = null;

function connect() {
    var socket = new SockJS('/handshake');
    stompClient = Stomp.over(socket);
    //off console debug
    stompClient.debug = null;
    //connect and create subscibe
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/getter/token', function (token) {
            var token = JSON.parse(token.body);
            email = token.email;
            stompClient.subscribe('/getter/'+token.token+'/message', function (message) {
                var message = JSON.parse(message.body);
                addMessage(message.message, message.conversationId, "get");
            });
        });
        stompClient.send('/newMessage/token');
    });

}

function sendMessage() {
    var message = $("#message").val();

    if(message == "")//if got message is empty return;
        return;

    $("#message").val("");//remove message from input
    message = {'sender': email,'conversationId' : conversationId,'message': message};//create json object to send

    stompClient.send("/newMessage/toUser", {}, JSON.stringify(message));
    addMessage(message.message, conversationId, "post");
}

function addMessage(message, id, type) {
    var messageArea = $("#messageArea");
    var Id = "#"+id;
    var convArea = messageArea.find( Id );
    convArea.append("<div class='"+type+"'><span>" + message + "</span></div>");//add message to message area
    messageArea.scrollTop(messageArea[0].scrollHeight);//scroll to end
}

// on window load
$(window).on('load', function () {
    var messageArea = $( "#messageArea" );

    //connect to websocket
    connect();

    $( ".receiver" ).css("background-color", "white");

    //set click event method for elem with id #send
    $( "#send" ).click(function() { sendMessage(); });
    //set click event method for elem with class .receiver
    $( ".receiver" ).click(function() {
        //set conversationId
        conversationId = $(this).val();

        $( ".receiver" ).css("background-color", "white")
        $(this).css("background-color", "green");

        //hidden all conversation except selected conversation
        messageArea.find("div[class='conv']").hide()
        messageArea.find("div[id='" + conversationId + "']").show()

        //scroll message area to end
        messageArea.scrollTop(messageArea[0].scrollHeight);
    });

    //click first receiver button
    $( ".receiver" )[0].click();
});