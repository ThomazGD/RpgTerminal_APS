@echo off
echo ========================================
echo    RPG Terminal - Script de Execucao
echo ========================================
echo.

echo [1/3] Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao esta instalado!
    echo Por favor, instale Java JDK em: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
echo OK: Java encontrado
echo.

echo [2/3] Compilando projeto...
javac -encoding UTF-8 -cp "sqlite-jdbc-3.45.1.0.jar" *.java 2>erros_compilacao.txt
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao!
    echo Verifique o arquivo erros_compilacao.txt para detalhes
    pause
    exit /b 1
)
echo OK: Compilacao concluida
echo.

echo [3/3] Iniciando jogo...
echo.
java -cp ".;sqlite-jdbc-3.45.1.0.jar" RpgTerminal
pause
