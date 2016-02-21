# zeptictac
- Tic Tac Toe game
- REST API JSON Java Jersey Jackson Maven
- Supports custom NxN field size and X number of players

# version
1.0

# requirements
- Java 8 (tested in 1.8.0_74)
- Apache Maven (tested in 3.3.9)
- Apache Tomcat (tested in 7.0.47)
- Git (tested in 1.9.4)

# note before installation
Configuration values can be defined:
```sh
$ cat /src/main/resources/zeptictac.properties
field.size=3 # Default 3x3 grid
symbol.empty=-
symbol.player.1=X # Default two players using symbols 'X' and 'O'
symbol.player.2=O # Minimum two players
pagination.limit=5
```

# installation
```sh
$ git clone https://github.com/adriwankenobi/zeptictac.git
$ cd zeptictac
$ # execute integration tests
$ mvn clean test
$ # generate war
$ mvn compile war:war
$ # deploy in tomcat (Linux = move war to webapps folder /opt/tomcat/webapps)
$ cp target/zeptictac.war $TOMCAT_WEBAPPS_FOLDER
$ # start tomcat (Linux = /etc/init.d/tomcat restart)
$ ./restart_tomcat.sh
```

# usage
- Show games
    - GET /v1/games?offset=:offset&limit=:limit
        - Query params:
            - optional: offset[numeric between 0 and 'total games size']
            - optional: limit[numeric between 1 and 20]
        - Success response:
            - Code: 200 OK
            - Content: 
                ```javascript 
               {"data":
                       [ {"id": "c67782q3aen8vs0l6dg0o8nbud",
                          "field": [["-", "X", "O"],["-", "X", "-"],["-", "-", "-"]],
                          "players": [ {"id": "5pqtnt3pa1304tc4t6mcmbc3h3",
                                        "nickname": "AnonymousPlayer1",
                                        "symbol": "X"},
                                        {"id": "jb3ds6kum3qagr2132dsl5nkmh",
                                        "nickname": "adriwankenobi",
                                        "symbol": "O"}],
                          "isFinished": false,
                          "turnNumber": 3,
                          "nextTurn": "jb3ds6kum3qagr2132dsl5nkmh",
                          "createdAt": "2016-02-21T15:09:11+0000"},
                         {...},
                         {...} ],
                "previuos": "http://localhost:8080/zeptictac/v1/games?limit=3",
                "next": "http://localhost:8080/zeptictac/v1/games?offset=6&limit=3"}
                ```
        - Error response:
            - Code: 400 BAD REQUEST
                -  When params have bad format
- Show game
    - GET /v1/games/:gameId
        - Path params:
            - required: gameId[alphanumeric, lower case letters]
        - Success response:
            - Code: 200 OK
            - Content: 
                ```javascript 
               {"id": "c67782q3aen8vs0l6dg0o8nbud",
                "field": [["-", "X", "O"],["-", "X", "-"],["-", "-", "-"]],
                "players": [ {"id": "5pqtnt3pa1304tc4t6mcmbc3h3",
                              "nickname": "AnonymousPlayer1",
                              "symbol": "X"},
                             {"id": "jb3ds6kum3qagr2132dsl5nkmh",
                              "nickname": "adriwankenobi",
                              "symbol": "O"}],
                "isFinished": false,
                "turnNumber": 3,
                "nextTurn": "jb3ds6kum3qagr2132dsl5nkmh",
                "createdAt": "2016-02-21T15:09:11+0000"}
                ```
        - Error response:
            - Code: 204 NO CONTENT 
                - When "gameId" does not exist
            - Code: 400 BAD REQUEST
                -  When params have bad format
- Create new game
    - POST /v1/games?nickname=:nickname
        - Query params:
            - optional: nickname[alphanumeric, lower case letters]
        - Success response:
            - Code: 200 OK
            - Content: 
                ```javascript 
               {"id": "c67782q3aen8vs0l6dg0o8nbud",
                "field": [["-", "-", "-"],["-", "-", "-"],["-", "-", "-"]],
                "players": [ {"id": "5pqtnt3pa1304tc4t6mcmbc3h3",
                              "nickname": "AnonymousPlayer1",
                              "symbol": "X"}],
                "isFinished": false,
                "turnNumber": 0,
                "nextTurn": "c67782q3aen8vs0l6dg0o8nbud",
                "createdAt": "2016-02-21T15:09:11+0000"}
                ```
        - Error response:
            - Code: 400 BAD REQUEST
                -  When params have bad format
- Join a game
    - PATCH /v1/games/:gameId?nickname=:nickname
        - Path params:
            - required: gameId[alphanumeric, lower case letters]
        - Query params:
            - optional: nickname[alphanumeric, lower case letters]
        - Success response:
            - Code: 200 OK
            - Content: 
                ```javascript 
               {"id": "c67782q3aen8vs0l6dg0o8nbud",
                "field": [["-", "-", "-"],["-", "-", "-"],["-", "-", "-"]],
                "players": [ {"id": "5pqtnt3pa1304tc4t6mcmbc3h3",
                              "nickname": "AnonymousPlayer1",
                              "symbol": "X"},
                             {"id": "jb3ds6kum3qagr2132dsl5nkmh",
                              "nickname": "recentlyJoinedPlayer",
                              "symbol": "O"}],
                "isFinished": false,
                "turnNumber": 0,
                "nextTurn": "c67782q3aen8vs0l6dg0o8nbud",
                "createdAt": "2016-02-21T15:09:11+0000"}
                ```
        - Error response:
            - Code: 204 NO CONTENT 
                - When "gameId" does not exist
            - Code: 400 BAD REQUEST
                -  When params have bad format
            - Code: 403 FORBIDDEN
                - When game is already full
- Put mark into game
    - PUT /v1/games/:gameId
        - Path params:
            - required: gameId[alphanumeric, lower case letters]
        - Data params:
            - required: playerId[alphanumeric, lower case letters]
            - required: x[numeric between 0 and grid_size-1]
            - required: y[numeric between 0 and grid_size-1]
             ```javascript 
            {"playerId": "c67782q3aen8vs0l6dg0o8nbud", "x": 0, "y": 0}
             ```
        - Success response:
            - Code: 200 OK
            - Content: 
                ```javascript 
               {"id": "c67782q3aen8vs0l6dg0o8nbud",
                "field": [["X", "-", "-"],["-", "-", "-"],["-", "-", "-"]],
                "players": [ {"id": "5pqtnt3pa1304tc4t6mcmbc3h3",
                              "nickname": "AnonymousPlayer1",
                              "symbol": "X"},
                             {"id": "jb3ds6kum3qagr2132dsl5nkmh",
                              "nickname": "recentlyJoinedPlayer",
                              "symbol": "O"}],
                "isFinished": false,
                "turnNumber": 1,
                "nextTurn": "jb3ds6kum3qagr2132dsl5nkmh",
                "createdAt": "2016-02-21T15:09:11+0000"}
                ```
        - Error response:
            - Code: 204 NO CONTENT 
                - When "gameId" does not exist
            - Code: 400 BAD REQUEST
                -  When params have bad format
            - Code: 401 UNAUTHORIZED
                - When "playerId" is not a player of the game
            - Code: 403 FORBIDDEN
                - When game is already finished
                - When there is not enough player's yet
                - When it's not the turn of "playerId"
                - When (x, y) cell is outside the grid
                - When (x, y) cell is not empty

# release notes
- Jersey 2.22 for implementing the REST server
- Jackson 2.22 for serializing/deserializing JSON objects
- REST entry points:
    - Implementation: "src/main/java/com/acerete/rest"
    - Custom filters: Decoration (Allows GZIP compressed payloads), authorization and validation
- Manual configuration support
    - Implementation: "src/main/java/com/acerete/config"
    - Config file: "src/main/resources/zeptictac.properties"
- Data model not persisted. Data lives while server is alive
    - Implementation: "src/main/java/com/acerete/datamodel"
    - Normally this would be a database layer, so I didn't worry too much about performance
- Singleton Service layer
    - Implementation: "src/main/java/com/acerete/services"
- JUnit 4 for integration tests
    - Embedded Grizzly server for testing (jersey-test)
    - Config and integration tests: "/src/test/java"