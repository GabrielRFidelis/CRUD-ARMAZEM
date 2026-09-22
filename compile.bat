@echo off
echo =========================================
echo  Compilando Sistema de Estoque...
echo =========================================

if not exist "lib\postgresql.jar" (
    echo [ERRO] Driver PostgreSQL nao encontrado em lib\postgresql.jar
    echo Baixe o driver em: https://jdbc.postgresql.org/download/
    echo Coloque o arquivo .jar na pasta lib com o nome postgresql.jar
    pause
    exit /b 1
)

if not exist "out" mkdir out

javac -encoding UTF-8 -cp "lib\postgresql.jar" -d out src\Main.java src\connection\ConnectionFactory.java src\model\*.java src\dao\*.java src\service\*.java src\controller\*.java src\util\Menu.java

if %errorlevel% neq 0 (
    echo [ERRO] Falha na compilacao.
    pause
    exit /b 1
)

echo.
echo =========================================
echo  Compilacao concluida! Executando...
echo =========================================
echo.

java -cp "out;lib\postgresql.jar" Main

pause
