# tcpServer
I have created the server using spring boot ,mongodb for persistent layer,lombok and ServerSocket for creating tcp socket.
Used spring boot with spring-data mongodb for simplicity,while whole project is a maven project.
The server socket listens for connections and sends   asynchronously request from client to appropriate  request handler.
I have used ThreadPoolTaskExecutor with pool size of 5 which can be configured and changed as desired.

Difficulties:
Tried to make the code as simplest as at can be, and dedicated a lot of time to the design of the server.
The server is implemented lru cache for caching and decided to use mongodb for the simplicity and readability.

Api:
leftAdd(Gary,sa) --> added from left
rightAdd(Gary,db) --> added from right
get(Gary) --> [sa,db]
set(Gary,[a,b]) --> set successfully
get(Gary) --> [a,b]
getAllKeys(^(.)ary) -->Gary


For running the server you need mongodb,java8 and maven to be installed in your machine.
for mongodb you should have database with name: serverDb ,username:tcpServer and password:tcpServer.
Tcp server persist data in keyValePairs collection.
You can configure the database name,user and password in application.properties.
The mongoDb should be running.
For running the server:
1. mvn clean install
2. go to target directory of the project and run java -jar tcp-1-SNAPSHOT.jar
