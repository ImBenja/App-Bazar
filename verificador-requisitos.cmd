@REM ╔════════════════════════════════════════════════════════════════╗
@REM ║                    VERIFICACIÓN DE CONFIGURACIÓN               ║
@REM ║                                                                ║
@REM ║ Este archivo verifica que todo está listo para ejecutar        ║
@REM ║ la aplicación SuperApp                                         ║
@REM ╚════════════════════════════════════════════════════════════════╝

@echo off
chcp 65001 >nul
cls

echo.
echo ════════════════════════════════════════════════════════════════
echo                   ✓ VERIFICACIÓN DE CONFIGURACIÓN
echo ════════════════════════════════════════════════════════════════
echo.

REM Verificar Java
echo [1/3] Verificando Java...
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Java está instalado
) else (
    echo ✗ Java NO está instalado
    echo   → Descarga Java desde: https://www.java.com
    goto error
)

echo.

REM Verificar Python
echo [2/3] Verificando Python...
py --version >nul 2>&1
if %errorlevel% equ 0 (
    for /f "tokens=2" %%i in ('py --version 3^>^&1') do set python_version=%%i
    echo ✓ Python está instalado ^(versión: %python_version%^)
) else (
    echo ✗ Python NO está instalado
    echo   → Descarga Python desde: https://www.python.org
    goto error
)

echo.

REM Verificar Maven (mvnw)
echo [3/3] Verificando Maven Wrapper...
if exist "backend\app\mvnw.cmd" (
    echo ✓ Maven Wrapper está disponible
) else (
    echo ✗ Maven Wrapper NO encontrado
    echo   → Verifica que la carpeta backend/app/ existe
    goto error
)

echo.
echo ════════════════════════════════════════════════════════════════
echo                      ✓ TODO LISTO!
echo ════════════════════════════════════════════════════════════════
echo.
echo Ahora puedes ejecutar:
echo.
echo   Terminal 1: .\start-backend.ps1
echo   Terminal 2: .\start-frontend.ps1
echo.
echo Y acceder a: http://localhost:5500
echo.
timeout /t 5
exit /b 0

:error
echo.
echo ════════════════════════════════════════════════════════════════
echo                      ✗ ERROR ENCONTRADO
echo ════════════════════════════════════════════════════════════════
echo.
timeout /t 10
exit /b 1