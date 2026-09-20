name: Build
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
      - name: Build Plugin
        run: |
          chmod +x gradlew
         ./gradlew make --stacktrace
      - uses: actions/upload-artifact@v4
        with:
          name: cs3
          path: FaridExtensions/build/*.cs3
