#!/bin/sh

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}

# Read in container limits and export the as environment variables
if [ -f "container-limits.sh" ]; then
    . /container-limits.sh
fi

if [ -f "java-default-options.sh" ]; then
    JAVA_OPTIONS=$(./java-default-options.sh)
    echo "Using JAVA_OPTIONS = " $JAVA_OPTIONS
fi

exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.klai.stl.ShopTheLookApp"  "$@"
