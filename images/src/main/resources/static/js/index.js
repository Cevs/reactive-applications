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
})();