# reactive-applications

<strong>Settings for project:</strong>

<strong>1.)</strong> MongoDB (https://www.mongodb.com/download-center/community)<br>
<strong>2.)</strong> Erlang (if using windows, needed for RabbitMQ) (http://www.erlang.org/downloads)<br>
<strong>3.)</strong> RabbitMQ (https://www.rabbitmq.com/download.html)<br>
<strong>4.)</strong> Clone Project<br>
<strong>5.)</strong> First start eureka-server. Then start everything else: shop,products,reviews and chat microservise<br>
<strong>6.)</strong> After starting the shop servise, database will be populated with test data <br>
    (you can disable this by removing annotation @Bean from CommandLineRunner method in InitDatabse.class inside shop project.
    If you do this, you will also need to delete content of upload-dir folder inside shop and product project).
