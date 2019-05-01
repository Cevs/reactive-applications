$(document).ready(function(){
    $("#navLinkProductAbout").on("click", function () {
        $("#navLinkProductUserReviews").removeClass("active");
        $("#navLinkSellerInfo").removeClass("active");
        $(this).addClass("active");
        $(".product-review").hide();
        $(".seller-info").hide();
        $(".product-about").show();
    });

    $("#navLinkProductUserReviews").on("click", function () {
        $("#navLinkProductAbout").removeClass("active");
        $("#navLinkSellerInfo").removeClass("active");
        $(this).addClass("active");
        $(".seller-info").hide();
        $(".product-about").hide();
        $(".product-review").show();
    });

    $("#navLinkSellerInfo").on("click", function(){
        $("#navLinkProductAbout").removeClass("active");
        $("#navLinkProductUserReviews").removeClass("active");
        $(this).addClass("active");
        $(".product-review").hide();
        $(".product-about").hide();
        $(".seller-info").show();
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
    }

    // Receive new comments from CHAT SERVICE
    // Update comment UI
    socket.onmessage = function(event){
        jsonDataObject = event.data;
        reviewData = JSON.parse(jsonDataObject);
        userId = reviewData.userId;
        date = reviewData.date;
        comment = reviewData.comment;
        $.get( "http://localhost:8080/user/"+userId, function( userData ) {
            img = "";
            if(userData.imageName == ""){
                img = "../images/profileplaceholder.jpg";
            }else{
                img="/user/"+userData.imageName+"/raw";
            }

            var $div =$(
                "<div id=\"userInfo-\" class=\"card mb-3\">"+
                    "<div class=\"row no-gutters p-1\">"+
                        "<div class='col-md-2  align-self-center'>" +
                            "<img src=\""+img+"\"  class=\'card-img mx-auto p-1\ style=\'height:80%;\'>" +
                            "<p style=\'font-size:12px; margin:0\'>"+userData.username+"</p>"+
                            "<p class=\'card-text\'><small class=\'text-muted\' style=\'font-size:12px;\'>"+date+"</small></p>"+
                        "</div>"+
                        "<div class=\"col-md-10  align-self-center\">" +
                            "<div class=\"card-body pt-0 pb-0\">" +
                                "<p class=\'card-text\'  style=\'font-size:14px;\'>"+reviewData.comment+"</p>"+
                            "</div>"+
                        "</div>"+
                    "</div>"+
                "</div>"
            );

            $div.effect("highlight", {}, 2000);
            $("#userReviewSection").append($div);
        });
    }

    $(".button-review").on("click", function(){
        button = $(".button-review");
        review = $("#reviewText").val();
        today = new Date();
        dd = String(today.getDate()).padStart(2,'0');
        mm = String(today.getMonth()).padStart(2,'0');
        yyyy = String(today.getFullYear());
        date = yyyy+"-"+mm+"-"+dd;

        xhr = new XMLHttpRequest();
        xhr.open('POST', '/reviews', true);
        formData = new FormData();
        formData.append('username', username);
        formData.append('comment', review);
        formData.append('productId', button.attr("id"));
        formData.append('date', date);
        xhr.send(formData);
        $("#reviewText").val("");
    });

    $("#btnUpdateProduct").on("click", function(){
        $.ajax({
            async:true,
            type:"GET",
            url:"http://localhost:9090/product/"+this.value,
            success:function (data) {
                console.log(data);
                populateProductModal(data);
                $("#modalInsertProduct").modal({backdrop: 'static', keyboard: false}, "show");
            }
        })
    });

    $("#formUpdateProduct").on("submit", function () {
        event.preventDefault();
        updateProduct(this);
    });

    $('#btnSelectImageProduct').on('click', function () {
        $('#uploadProductImage').trigger('click');
    });

    $("#uploadProductImage").change(function () {
        readURL(this);
    });

    $("#btn-shoppingcart-add").on("click", function () {
        productId = $("#productId").val();
        console.log("PRODUCT ID: "+productId);
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/shopping-cart",
            contentType: false,
            processData: false,
            data: productId,
            success: function () {
                console.log("ok");
            },
        });
    });
});

function populateProductModal(data) {

    $("#formUpdateProduct .modal-body #productId").val(data.id);
    $("#formUpdateProduct .modal-body #productName").val(data.name);
    $("#formUpdateProduct .modal-body #productDescription").val(data.description);
    $("#formUpdateProduct .modal-body #productCategory").val(data.category);
    $("#formUpdateProduct .modal-body #productLocation").val(data.locationName);
    $("#formUpdateProduct .modal-body #productPrice").val(data.price);
    $("#formUpdateProduct .modal-body #productBaseDiscount").val(data.baseDiscount);
    $("#formUpdateProduct .modal-body #productQuantity").val(data.quantity);
    $("#formUpdateProduct .modal-body #productAvailability").val(data.available+"");
    if(data.image != ""){
        $("#formUpdateProduct .modal-body #image").attr("src", "http://localhost:9090/product/" + data.imageName + "/raw");
    }
}

function updateProduct(event){
    formData = new FormData(event);
    endPoint = "http://localhost:9090/product/update";

    $.ajax({
        url: endPoint,
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            location.reload();
        }
    });

    $("#modalInsertProduct").modal("hide");
    $(".modal-body input").val("");
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.view-img-product img').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
