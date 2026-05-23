@echo off
echo ========================================
echo    RPG Terminal - Executar (Compilado)
echo ========================================
echo.

echo [1/2] Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao esta instalado!
    echo Por favor, instale Java JDK em: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
echo OK: Java encontrado
echo.

echo [2/2] Iniciando jogo...
echo.
java -cp ".;sqlite-jdbc-3.45.1.0.jar" RpgTerminal
pause
