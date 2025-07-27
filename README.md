# swimmingOrg
This version contains the structure of the java program, aswell as the first query and exit routine.

### In order to run:
- create the swimmingOrg database in postgreSQL (with pgAdmin 4)

- import tables

- import data

- install java package for vscode

- install "postgresql-42.7.7.jar"
  - (java 8 version: https://jdbc.postgresql.org/download/)

- drop postgresql-42.7.7.jar and PostgresqlSwimmingorg.java into the same folder

- open Git Bash in the folder (on windows 11: RMB the folder, "show more options", "open Git Bash here")


### compile changes using:
`javac -cp ".;postgresql-42.7.7.jar" PostgresqlSwimmingorg.java`

### run program using:
`java -cp ".;postgresql-42.7.7.jar" PostgresqlSwimmingorg`

### To edit:
open PostgresqlSwimmingorg.java with vscode

edited changes must be recompiled
