$(document).ready(function(){
    $("#navLinkProductAbout").on("click", function () {
        $("#navLinkProductUserReviews").removeClass("active");
        $(this).addClass("active");
        $(".product-review").hide();
        $(".product-about").show();
    });

    $("#navLinkProductUserReviews").on("click", function () {
        $("#navLinkProductAbout").removeClass("active");
        $(this).addClass("active");
        $(".product-review").show();
        $(".product-about").hide();
    });


    var source = new EventSource("http://localhost:8080/advertisement/");
    source.onmessage = function (evt) {
        var dataObj  = evt.data;
        var tupleObject = JSON.parse(dataObj);
        var imageNamePrimary = tupleObject.t1.imageName;
        var imageNameSecondary = tupleObject.t2.imageName;

        $("#advertisementImagePrimary").attr('src',  "/advertisement/" + imageNamePrimary + "/raw");
        $("#advertisementImageSecondary").attr('src',  "/advertisement/" + imageNameSecondary + "/raw");
    }


    var socket = new WebSocket(
        'ws://localhost:8200/topic/reviews.new'
    );
    socket.onopen = function (ev) {
        console.log('--- Connected to chat service ---');
        console.log(event);
    }

    // Receive new comments from CHAT SERVICE
    // Update comment UI
    socket.onmessage = function(event){
        jsonDataObject = event.data;
        reviewData = JSON.parse(jsonDataObject);
        userId = reviewData.userId;
        date = reviewData.date;
        comment = reviewData.comment;
        console.log(comment);

        $.get( "http://localhost:8080/user/"+userId, function( userData ) {

            console.log(userData);
            console.log(userData.imageName);
            var $outerDiv = $("<div id=\"userReviewRow\" class=\"row no-gutters p-1\">");

            var $innerDiv1 =$(
                "<div class='col-md-2  align-self-center'>" +
                    "<img th:src=\"@{\'/products/" + userData.imageName + "/raw\'}\"  class=\'card-img mx-auto p-1\ style=\'height:80%;\'>" +
                    "<p style=\'font-size:12px; margin:0\' th:text=\'"+userData.username+"\'></p>"+
                    "<p class=\'card-text\'><small class=\'text-muted\' style=\'font-size:12px;\' th:text=\'"+date+"\'></small></p>"+
                "</div>");


            var $innerDiv2 =$(
                "<div class=\"col-md-10  align-self-center\">" +
                    " <div class=\"card-body pt-0 pb-0\">" +
                        "<p class=\'card-text\'  style=\'font-size:14px;\' th:text=\'"+reviewData.comment+"\'></p>"+
                    "</div>"+
                "</div>");

            completeDiv = $outerDiv.append($innerDiv1);
            completeDiv.append($innerDiv2);
            $("#userReviewSection").append(completeDiv);
        });

    }

    // Update button UI after adding product
    document.querySelectorAll('.button-review')
        .forEach(function (button) {
            button.addEventListener('click', function(){
                var review = document.getElementById(
                    'review-' + button.id
                );

                var today = new Date();
                var dd = String(today.getDate()).padStart(2,'0');
                var mm = String(today.getMonth()).padStart(2,'0');
                var yyyy = String(today.getFullYear());
                var date = yyyy+"-"+mm+"-"+dd;

                var xhr = new XMLHttpRequest();
                xhr.open('POST', '/reviews', true);

                var formData = new FormData();
                formData.append('userId', 11);
                formData.append('comment', review.value);
                formData.append('productId', button.id);
                formData.append('date', date);
                xhr.send(formData);

                review.value = '';
            });
        });



});