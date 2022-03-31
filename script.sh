echo "##################### BCP ###################"

echo " clean  ..."
./gradlew :app:clean

echo " assemble  ..."
./gradlew :app:assembleRelease

echo "completed script"
echo "##################### BCP ###################"