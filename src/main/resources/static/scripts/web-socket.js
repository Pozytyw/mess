var stompClient = null;
var conversationId = null;
var lastSearch = null;

function connect() {
    var socket = new SockJS('/handshake');

    let messageHandler = null;
    let convHandler = null;
    let groupHandler = null;

    stompClient = Stomp.over(socket);

    //off console debug
    stompClient.debug = null;
    //connect and create subscibe
    stompClient.connect({}, function (frame) {

        //subscribe for updating tokens
        stompClient.subscribe('/user/getter/token', function (token) {
            var token = JSON.parse(token.body);

            //unsubscribe already exit message handler
            if(messageHandler != null){
                messageHandler.unsubscribe();
                convHandler.unsubscribe();
                groupHandler.unsubscribe();
            }

            //subscribe for updating messages
            messageHandler = stompClient.subscribe('/getter/message/'+token.token, function (message) {
                var message = JSON.parse(message.body);
                addMessage(message.message, message.conversationId, "get");
            });

            //subscribe for new talk conversation
            convHandler = stompClient.subscribe('/getter/new_talk/'+token.token, function (conversation) {
                var conversation = JSON.parse(conversation.body);
                console.log(conversation.name);
                addConv(conversation.id, conversation.name);
            });

//            //subscribe for add user to group or create new group
//            groupHandler = stompClient.subscribe('/getter/add_group/'+token.token, function (group) {
//                var group = JSON.parse(group.body);
//                var messageArea = $("#messageArea");
//                var convArea = messageArea.find( "#"+group.conv_id );
//                if(convArea.length == 0){
//                    addConv(group.conv_id, group.name);
//                }
//            });
        });
        stompClient.send('/newMessage/token');

        //subscribe for searching users
        stompClient.subscribe('/user/getter/get_users', function (foundList){
            var foundList = JSON.parse(foundList.body);
            showUsers(foundList);

        });
    });

}

function sendMessage() {
    var message = $("#message").val();

    if(message == "")//if got message is empty return;
        return;

    $("#message").val("");//remove message from input
    message = {'sender': "n/a", 'conversationId' : conversationId,'message': message};//create json object to send

    stompClient.send("/newMessage/toUser", {}, JSON.stringify(message));
    addMessage(message.message, conversationId, "post");
}

function showUsers(foundList){
    $(".nav .found").html("");
    for(user of foundList){
        if(user.conv_id)
            $(".nav .found").append("<div class='user' onclick='showConv(" + user.conv_id + ")'><span>"+ user.name +"</span><i class='fa fa-arrow-right' aria-hidden='true'></i></div>");
        else
            $(".nav .found").append("<div class='user' onclick='newConv(" + user.user_id + ")'><span>"+ user.name +"</span><i class='fas fa-plus'></i></div>");
    }
}

function newConv(user_id){
    stompClient.send("/newMessage/new_talk", {}, user_id);
}

function addConv(conv_id, name){
    var div = "";
    div += "<div class='conversation' id='"+conv_id+"'>";
        div += "<div class='icon'>";
            div += "<img src='/images/img.png'/>";
        div += "</div>";
        div += "<div class='name'>";
            div += "<span>" + name + "</span>";
        div += "</div>"
    div += "</div>"
    //append div to conversations list
    $(".conversations_list").append(div);

    //set icon height to width
    var cw = $( ".conversation .icon" ).width();
    $( ".conversation .icon" ).css({'height':cw+'px'});
}

function addMessage(message, id, type) {
    var messageArea = $("#messageArea");
    var convArea = messageArea.find( "#"+id );
    convArea.append("<div class='"+type+"'><span>" + message + "</span></div>");//add message to message area
    messageArea.scrollTop(messageArea[0].scrollHeight);//scroll to end
}

function showConvList(){
    var messageArea = $( "#messageArea" );

    //show conversations list
    $( ".conversations_list" ).show();

    //hide all conv
    messageArea.find(".conv").hide();
    $( ".conversations_box" ).hide();
}

function showConv(conv_id){
    var messageArea = $( "#messageArea" );
    conversationId = conv_id;

    //hide conversations list
    $( ".conversations_list" ).hide();

    //show selected conversation
    $( ".conversations_box" ).show();
    messageArea.find("div[id='" + conv_id + "']").show()

    //scroll message area to end
    messageArea.scrollTop(messageArea[0].scrollHeight);
}

// on window load
$(window).on('load', function () {
    var messageArea = $( "#messageArea" );
    var convList = messageArea.find( ".conv" );

    //connect to websocket
    connect();

    //hide conv box
    convList.hide();
    $( ".conversations_box" ).hide()

    //set click event method for elem with id #send
    $( "#send" ).click(function() {
        sendMessage();
    });

    //send message if enter press on message box
    $( "#message" ).keypress(function(event){
        if( event.which == 13 ){
            sendMessage();
        }
    });

    //search for users, when user pressed enter or min 3 times type character. If user add new character, send new query
    $( "#search" ).keypress(function (event){
        var query = null;

        //if enter
        if( event.which == 13 ){
            if(this.value){
                query = this.value;//set value if != null
            }
        }else if( this.value.length >= 2 ){
            query = this.value + event.key;//add pressed key value to the query
        }

        //if query != null send to server query parameter and query is different from lastSearch
        if(query && query != lastSearch){
            stompClient.send("/newMessage/get_users", {}, query);//send with query parameter
        }
    });
});