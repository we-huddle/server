set -e
flyway -configFiles=flyway.conf migrate
rm -rf generated
./gradlew generateJavaJooq
