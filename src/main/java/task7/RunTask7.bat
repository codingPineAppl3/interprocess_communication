@ECHO OFF

if not exist "C:\temp\" ( 
    ECHO Creating Folder temp on C: drive
    mkdir "C:\temp\"
)

javac Player.java
ECHO compiled Player.java
javac Player2.java
ECHO compiled Player2.java

ECHO Start Player 
ECHO Start Player2
start /b java Player.java & java Player2.java
pause