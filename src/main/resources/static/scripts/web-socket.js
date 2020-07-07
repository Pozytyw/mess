var stompClient = null;
var conversationId = null;
var lastSearch = null;

function connect() {
    var socket = new SockJS('/handshake');

    let messageHandler = null;
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
                console.log("unsubscribe");
                messageHandler.unsubscribe();
            }

            //subscribe for updating messages
            messageHandler = stompClient.subscribe('/getter/message/'+token.token, function (message) {
                var message = JSON.parse(message.body);
                addMessage(message.message, message.conversationId, "get");
            });
        });
        stompClient.send('/newMessage/token');

        //subscribe for searching users
        stompClient.subscribe('/user/getter/get_users', function (users){
            var usersList = JSON.parse(users.body);
            showUsers(usersList);

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

function addMessage(message, id, type) {
    var messageArea = $("#messageArea");
    var Id = "#"+id;
    var convArea = messageArea.find( Id );
    convArea.append("<div class='"+type+"'><span>" + message + "</span></div>");//add message to message area
    messageArea.scrollTop(messageArea[0].scrollHeight);//scroll to end
}

function showUsers(usersList){
    let findBox = $( ".search .find" );
    findBox.html("");//delete old find users from findBox

    for(user of usersList){
        //add all users to findBox
        findBox.append(" <div class='user'> <button class='conv_button' onclick='getConversation("+user.id+","+user.conv_id+",\""+user.username+"\")'>" + user.username + "</button></div>");
    }
    findBox.show();
}

function getConversation(user_id, conv_id, username){
    if(conv_id){
        showConv(conv_id);
    }else{
        if(confirm("Create new conversation with \"" + username + "\" ?"))
            stompClient.send("/newMessage/new_conv", {}, user_id);
    }
}

function showConv(conv_id){
    var messageArea = $( "#messageArea" );

    //hidden all conversation except selected conversation
    messageArea.find("div[class='conv']").hide()
    messageArea.find("div[id='" + conv_id + "']").show()

    $( ".desc" ).css("border", "none")//unselect
    $( ".receiver[id='" + conv_id + "']" ).parent().css("border", "solid 2.5px black");//selected
}

// on window load
$(window).on('load', function () {
    var messageArea = $( "#messageArea" );

    //connect to websocket
    connect();

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

    //show find box if has children
    $( "#search" ).click(function (){
        let findBox = $( ".search .find" );
        if(findBox.children().length > 0)
            findBox.show();
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

    //set blur(focus out) event method for search bar
    $( ".search" ).focusout(function (){
        let findBox = $( ".search .find" );
         setTimeout(function () {findBox.hide()}, 100);//create delay to make sure buttons onclick work
    });

    //set click event method for elem with class .receiver. Receiver button on click show conversation witch clicked user
    $( ".desc" ).click(function(event) {
        //set conversationId
        conversationId = $(this).find(".receiver")[0].id;

        $( ".desc" ).css("border", "none")//unselect
        $(this).css("border", "solid 2.5px black");//selected

        showConv(conversationId);

        //scroll message area to end
        messageArea.scrollTop(messageArea[0].scrollHeight);
    });

    //click first receiver button
    $( ".receiver" )[0].click();

    //hide find box
    $( ".search .find" ).hide();

    //prevent find input form
    $(".search form").on('submit', function (e) {
            e.preventDefault();
    });

});