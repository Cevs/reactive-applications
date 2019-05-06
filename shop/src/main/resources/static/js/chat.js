arrayOfExistingChats = [];

$(document).ready(function(){

    outboundChatMessage = null;
    inboundChatMessages = null;
    username = $("#logged_username").text()
    if((outboundChatMessage == null || inboundChatMessages == null) && username != ""){
        seller = $("#sellerUsername").text();
        if(seller != '' || seller == 'undefined'){
            arrayOfExistingChats.push(seller);
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
                if(username != fromUser && (jQuery.inArray(fromUser, arrayOfExistingChats) == -1)){
                    arrayOfExistingChats.push(fromUser); //Push chat in chatArray

                    numberOfChatBoxes = arrayOfExistingChats.length;

                    positionRight = 20 + (numberOfChatBoxes-1)*320
                    //Create chat box
                    var $chatBox =  $(
                        "<div id=\"chatwindow-"+fromUser+"\"class=\"chatwindow\" style='right:"+positionRight+"px'>"+
                        "<div id=\"chatheader-"+fromUser+"\" class=\"chatheader\">"+
                        "<div id=\"close-"+fromUser+"\" class=\"close-btn\"><i id=\"chatHeaderButton-"+fromUser+"\" class=\"far fa-times-circle\"></i></div>"+
                        "<div class=\"title\"><span>"+fromUser+"</span></div>"+
                        "</div>"+
                        "<div id=\"container-"+fromUser+"\" class=\"container-chat\">"+
                        "<div id=\"chatDisplay-"+fromUser+"\" class=\"chatDisplay conversations\" disabled=\"true\" style=\"overflow-y: auto; height:250px;\"></div>"+
                        "</div>"+
                        "<div id=\"replySection-"+fromUser+"\" class=\"replySection\">"+
                        "<textarea name=\"reply\" id=\"reply-"+fromUser+"\" cols=\"30\" rows=\"1\" class=\"reply\"></textarea>"+
                        "<button id=\"send-"+fromUser+"\" class=\"btn-send\" >Send</button>"+
                        "</div>"+
                        "</div>"
                    );

                    //Append chatbox
                    $("#chatSection").append($chatBox);

                    //bind functions
                    $("#chatheader-"+fromUser).on("click",function(event){
                        if($("#container-"+fromUser).is(":visible")){
                            $("#container-"+fromUser).hide();
                            $("#replySection-"+fromUser).hide();
                        }else{
                            $("#container-"+fromUser).show();
                            $("#replySection-"+fromUser).show();
                        }
                    });

                    $("#close-"+fromUser).on("click",function(event){
                        user = (event.currentTarget.id).substring(6); //Remove 'close-' to get username
                        pos = $.inArray(user, arrayOfExistingChats);
                        if(pos != -1){
                            $("#chatwindow-"+user).remove();
                            arrayOfExistingChats.splice($.inArray(pos,1));
                        }


                    });

                    $("#send-"+fromUser).on("click", function (event) {
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

                //Sender is user so we update chat for reciever with sender class
                if(username == fromUser){
                    //Do nothing, already updated
                }else if(username == toUser){ //MI smo primatelj poruke
                    chatDisplay = $("#chatDisplay-"+fromUser);
                    $div = "<p class='message'><span class='receive'>"+messageText+"</span></p>"
                    chatDisplay.append($div);
                }
            }

        };//Kraj inbound messagea

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

