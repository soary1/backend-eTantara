@echo off
echo =======================================
echo    eTantara - Build et Run
echo =======================================

echo.
echo 1. Arret des processus Java existants...
taskkill /f /im java.exe 2>nul

echo.
echo 2. Nettoyage du projet...
call mvn clean

echo.
echo 3. Compilation du projet...
call mvn compile

if errorlevel 1 (
    echo Erreur de compilation!
    pause
    exit /b 1
)

echo.
echo 4. Demarrage de l'application...
echo.


timeout /t 2 /nobreak > nul

call mvn spring-boot:run

pause
