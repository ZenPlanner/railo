## Railo CFML Engine

Welcome to the Railo CFML Engine repostory

Building from source
--------------------

### 1. Before you get started
Before you start building Railo from source, you are going to need a few things installed on your machine:

1. Eclipse for JEE - this is the easiest Eclipse bundle to work with when building Java projects <http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/heliosr>
1. Java 6 JDK - not just the JRE (because you're going to be compiling Java code) - and not Java 7! Railo requires Java 6 to build correctly!
1. A Git client, any client will do. The demo here will be using the command line client to keep it simple <http://git-scm.com/>. There is also the EGit plugin for Eclipse for you to commit your changes locally and create patch files for submission <http://www.eclipse.org/egit/>
1. A running Railo installation in which to test your new patch file <http://www.getrailo.org/index.cfm/download/>
	- make sure that your railo source contains the compileAdmin.cfm files
	- if not you may want to run the Railo Express version
	- http://www.getrailo.org/index.cfm/download/olderversions/


#### a. This version has a major change to Client Variable storage, to handle the race condition brought about by a long running request followed by a short. Before this patched version the long running request would always overwrite the client variable data storage after the short running request.

	update cf_client_data set expires='', data='',cfid='123-...', timecreated=1437507176047 where cfid='123-...' and name='a' and timecreated <= 1437507176047

	NOTE: that the client variables storage table needs a PRIMARY KEY on cfid,name pair!  
	- you will need to make this change manually, the auto create will not create the PK pair.
	- otherwise you will get lots of duplicate cfid inserts due to the new timecreated check

	The other major change here is that expires is now a bigint instead of a varchar.

	CREATE TABLE [dbo].[cf_client_data](
		[expires] [bigint] NULL,
		[cfid] [uniqueidentifier] NOT NULL,
		[name] [char](3) NOT NULL,
		[data] [text] NOT NULL,
		[timecreated] [bigint] NULL,
	 CONSTRAINT [PK_cf_client_data_1] PRIMARY KEY CLUSTERED 
	(
		[cfid] ASC,
		[name] ASC
	)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
	) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

### 2. Build railo server using the railo-code-builder and docker container
https://github.com/ZenPlanner/railo-code-builder


### 6. Setting your build version
Now that we have the projects imported we need to set the build version for our brand new version of Railo. To do this you need to edit the following file in the Railo-Core project:

	/Railo-Core/src/railo/runtime/Info.ini

At the point of writing, the Info.ini looks like:

	[version]
	number=3.3.5.0157
	level=os
	state=final
	name=zp
	name-explanation=http://
	release-date=2015/07/20 00:00:00	

So for this example, we are going to change the number entry to:

	[version]
	number=3.3.5.0158

As you create new builds of railo you will want to be increase the version number so you can see which patches youh have applied.

### 7. Setting up the Compile server

As part of the build process we need to setup an instance of Railo (as mentioned previously in the requirements). This example uses the railo-3.1.2.001-railo-express-macosx.zip that you can obtain from <http://www.getrailo.org/index.cfm/download/> which is using the Jetty server. You can use the apropiate express version for your OS.

1. Once unzipped, open "/railo-3.1.2.001-railo-express-macosx/contexts/railo.xml" and change:

   	<Set name="resourceBase"><SystemProperty name="jetty.home" default="."/>/webroot/</Set>

   to point to the directory where the Railo-CFML project is, for example:

   	<Set name="resourceBase">/Users/markdrew/Projects/railo/railo-cfml/</Set>
   	Make sure to remove this bit <SystemProperty name="jetty.home" default="."/>  

1. Start the server by either double clicking on start or start.bat files.
1. Log into the Railo Administrator by going to: <http://localhost:8888/railo-context/admin/web.cfm> and set a password
1. Edit /Railo-Core/build.properties to add the URL of our compile script:

   	railo.url=http://localhost:8888/compileAdmin.cfm

### 8. Building Railo
Open up the Railo Source project and right click on "build.xml" and select:

	Run As > Ant Build

This will kick of the process of building your patch file and will take a few seconds, if all goes well you will get something like:

	BUILD SUCCESSFUL
	Total time: 24 seconds

Right-click on the Railo-Core project and select "Refresh", you should now see a new folder called "dist" with file inside called "3.1.2.118.rc" (or whatever the version number you setup in Info.ini). Well done! This is your patch file that you can apply to your server!

### 9. Deploying your new build
Now that you have your patch file, all you have to do is copy it from the "railo/railo-java/railo-core/dist" folder to your current running Railo server, to the folder "your-railo-server/lib/railo-server/patches" and restart the server.

Once you have restarted, if you go to the web or server administrator you should see:

	Version	Railo 3.3.5.0157 final


Well done! you have now built Railo!


### 10. Posting patches and fixes
If you have modified the code and want to submit a patch, you should post your issues at the JIRA bug tracker: <https://jira.jboss.org/browse/RAILO>






###11. Debugging Railo Notes:

https://github.com/getrailo/railo/wiki/Contributing-DevelopingRailo

1. Railo-Debug
   Context-click on the "RunAsJavaApplication.java" file, and select "Run As > Run configurations". Add a new Java Application Configuration (unless you already see one for the RunAsJavaApplication class), and click on the "Arguments" tab. The argument order is as follows:
   port instanceDir webContextDir adminContextDir

   The webContextDir and adminContextDir will default to the Railo defaults of /WEB-INF/railo and /WEB-INF/lib/railo-server respectively.
   Ex:

   8088 /path/to/folder/with/WEB-INF/in/it

   ---note which port Jetty is running on
   -changed to 8090 since Jetty is using 8088!!


2. Eclipse may have memory issues and may hang on any breakpoints, so add java vm arguments
   Eclipse Changes
   Debug Configurations -  Railo-debug> RunAsJavaApplication

   Program arguments:8090 /opt/www/studio.zenplanner.local
   VM arguments:-Xmx1024m   ##NEEDED for breakpoints so eclipse has enough memory to handle all of the railo source without crashing!

   JRE - 1.6 JDK

3. the JDBC driver in MSSQL will try to cache query params. in order to see the full queries make sure you turn this off in the railo-server.xml 	in the datasource section


	DATASOURCE change in railo to see actual queries in MSSQL profiler

	class="net.sourceforge.jtds.jdbc.Driver"
	dsn="jdbc:jtds:sqlserver://{host}:{port}/{database};prepareSQL=0" 

4. if you are running the Railo Express through Jetty from Eclipse and it hangs you will need to kill the process manually.
   OSX
   sudo lsof -i:PORT_JETTY_IS_RUNNING_ON
   sudo kill -9 [process id]

   example:
   sudo lsof -i:8090  
   sudo kill -9 [process id]

   START JETTY
   java -DSTOP.PORT=8887 -DSTOP.KEY=railo -jar -Xms256M  -Xmx512M lib/start.jar

   STOP JETTY from running
   java -DSTOP.PORT=8088 -DSTOP.KEY=stop_jetty -jar lib/start.jar --stop




