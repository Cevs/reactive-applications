$(document).ready(function(){

    outboundChatMessage = null;
    inboundChatMessages = null;


    if(outboundChatMessage == null || inboundChatMessages == null){
        username = $("#username").val();
        receiver = $("#sellerUsername").text();
        outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
            + username);

        outboundChatMessage.onopen = function (event) {
            $("#send").on("click", function(){
                reply = $("#reply").val();
                wholeReply = "@"+receiver+" "+reply;
                if(!isEmpty(reply)){
                    console.log("Sending: "+wholeReply);
                    outboundChatMessage.send(wholeReply);
                    $("#reply").val("");
                }
            });
        };

        inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
            + username);

        inboundChatMessages.onmessage = function (event) {
            console.log("Receiving: "+event.data);
            chatDisplay = $("#chatDisplay");
            message = event.data;
            sender = message.substr(0,message.indexOf(')')+1);
            content = message.substr(message.indexOf(':')+1);
            console.log("CONTENT: "+content);

            if((event.data.indexOf(username)) >= 0){
                $div = "<p class='message'><span class='sender'>"+content+"</span></p>"
            }else{
                $div = "<p class='message'><span class='receiver'>"+content+"</span></p>"
            }
            chatDisplay.append($div);
        };
        $("#reply").focus();
    }


    $("#minmax").click(function(){
        //startChat();
        if($("#container").is(":visible")){
            $("#container").hide();
            $(".replySection").hide();
            $("#chatHeaderButton").removeClass("fa-times");
            $("#chatHeaderButton").addClass("fa-angle-up");
        }else{
            $("#container").show();
            $(".replySection").show();
            $("#chatHeaderButton").addClass("fa-times");
            $("#chatHeaderButton").removeClass("fa-angle-up");
        }
    });
});

function startChat(){
    if(outboundChatMessage == null || inboundChatMessages == null){
        username = $("#username").val();
        receiver = $("#sellerUsername").text();
        outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
            + username);

        outboundChatMessage.onopen = function (event) {
            $("#send").on("click", function(){
                reply = $("#reply").val();
                wholeReply = "@"+receiver+" "+reply;
                if(!isEmpty(reply)){
                    console.log("Sending: "+wholeReply);
                    outboundChatMessage.send(wholeReply);
                    $("#reply").val("");
                }
            });
        };

        inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
            + username);

        inboundChatMessages.onmessage = function (event) {
            console.log("Receiving: "+event.data);
            chatDisplay = $("#chatDisplay");

            message = event.data;
            content = message.substr(message.indexOf(':')+1);
            console.log("CONTENT: "+content);

            if((event.data.indexOf(username)) >= 0){
                $div = "<p class='message'><span class='sender'>"+content+"</span></p>"
            }else{
                $div = "<p class='message'><span class='receiver'>"+content+"</span></p>"
            }
            chatDisplay.append($div);
        };
        $("#reply").focus();
    }
}

function isEmpty(value) {
    return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}