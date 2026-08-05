@ECHO OFF
@IF "%__JAVA_HOME%"=="" SET "JAVA_HOME=%JAVA_HOME%"
@IF "%JAVA_HOME%"=="" SET "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
set MAVEN_PROJECTBASEDIR=%~dp0
if "%MAVEN_PROJECTBASEDIR%" == "" set MAVEN_PROJECTBASEDIR=%CD%
set MAVEN_CMD_LINE_ARGS=%*
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
if exist %WRAPPER_JAR% goto run
echo Error: Could not find %WRAPPER_JAR%
exit /b 1
:run
"%JAVA_HOME%\bin\java.exe" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -classpath %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*
