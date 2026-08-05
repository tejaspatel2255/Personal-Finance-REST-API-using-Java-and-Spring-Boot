@ECHO OFF
@IF "%__JAVA_HOME%"=="" SET "JAVA_HOME=%JAVA_HOME%"
@IF "%JAVA_HOME%"=="" SET "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
SET "MAVEN_PROJECTBASEDIR=%~dp0"
IF "%MAVEN_PROJECTBASEDIR%" == "" SET "MAVEN_PROJECTBASEDIR=%CD%\"
SET "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
SET "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"
IF EXIST "%WRAPPER_JAR%" GOTO run
ECHO Error: Could not find "%WRAPPER_JAR%"
EXIT /b 1
:run
"%JAVA_HOME%\bin\java.exe" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR:~0,-1%" -classpath "%WRAPPER_JAR%" %WRAPPER_LAUNCHER% %*
