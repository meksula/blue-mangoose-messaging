A network messenger project consist of server side and desktop client.

How to run:
1. First of all build server side application:<br>
``$ ./gradlew build`` <br>
2. Next, go to build/libs and run builded project:<br>
``$ java -jar blue-mangoose-server-0.0.1.jar``<br>
3. When server is running you can use client application:<br>
Go to client project directory and build project with javaFx gradle plugin:<br>
``$ gradle jfxJar``
4. Go to build/jfx/app and type:
``$ java -jar project-jfx.jar <run-arg> <br>
There is two available arguments: `localhost` and `remote`
<br>
If you going to test this app, type `localhost`, then client will make requests to local machine.

<br><hr>
Technologies used:

Server:


Desktop:



