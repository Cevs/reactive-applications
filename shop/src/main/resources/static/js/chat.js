chatWindows = [];
$(document).ready(function(){
    username = $("#logged_username").text();
    retrievedData = localStorage.getItem(username+"chatWindows");

    if(retrievedData != null){
        chatWindows = JSON.parse(retrievedData);
        if(chatWindows.length != 0){
            clearChatWindowsSection();
        }
    }

    if($("#sellerUsername").length){
        insertNewChatWindow($("#sellerUsername").text());
    }

    refreshChatWindows();

    outboundChatMessage = null;
    inboundChatMessages = null;

    $("#btn_logout").on("click",function(){
        localStorage.removeItem(username+"chatWindows");
    });

    if((outboundChatMessage == null || inboundChatMessages == null) && username != ""){
        seller = $("#sellerUsername").text();
        if(seller != '' || seller == 'undefined'){
            insertNewChatWindow(seller);
        }
        outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
            + username);

        outboundChatMessage.onopen = function (event) {
            $("#send-"+seller).on("click", function(){
                reply = $("#reply-"+seller).val();

                jsonMessage = {
                    sender: username,
                    receiver: seller,
                    message: reply
                };

                stringReplay = JSON.stringify(jsonMessage);

                if(!isEmpty(reply)){
                    insertNewChatWindow(seller);
                    outboundChatMessage.send(stringReplay);
                    $("#reply-"+seller).val("");

                    //Update chat display
                    chatDisplaySeller = $("#chatDisplay-"+seller);
                    $div = "<p class='message'><span class='send'>"+reply+"</span></p>"
                    chatDisplaySeller.append($div);
                }
            });
        };

        inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
            + username);

        inboundChatMessages.onmessage = function (event) {
            message = jQuery.parseJSON(event.data);

            if(message.sender != username && message.receiver == username ){

                fromUser = message.sender;
                toUser = message.receiver;
                messageText = message.message;

                /*Check if chat already exists */
                if(username != fromUser && (jQuery.inArray(fromUser, chatWindows) == -1)){
                    insertNewChatWindow(fromUser);
                    refreshChatWindows();
                }

               if(username == toUser){
                    chatDisplay = $("#chatDisplay-"+fromUser);
                    $div = "<p class='message'><span class='receive'>"+messageText+"</span></p>"
                    chatDisplay.append($div);
                }
            }

        };

        $("#reply").focus();

    }


    $("#chatheader-"+seller).click(function(){
        if($("#container-"+seller).is(":visible")){
            $("#container-"+seller).hide();
            $("#replySection-"+seller).hide();
        }else{
            $("#container-"+seller).show();
            $("#replySection-"+seller).show();
        }
    });
});

function isEmpty(value) {
    return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}


function insertNewChatWindow(username){
    if(!chatWindowExists(username)){
        chatWindows.push(username);
        updateLocalStorage();
    }
}

function chatWindowExists(username){
    return chatWindows.includes(username);
}

function removeChatWindow(username){
    if(chatWindowExists(username)){
        for( var i = 0; i < chatWindows.length; i++){
            if ( chatWindows[i] === username) {
                chatWindows.splice(i, 1);
            }
        }
        updateLocalStorage();
        refreshChatWindows();
    }
}

function updateLocalStorage(){;
    localStorage.setItem(username+"chatWindows", JSON.stringify(chatWindows));
}

function clearChatWindowsSection(){
    $("#chatSection").empty();
}

/*
    Load opened chat windows with other users
 */
function refreshChatWindows(){
    clearChatWindowsSection();
    $.ajax({
        url: "http://localhost:8200/chats/"+username,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data:JSON.stringify(chatWindows),
        async:true,
        success: function (dataArr) {
            updateChatWindows(dataArr);
        }
    });
}

function updateChatWindows(dataArr){
    for(var i = 0; i<chatWindows.length; i++){
        var user = chatWindows[i];
        positionRight = 20 + i * 320;
        var $chatBox =  $(
            "<div id=\"chatwindow-"+user+"\"class=\"chatwindow\" style='right:"+positionRight+"px'>"+
            "<div id=\"chatheader-"+user+"\" class=\"chatheader\">"+
            "<div id=\"close-"+user+"\" class=\"close-btn\"><i id=\"chatHeaderButton-"+user+"\" class=\"far fa-times-circle\"></i></div>"+
            "<div class=\"title\"><span>"+user+"</span></div>"+
            "</div>"+
            "<div id=\"container-"+user+"\" class=\"container-chat\">"+
            "<div id=\"chatDisplay-"+user+"\" class=\"chatDisplay conversations\" disabled=\"true\" style=\"overflow-y: auto; height:250px;\"></div>"+
            "</div>"+
            "<div id=\"replySection-"+user+"\" class=\"replySection\">"+
            "<textarea name=\"reply\" id=\"reply-"+user+"\" cols=\"30\" rows=\"1\" class=\"reply\"></textarea>"+
            "<button id=\"send-"+user+"\" class=\"btn-send\" >Send</button>"+
            "</div>"+
            "</div>"
        );

        //Append chatbox
        $("#chatSection").append($chatBox);


        if(dataArr != ""){
            $.each(dataArr, function (key,value) {
                receiver = value.receiver;
                if(receiver == user){
                    msgArray = value.messages;
                    $.each(msgArray, function(key,value){
                        if(value.receiver == receiver){
                            $div = $("<p class='message'><span class='send'>"+value.text+"</span></p>");
                        }else{
                            $div = $("<p class='message'><span class='receive'>"+value.text+"</span></p>");
                        }
                        $("#chatDisplay-"+user).append($div)
                    });
                }
            });
        }


        //bind functions
        $("#chatheader-"+user).on("click",function(event){
            user = (event.currentTarget.id).substring(11); //Remove 'chatheader--' to get username
            if($("#container-"+user).is(":visible")){
                $("#container-"+user).hide();
                $("#replySection-"+user).hide();
            }else{
                $("#container-"+user).show();
                $("#replySection-"+user).show();
            }
        });

        $("#close-"+user).on("click",function(event){
            user = (event.currentTarget.id).substring(6); //Remove 'close-' to get username
            pos = $.inArray(user, chatWindows);
            if(pos != -1){
                $("#chatwindow-"+user).remove();
                removeChatWindow(user);
            }
        });

        $("#send-"+user).on("click", function (event) {
            user = (event.currentTarget.id).substring(5); //Remove 'send-' to get username
            reply = $("#reply-"+user).val();
            jsonMessage = {
                sender: username,
                receiver: user,
                message: reply
            };

            stringReplay = JSON.stringify(jsonMessage);

            if(!isEmpty(reply)){
                outboundChatMessage.send(stringReplay);
                //Update chat display
                chatDisplay = $("#chatDisplay-"+user);
                $div = "<p class='message'><span class='send'>"+reply+"</span></p>"
                chatDisplay.append($div);
                $("#reply-"+user).val("");
            }
        });
    }
}