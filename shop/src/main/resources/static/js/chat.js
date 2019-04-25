$(document).ready(function(){

    outboundChatMessage = null;
    inboundChatMessages = null;

    $("#connect").on("click", function(){
       $("#connect").hide();
       $("#username").hide();
       username = $("#username").val();
       console.log(username);

        outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
            + username);

        outboundChatMessage.onopen = function (event) {
            $("#send").on("click", function(){
               reply = $("#reply");
               outboundChatMessage.send(reply.val());
               reply.val("");
            });
        }

        inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
            + username);

        inboundChatMessages.onmessage = function (event) {
            console.log(event.data);
            chatDisplay = $("#chatDisplay");
            if((event.data.indexOf(username)) >= 0){
                $div = "<p class='message sender'>"+event.data+"</p>"
            }else{
                $div = "<p class='message receiver'>"+event.data+"</p>"
            }
            chatDisplay.append($div);
        };

        $("#reply").focus();
    });

    $("#minmax").click(function(){
        console.log($("#container").is(":visible"));
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