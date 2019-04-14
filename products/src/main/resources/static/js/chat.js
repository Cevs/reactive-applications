$(document).ready(function(){

    var outboundChatMessage = null;
    var inboundChatMessages = null;

    document.getElementById('connect')
        .addEventListener('click', function () {
            document.getElementById('connect').style.display = 'none';
            document.getElementById('disconnect').style.display = 'inline';

            usernameInput = document.getElementById('username');

            document.getElementById('chatBox').style.display = 'inline';

            outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new?user='
                + usernameInput.value);

            //Post new chat messages
            outboundChatMessage.onopen = function (event) {
                document.getElementById('chatButton')
                    .addEventListener('click', function(){
                        var chatInput = document.getElementById('chatInput');
                        console.log('Publishing "' + chatInput.value + '"');
                        outboundChatMessage.send(chatInput.value);
                        chatInput = "";
                    });
            }

            inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new?user="
                + usernameInput.value);
            inboundChatMessages.onmessage = function (event) {
                console.log("Recieved " + event.data);
                var chatDisplay = document.getElementById('chatDisplay');
                chatDisplay.value = chatDisplay.value + event.data + "\n";
            };

            usernameInput.value = "";
            document.getElementById('chatInput').focus();
        });

    document.getElementById('disconnect')
        .addEventListener('click', function () {
            document.getElementById('connect').style.display = 'inline';
            document.getElementById('disconnect').style.display = 'none';
            document.getElementById('chatBox').style.display = 'none';

            if (outboundChatMessage != null) {
                outboundChatMessage.close();
            }
            if (inboundChatMessages != null) {
                inboundChatMessages.close();
            }
        });
});