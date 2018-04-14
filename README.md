# SistemaPizzaria
Projeto 4º semestre SI - Prova de conceito - Sistema para pizzaria (Console)


##Para criar arquivo "bat" para execução do .jar no console do windows:
Criar novo arquivo texto com o texto abaixo:
-----------

@echo off 

C:

CD \CAMINHO COMPLETO DO DIRETORIO ONDE ESTA O JAR\

Title Sistema Pizzaria

java.exe -jar SistemaPizzaria.jar

----------
->Salvar este arquivo texto com a extensão ".bat"
OBS:   ".jar" está na pasta 'dist'

##Para conexão ao banco:
Método dbCon() da classe EntidadeBase possui dados de conexão para banco de dados.

        String url = "jdbc:postgresql://localhost:5432/pizzaria";  
        //localhost é o nome do servidor e 5432 a porta(definidos junto ao usuário e senha quando instalou o postgres)
        // "pizzaria" é o nome do banco de dados

        String usuario = "postgres";  
        //usuário de acesso ao servidor
        
        String senha = "postgres";    
        //senha de acesso ao servidor

OBS:   Classes estão na pasta 'src'
