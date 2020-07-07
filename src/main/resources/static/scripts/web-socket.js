var stompClient = null;
var conversationId = null;
var lastSearch = null;

function connect() {
    var socket = new SockJS('/handshake');

    let messageHandler = null;
    let convHandler = null;

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
            }

            //subscribe for updating messages
            messageHandler = stompClient.subscribe('/getter/message/'+token.token, function (message) {
                var message = JSON.parse(message.body);
                addMessage(message.message, message.conversationId, "get");
            });

            //subscribe for new conversation
            convHandler = stompClient.subscribe('/getter/new_conv/'+token.token, function (conversation) {
                var conversation = JSON.parse(conversation.body);
                addConv(conversation.id, conversation.name);
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

function addConv(conv_id, conv_name){
    var friendsBar = $( ".friendsBar" );
    var divStr = '<div class="profile"> \n' +
                     '<div class="icon"> \n' +
                        '<svg viewBox="-42 0 512 512.002" xmlns="http://www.w3.org/2000/svg"><path d="m210.351562 246.632812c33.882813 0 63.222657-12.152343 87.195313-36.128906 23.972656-23.972656 36.125-53.304687 36.125-87.191406 0-33.875-12.152344-63.210938-36.128906-87.191406-23.976563-23.96875-53.3125-36.121094-87.191407-36.121094-33.886718 0-63.21875 12.152344-87.191406 36.125s-36.128906 53.308594-36.128906 87.1875c0 33.886719 12.15625 63.222656 36.132812 87.195312 23.976563 23.96875 53.3125 36.125 87.1875 36.125zm0 0"/><path d="m426.128906 393.703125c-.691406-9.976563-2.089844-20.859375-4.148437-32.351563-2.078125-11.578124-4.753907-22.523437-7.957031-32.527343-3.308594-10.339844-7.808594-20.550781-13.371094-30.335938-5.773438-10.15625-12.554688-19-20.164063-26.277343-7.957031-7.613282-17.699219-13.734376-28.964843-18.199219-11.226563-4.441407-23.667969-6.691407-36.976563-6.691407-5.226563 0-10.28125 2.144532-20.042969 8.5-6.007812 3.917969-13.035156 8.449219-20.878906 13.460938-6.707031 4.273438-15.792969 8.277344-27.015625 11.902344-10.949219 3.542968-22.066406 5.339844-33.039063 5.339844-10.972656 0-22.085937-1.796876-33.046874-5.339844-11.210938-3.621094-20.296876-7.625-26.996094-11.898438-7.769532-4.964844-14.800782-9.496094-20.898438-13.46875-9.75-6.355468-14.808594-8.5-20.035156-8.5-13.3125 0-25.75 2.253906-36.972656 6.699219-11.257813 4.457031-21.003906 10.578125-28.96875 18.199219-7.605469 7.28125-14.390625 16.121094-20.15625 26.273437-5.558594 9.785157-10.058594 19.992188-13.371094 30.339844-3.199219 10.003906-5.875 20.945313-7.953125 32.523437-2.058594 11.476563-3.457031 22.363282-4.148437 32.363282-.679688 9.796875-1.023438 19.964844-1.023438 30.234375 0 26.726562 8.496094 48.363281 25.25 64.320312 16.546875 15.746094 38.441406 23.734375 65.066406 23.734375h246.53125c26.625 0 48.511719-7.984375 65.0625-23.734375 16.757813-15.945312 25.253906-37.585937 25.253906-64.324219-.003906-10.316406-.351562-20.492187-1.035156-30.242187zm0 0"/></svg> \n' +
                     '</div> \n' +
                     '<div class="desc"> \n' +
                        '<span class="receiver" id="'+conv_id+'">'+conv_name+'</span> \n' +
                     '</div> \n' +
                 '</div>';
    friendsBar.append(divStr);
    $( ".desc" ).click(function(event) {
        descClick($(this));
    });
}

function descClick(elem){
    var messageArea = $( "#messageArea" );
    //set conversationId
    conversationId = elem.find(".receiver")[0].id;

    $( ".desc" ).css("border", "none")//unselect
    $(event).css("border", "solid 2.5px black");//selected

    showConv(conversationId);

    //scroll message area to end
    messageArea.scrollTop(messageArea[0].scrollHeight);
}

// on window load
$(window).on('load', function () {

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
        descClick($(this));
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