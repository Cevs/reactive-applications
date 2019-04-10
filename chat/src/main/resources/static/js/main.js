(function(){
   var xhr = new XMLHttpRequest();
   xhr.open("GET", "/imagesService", true);
   xhr.onload  = function(event){
     if(xhr.readyState == 4){
         if(xhr.status == 200){
             document.getElementById('images').innerHTML = xhr.responseText;

             //Register a handler for each request
             document.querySelectorAll('button.comment')
                 .forEach(function (button) {
                    button.addEventListener('click', function (e) {
                       e.preventDefault();
                       var comment = document.getElementById('comment-' + button.id);

                       var xhr = new XMLHttpRequest();
                       xhr.open('POST', "/comments", true);

                       var formData = new FormData();
                       formData.append('comment', comment.value);
                       xhr.send(formData);

                       comment.value = "";
                    });
                 });

             document.querySelector("button.delete")
                 .forEach(function (button) {
                    button.addEventListener('click', function(e){
                        e.preventDefault();
                        var xhr = new XMLHttpRequest();
                        xhr.open('DELETE', button.id, true);
                        xhr.withCredentials = true;
                        xhr.send(null);
                    }) ;
                 });

             document.getElementById('upload')
                 .addEventListener('click', function (e) {
                    e.preventDefault();

                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', "/images", true);

                    var fils = document.getElementById('file').files;

                    var formData = new FormData();
                    formData.append('file', files[0], files[0].name);

                    xhr.send(formData);
                 });
         }
     }
   }
   xhr.send(null);

   //Send in new comments via AJAX call
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


    // Listen for new chat messages
    var newComments = new WebSocket('ws://localhost:8080/topic/comments.new');
    newComments.onmessage = function (event) {
        console.log('Received ' + event.data);
        var parsedMesssage = JSON.parse(event.data);
        var ul = document.getElementById('comments-' + parsedMesssage.imageId);
        var li = document.createElement('li');
        li.appendChild(document.createTextNode(parsedMesssage.comment));
        ul.appendChild(li);
    }

    var outboundChatMessage = new WebSocket('ws://localhost:8200/app/chatMessage.new');
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

    var inboundChatMessages = new WebSocket("ws://localhost:8200/topic/chatMessage.new');
    inboundChatMessages.onmessage = function (event) {
        console.log("Recieved " + event.data);
        var chatDisplay = document.getElementById('chatDisplay');
        chatDisplay.value = chatDisplay.value + event.data + "\n";
    };

})();