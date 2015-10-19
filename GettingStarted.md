Jackrabbit Explorer is a rich web application tool to explore and administer a jackrabbit repository via RMI.




---


# Getting Started #

Jackrabbit server

First thing needed is a Jackrabbit server up and running.

A good way to get started quickly is to download the standalone version of Jackrabbit
http://jackrabbit.apache.org/downloads.html.
This is basically a Jackrabbit instance configured with Tomcat ready to go.

Start the standalone server with
```
java -jar jackrabbit-standalone-2.1.0.jar --port 8082
```
There will be a Jackrabbit start page at http://localhost:8082/ once running.
From there you can click the populate link to add some random binary content to you Jackrabbit repository.


---


Jackrabbit Explorer

To start using Jackrabbit Explorer right away download the latest jackrabbitexplorer.war file and put that in a servlet container such as Tomcat or Jetty. I won't explain that here.

Once your servlet container is running. Point your browser to http://localhost:[port]/jackrabbitexplorer



Configuration

/WEB-INF/web.xml
context params configure the default startup login text boxes and set the admin username and password for the application to use.

e.g.
```
	<context-param>
	  <param-name>rmiUrl</param-name>
	  <param-value>http://localhost:8082/rmi</param-value>
	</context>
```

Icon images associated with node types can be changed in the /WEB\_INF/nodeTypeIcons.properties file.
New icons can be added to /images. In addition custom node types can be added to the properties file.

e.g.
```
rep\:system=icons/operation.png
```

rep:system is is the node type.
icons/operation.png is the image path from jackrabbitexplorer/images/

_Note: the backslash escapes the semi colon in the node type. i.e. rep\:system_