(function(){
    var socket = new WebSocket(
        'ws://localhost:8200/topic/comments.new'
    );
    socket.onopen = function (ev) {
        console.log('Connected to chat service');
        console.log(event);
    }

    socket.onmessage = function(event){
        console.log('Recieved ' + event.data + "!");
        var parsedMessage = JSON.parse(event.data);
        var ul = document.getElementById(
            'comments-' + parsedMessage.imageId
        );
        var li = document.createElement("li");
        li.appendChild(
            document.createTextNode(parsedMessage.comment)
        );
        ul.appendChild(li);
    }

    document.querySelectorAll('button.comment')
        .forEach(function (button) {
            button.addEventListener('click', function(){
                var comment = document.getElementById(
                    'comment-' + button.id
                );

                var xhr = new XMLHttpRequest();
                xhr.open('POST', '/comments', true);

                var formData = new FormData();
                formData.append('comment', comment.value);
                formData.append('imageId', button.id);

                xhr.send(formData);

                comment.value = '';
            });
        });


    var newComments = null;
    var outboundChatMessage = null;
    var inboundChatMessages = null;

    document.getElementById('connect')
        .addEventListener('click', function () {
            document.getElementById('connect').style.display = 'none';
            document.getElementById('disconnect').style.display = 'inline';

            usernameInput = document.getElementById('username');

            document.getElementById('chatBox').style.display = 'inline';

             newComments =
                new WebSocket('ws://localhost:8200/topic/comments.new?user='+usernameInput.value);

            // Listen for new chat messages
            newComments.onmessage = function (event) {
                console.log('Received ' + event.data);
                var parsedMesssage = JSON.parse(event.data);
                var ul = document.getElementById('comments-' + parsedMesssage.imageId);
                var li = document.createElement('li');
                li.appendChild(document.createTextNode(parsedMesssage.comment));
                ul.appendChild(li);

            }

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

            if (newComments != null) {
                newComments.close();
            }
            if (outboundChatMessage != null) {
                outboundChatMessage.close();
            }
            if (inboundChatMessages != null) {
                inboundChatMessages.close();
            }
        });

})();