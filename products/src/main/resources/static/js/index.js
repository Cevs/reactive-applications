$(document).ready(function(){
    var socket = new WebSocket(
        'ws://localhost:8200/topic/comments.new'
    );
    socket.onopen = function (ev) {
        console.log('--- Connected to chat service ---');
        console.log(event);
    }

    // Receive new comments from CHAT SERVICE
    // Update comment UI
    socket.onmessage = function(event){
        console.log('Recieved ' + event.data + "!");
        var parsedMessage = JSON.parse(event.data);
        var ul = document.getElementById(
            'comments-' + parsedMessage.productId
        );
        var li = document.createElement("li");
        li.appendChild(
            document.createTextNode(parsedMessage.comment)
        );
        ul.appendChild(li);
    }

    // Update button UI after adding product
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
                formData.append('productId', button.id);
                xhr.send(formData);

                comment.value = '';
            });
        });


    /*------------- Modal -----------------*/

    $("#addNewProduct").on("click", function () {
        $("#modalInsertProduct").modal({backdrop: 'static', keyboard: false}, "show");
    });

    $("#bntSaveProduct").on("click", function () {
        console.log("clicked");
    });

    $("#formAddNewProduct").on("submit", function () {
        event.preventDefault();
        saveNewProduct(this);
    })

    $("#uploadProductImage").change(function () {
       readURL(this);
    });

    $('#btnSelectImageProduct').on('click', function () {
        $('#uploadProductImage').trigger('click');
    });

    $("#btnCancel").on('click', function () {
        $(".modal-body input").val("");
        $('.view-img-product img').attr('src', "/images/placeholder.jpg");
    });

});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.view-img-product img').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function saveNewProduct(event){
    endPoint = window.location + "products/new"
    formData = new FormData(event);
    console.log(formData);

    $.ajax({
        url: endPoint,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            //console.log(response);
        }
    });

    $("#modalInsertProduct").modal("hide");
    $(".modal-body input").val("");
    $('.view-img-product img').attr('src', "/images/placeholder.jpg");
}