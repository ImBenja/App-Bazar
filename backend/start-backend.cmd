@echo off
chcp 65001 >nul
cls
echo.
echo ════════════════════════════════════════
echo      INICIANDO BACKEND (Puerto 8080)
echo ════════════════════════════════════════
echo.
cd appsupermercado

REM Primero intenta sin clean
echo [INFO] Ejecutando backend...
echo.
call mvnw spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo ════════════════════════════════════════
    echo ERROR: Intenta lo siguiente:
    echo ════════════════════════════════════════
    echo.
    echo 1. Elimina manualmente: appsupermercado\target
    echo 2. Vuelve a ejecutar este script
    echo.
    pause
    exit /b 1
)
pause