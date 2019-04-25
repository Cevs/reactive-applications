$(document).ready(function(){
   $("#registration_username").on('change', function(){
       console.log(this.value);
       $.ajax({
         url: "http://localhost:8080/user/exist",
         type: "GET",
         data:{
            "username":this.value
         },
         async:true,
         success:function (data) {
             console.log(data);
             if(data == true){
                 $("#btn_register").prop("disabled",true);
                 $("#err_username").text("Username already exists!");
                 if($("#err_username").is(":visible") == false){
                     $("#err_username").toggle();
                 }

             }else{
                 $("#btn_register").prop("disabled",false);
                 $("#err_username").toggle();
             }
         }
     });
   });

    $("form").submit(function(e){
        if(!checkInputs()){
            e.preventDefault();
        }
    });
});

function checkInputs(){

    proceed = true;

    username = $("#registration_username").val();
    email = $("#registration_email").val();
    password = $("#registration_password").val();
    rePassword = $("#registration_re_password").val();

    if(username == ""){
        if($("#err_username").is(":visible") == false){
            $("#err_username").toggle();
        }
        $("#err_username").text("Must not be empty!");
        proceed = false;
    }else{
        if($("#err_username").is(":visible") == true){
            $("#err_username").toggle();
        }
    }
    if(email == ""){
        if($("#err_email").is(":visible") == false){
            $("#err_email").toggle();
        }
        $("#err_email").text("Must not be empty!");
        proceed = false;
    }else{
        if($("#err_email").is(":visible") == true){
            $("#err_email").toggle();
        }
    }
    if(password == ""){
        if($("#err_password").is(":visible") == false){
            $("#err_password").toggle();
        }
        $("#err_password").text("Must not be empty!");
        proceed = false;
    }else{
        if($("#err_password").is(":visible") == true){
            $("#err_password").toggle();
        }
    }

    if(rePassword == ""){
        if($("#err_re_password").is(":visible") == false){
            $("#err_re_password").toggle();
        }
        $("#err_re_password").text("Must not be empty!");
        proceed = false;
    }else{
        if($("#err_re_password").is(":visible") == true){
            $("#err_re_password").toggle();
        }
    }

    if(password != rePassword){
        if($("#err_re_password").is(":visible") == false){
            $("#err_re_password").toggle();
        }
        $("#err_re_password").text("Passwords do not match!");
        proceed = false;
    }else{
        if($("#err_re_password").is(":visible") == true){
            $("#err_re_password").toggle();
        }
    }

    return proceed;
}
