#!/bin/sh

# for debug purpose
echo "Running generator in $(pwd)"
echo "Working directory: "
ls -la

java -jar /github-readme-scraper.jar "$@"
