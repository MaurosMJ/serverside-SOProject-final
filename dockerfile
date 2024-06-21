FROM node:12-alpine

# Defining workdirectory
WORKDIR /app

# Copy Files from the project to the container
# HERE COPIES ALL NEEDED FILES (FOLDER SOURCE)
COPY . .

# Installing Java 1.8
RUN apk add --no-cache openjdk8

# Install project dependencies
RUN yarn install --production

#Comando para iniciar a aplicação
CMD ["sh", "-c", "java -jar /app/SOCalc/dist/SOCalc.jar && tail -f /dev/null"]
CMD ["sh", "-c", "java -jar /app/SOLoader/dist/SOLoader.jar && tail -f /dev/null"]
CMD ["sh", "-c", "java -jar /app/SOMonitoring/dist/SOMonitoring.jar && tail -f /dev/null"]

# Container ALWAYS executing 
CMD ["tail", "-f", "/dev/null"]

#3000 = Monitoring.jar, 4000 = Loader.jar and 5000 = Calc.jar

EXPOSE 3000
EXPOSE 4000
EXPOSE 5000
