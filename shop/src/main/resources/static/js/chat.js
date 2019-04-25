$(document).ready(function(){

    outboundChatMessage = null;
    inboundChatMessages = null;

    $("#connect").on("click", function(){
       $("#connect").hide();
       $("#username").hide();
       username = $("#username").val();

        outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
            + username);

        outboundChatMessage.onopen = function (event) {
            $("#send").on("click", function(){
               reply = $("#reply");
               if(!isEmpty(reply.val())){
                   console.log("Sending: "+reply.val());
                   outboundChatMessage.send(reply.val());
                   reply.val("");
               }
            });
        }

        inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
            + username);

        inboundChatMessages.onmessage = function (event) {
            console.log("Receiving: "+event.data);
            chatDisplay = $("#chatDisplay");
            if((event.data.indexOf(username)) >= 0){
                $div = "<p class='message'><span class='sender'>"+event.data+"</span></p>"
            }else{
                $div = "<p class='message'><span class='receiver'>"+event.data+"</span></p>"
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

function isEmpty(value) {
    return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}