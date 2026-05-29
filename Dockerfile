# Usa uma imagem com JDK 25 para compilar o projeto Spring Boot
FROM eclipse-temurin:25-jdk AS build

# Define o diretório de trabalho dentro do container de build
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . .

# Dá permissão de execução ao Maven Wrapper, caso o projeto use ./mvnw
RUN chmod +x mvnw

# Compila o projeto e gera o arquivo .jar dentro da pasta target
# O parâmetro -DskipTests evita executar os testes durante o build da imagem
RUN ./mvnw clean package -DskipTests

# Usa uma imagem menor, apenas com JRE 25, para rodar a aplicação
FROM eclipse-temurin:25-jre

# Define o diretório de trabalho dentro do container final
WORKDIR /app

# Copia o arquivo .jar gerado na etapa de build para o container final
COPY --from=build /app/target/*.jar app.jar

# Informa que a aplicação utilizará a porta 8083
EXPOSE 8083

# Executa a aplicação Spring Boot na porta 8083
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8083"]