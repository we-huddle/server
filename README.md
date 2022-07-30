## weHuddle - Server code 

### Set up the project

#### Prerequisites,

1. Docker with docker compose 
2. Flyway cli tool
3. Java 11

#### Set up the database,
1. `cd` in to `database` directory
```
cd database
````
2. Run docker compose up 
```aidl
docker-compose up
```
or
```aidl
docker compose up
```
3. Run the migration
```aidl
cd ../
```
```aidl
./migrate.sh
```

#### Run the project
In the root directory run,
```
./gradlew run
```
Runs the app in the development mode.

Open http://0.0.0.0:8080/ to view it in the browser.

#### Notes 
1. After making changes to the database by making another version sql file in resources migrate.sh should be 
executed to generate the java orm files
2. All the environment variables should be accessed on the Application.kt file on the first run
3. Newly added environment variables should always be added on to the deployed version before merging a PR 

