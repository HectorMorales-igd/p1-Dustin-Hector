# Pràctica 1 - Xarxes (Sockets Java)

## Membres del grup
- Héctor Morales Viñolo
- Dustin Sanchez Rodriguez

## Descripció
Adaptació d'una aplicació local de base de dades de videojocs a un model **client-servidor** mitjançant sockets Java.

El servidor gestiona la base de dades (`videogamesDB.dat`) i el client s'hi connecta remotament per fer consultes, afegir i eliminar videojocs.

## Estratègia implementada
**Servidor concurrent** amb `synchronized` per a l'accés a la base de dades, permetent múltiples clients simultanis.

## Detalls d’implementació

- El servidor crea un únic objecte `VideoGamesDB` compartit entre tots els clients, amb accés sincronitzat (`synchronized`) per evitar condicions de carrera.
- Cada client es gestiona en un fil separat (`ClientHandler`), mantenint la connexió oberta fins que l’usuari tria l’opció 5.
- El client utilitza `DataInputStream`/`DataOutputStream` i el format fix `VideoGameInfo.SIZE` per enviar i rebre la informació dels videojocs.

## Estructura del projecte
```
p1-Dustin-Hector/
├── src/
│   ├── Server.java          # Servidor concurrent amb sockets
│   ├── Client.java          # Client amb menú interactiu
│   ├── VideoGameInfo.java   # Model de dades (toBytes/fromBytes)
│   ├── VideoGameInfoReader.java
│   ├── VideoGamesDB.java    # Gestió de la DB (synchronized)
│   ├── PackUtils.java       # Utilitats d'empaquetat
│   └── GenerateVideoGamesDB.java
├── VideoGames/              # Fitxers de videojocs per generar la DB
├── videogamesDB.dat         # Base de dades (generada en local)
├── .gitignore
└── README.md
```
## Opcions del menú
| Opció | Descripció |
|-------|------------|
| 1 | Llistar títols de tots els videojocs |
| 2 | Consultar informació d'un videojoc |
| 3 | Afegir un videojoc |
| 4 | Eliminar un videojoc |
| 5 | Sortir |

## Protocol de comunicació
- **Opció 1**: Client envia `int 1` → Servidor respon `int` (num) + `UTF` per cada títol
- **Opció 2**: Client envia `int 2` + `UTF` (títol) → Servidor respon `boolean` + `bytes` (VideoGameInfo)
- **Opció 3**: Client envia `int 3` + `bytes` (VideoGameInfo) → Servidor respon `boolean`
- **Opció 4**: Client envia `int 4` + `UTF` (títol) → Servidor respon `boolean`
- **Opció 5**: Client envia `int 5` → Tanca connexió
