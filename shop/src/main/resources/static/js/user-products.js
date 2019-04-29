$(document).ready(function(){

    selectedCategory = "";
    priceLowerLimit = "";
    priceUpperLimit = "";
    selectedItemLocation = "";
    nameSearch = "";
    owner = "";

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


    $("input[name='category']").change(function(){
        selectedCategory = $("input[name='category']:checked").val();
        fetchProducts();
    });


    $("#searchBar").on('keyup', function(event){
        nameSearch = this.value;
        fetchProducts();
    });


    $("input[name='location']").change(function(){
        selectedItemLocation = $("input[name='location']:checked").val();
        fetchProducts();
    });

    $("#priceLowerLimit").on('change', function(){
        priceLowerLimit = this.value;
        fetchProducts();
    });

    $("#priceUpperLimit").on('change', function(){
        priceUpperLimit = this.value;
        fetchProducts();
    });

});

function fetchProducts(){

    owner = $("#logged_username").text();

    $.ajax({
        url: "http://localhost:9090/products/owner/search",
        type: 'GET',
        data: {
            "owner": owner,
            "productName": nameSearch,
            "productCategory":selectedCategory,
            "productLocation":selectedItemLocation,
            "priceLowerLimit":priceLowerLimit,
            "priceUpperLimit":priceUpperLimit
        },
        async:true,
        success: function (dataArr) {
            $("#productArea").empty();
            $.each(dataArr, function(index, value){
                product = value;
                $div =$(
                    "<div class=\"card m-3\" style=\"width:20rem; height:20rem;\">"+
                    "<a href=\"product/" + product.id +"\" style=\"text-decoration: none;color: inherit; height:100%\">"+
                    "<div class=\"row no-gutters\" style=\"height:100%\">"+
                    "<div class=\"col-md-4 my-auto\">"+
                    "<img src=\"products/" + product.imageName + "/raw\" class=\"card-img p-2\" style=\"height: 100%\">"+
                    "</div>"+
                    "<div class=\"col-md-8\">"+
                    "<div class=\"card-body\" style=\"height: 100%\">"+
                    "<h5 class=\"card-title\">"+product.name+"</h5>"+
                    "<p class=\"card-text\">"+product.description+"</p>"+
                    "<p class=\"card-text\"><strong>$ </strong><strong>"+product.price+"</strong></p>"+
                    "</div>"+
                    "</div>"+
                    "</div>"+
                    "</a>"+
                    "</div>"
                );
                $("#productArea").append($div);
            });
        }
    });
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

function saveNewProduct(event){
    endPoint = "http://localhost:9090/product/new"
    formData = new FormData(event);
    console.log(formData);

    $.ajax({
        url: endPoint,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
        }
    });

    $("#modalInsertProduct").modal("hide");
    $(".modal-body input").val("");
    $('.view-img-product img').attr('src', "/images/placeholder.jpg");
}