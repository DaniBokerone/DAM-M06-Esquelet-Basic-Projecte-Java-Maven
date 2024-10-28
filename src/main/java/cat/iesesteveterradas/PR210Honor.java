package cat.iesesteveterradas;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class PR210Honor {

    public static void main(String[] args) throws SQLException {
        
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "database.db";
        ResultSet rs = null;

        // Si no hi ha l'arxiu creat, el crea i li posa dades
        File fDatabase = new File(filePath);
        if (!fDatabase.exists()) { initDatabase(filePath); }

        // Connectar (crea la BBDD si no existeix)
        Connection conn = UtilsSQLite.connect(filePath);

        PR210Honor app = new PR210Honor();
        app.showActionMenu(conn);
        
        // Desconnectar
        UtilsSQLite.disconnect(conn);
    }

    public void showActionMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (option != 4) {
            System.out.println("------------------------------------");
            System.out.println("Acciones disponibles:");
            System.out.println("1 - Consultar una taula");
            System.out.println("2 - Mostrar personatges per facció");
            System.out.println("3 - Mostrar els millors de cada faccio");
            System.out.println("4 - Sortir");
            System.out.println("------------------------------------");
            System.out.print("Selecciona una opció: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    //Sub menu taules
                    showTableSubMenu(conn);
                    break;
                case 2:
                    // Sub menu faccions
                    showFactionSubMenu(conn);
                    break;
                case 3:
                    // Sub menu els millors
                    showBestOfFactionSubMenu(conn);
                    break;
                case 4:
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            }
        }

        scanner.close();
    }

    // Tables Menu
    public void showTableSubMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int subOption = 0;

        while (subOption != 3) {
            System.out.println("------------------------------------");
            System.out.println("1 - Personatge");
            System.out.println("2 - Faccio");
            System.out.println("3 - Tornar al menu principal");
            System.out.println("------------------------------------");
            System.out.print("Selecciona una opció: ");
            subOption = scanner.nextInt();

            switch (subOption) {
                case 1:
                    getAllTableInfo(conn,"Personatge");
                    break;
                case 2:
                    getAllTableInfo(conn,"Faccio");
                    break;
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            } 
        }
    }

    private void getAllTableInfo(Connection conn, String table) {
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT * FROM " + table + ";");
            ResultSetMetaData rsmd = rs.getMetaData();
    
            System.out.println("Informació de la taula:");
            printTable(rs, rsmd);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Faccions Menu
    public void showFactionSubMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int subOption = 0;

        while (subOption != 4) {
            System.out.println("------------------------------------");
            System.out.println("1 - Cavallers");
            System.out.println("2 - Vikings");
            System.out.println("3 - Samurais");
            System.out.println("4 - Tornar al menu principal");
            System.out.println("------------------------------------");
            subOption = scanner.nextInt();

            switch (subOption) {
                case 1:
                    getFactionCharactersTableInfo(conn,"Cavallers");
                    break;
                case 2:
                    getFactionCharactersTableInfo(conn,"Vikings");
                    break;
                case 3:
                    getFactionCharactersTableInfo(conn,"Samurais");
                    break;
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            }
            
        }
    }

    private void getFactionCharactersTableInfo(Connection conn,String faccio) {
        try {
            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT p.id as 'id personatge',p.nom,p.atac,p.defensa, fac.id as 'id Faccio',fac.nom as 'nom Faccio'"
            +" FROM Personatge AS p LEFT JOIN Faccio AS fac ON p.idFaccio = fac.id "
            +"WHERE fac.nom LIKE '%" + faccio + "%';");            
            ResultSetMetaData rsmd = rs.getMetaData();
    
            System.out.println("Informació de la taula:");
            printTable(rs, rsmd);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mejor en Faccions Menu
    public void showBestOfFactionSubMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int subOption = 0;

        while (subOption != 4) {
            System.out.println("------------------------------------");
            System.out.println("1 - Cavallers");
            System.out.println("2 - Vikings");
            System.out.println("3 - Samurais");
            System.out.println("4 - Tornar al menu principal");
            System.out.println("------------------------------------");
            subOption = scanner.nextInt();

            switch (subOption) {
                case 1:
                    getBestFactionCharactersTableInfo(conn,"Cavallers");
                    break;
                case 2:
                    getBestFactionCharactersTableInfo(conn,"Vikings");
                    break;
                case 3:
                    getBestFactionCharactersTableInfo(conn,"Samurais");
                    break;
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            }
            
        }
    }


    private void getBestFactionCharactersTableInfo(Connection conn,String faccio) {

        Scanner scanner = new Scanner(System.in);
        String column = "";
        Boolean continueCheck = false;

        try {
            int subOption = 0;

            while (!continueCheck && subOption != 3) {
                System.out.println("------------------------------------");
                System.out.println("De quina estadistica vols veure els millors personatges:");
                System.out.println("1 - Atac");
                System.out.println("2 - Defensa");
                System.out.println("3 - Sortir al menu principal");
                System.out.println("------------------------------------");
                subOption = scanner.nextInt();

                switch (subOption) {
                    case 1:
                        column = "p.atac";
                        continueCheck = true;
                        break;
                    case 2:
                        column = "p.defensa";
                        continueCheck = true;
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Opció no vàlida. Intenta-ho de nou.");
                }
                    
            }

            ResultSet rs = UtilsSQLite.querySelect(conn, "SELECT p.id as 'id personatge',p.nom,p.atac,p.defensa, fac.id as 'id Faccio',fac.nom as 'nom Faccio'"
            +" FROM Personatge AS p LEFT JOIN Faccio AS fac ON p.idFaccio = fac.id "
            +"WHERE fac.nom LIKE '%" + faccio + "%'"
            +"ORDER BY " + column + " DESC, p.id ASC LIMIT 1;");            
            ResultSetMetaData rsmd = rs.getMetaData();
    
            System.out.println("Aquest es el millor personatge:");
            printTable(rs, rsmd);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTable(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        int columnCount = rsmd.getColumnCount();
    
        //Headers
        StringBuilder header = new StringBuilder();
        StringBuilder headerLines = new StringBuilder();
        for (int cnt = 1; cnt <= columnCount; cnt++) { 
            String label = rsmd.getColumnLabel(cnt);
            header.append(String.format("%-15s", label));
            headerLines.append(String.format("%-15s", "-------"));
        }
        System.out.println(header.toString());
        System.out.println(headerLines.toString());
    
        //Table Info
        while (rs.next()) {
            StringBuilder row = new StringBuilder();
            for (int cnt = 1; cnt <= columnCount; cnt++) {
                row.append(String.format("%-15s", rs.getString(cnt)));
            }
            System.out.println(row.toString());
        }
    }


    static void initDatabase(String filePath) {
        Connection conn = UtilsSQLite.connect(filePath);

        // Esborrar la taula (per si existeix)
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Faccio;");
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Personatge;");


        // Crear una nova taula
        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Faccio ("
                                    + " id integer PRIMARY KEY AUTOINCREMENT,"
                                    + " nom varchar(15) NOT NULL,"
                                    + " resum text(500) NOT NULL);");

        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Personatge ("
                                    + " id integer PRIMARY KEY AUTOINCREMENT,"
                                    + " nom varchar(15) NOT NULL,"
                                    + " atac float NOT NULL,"
                                    + " defensa float NOT NULL,"
                                    + " idFaccio integer NOT NULL,"
                                    + " FOREIGN KEY(idFaccio) REFERENCES Faccio(id));");

        // Afegir elements a una taula
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES (\"Cavallers\", \"Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion.\");");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES (\"Vikings\",   \"The Vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn.\");");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES (\"Samurais\",  \"The Samurai are the most unified of the three factions, though this does not say much as the Daimyos were often battling each other for dominance.\");");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warden\",      1, 3, 1);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Conqueror\",   2, 2, 1);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Peacekeep\",   2, 3, 1);");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Raider\",    3, 3, 2);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Warlord\",   2, 2, 2);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Berserker\", 1, 1, 2);");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Kensei\",  3, 2, 3);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Shugoki\", 2, 1, 3);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (\"Orochi\",  3, 2, 3);");

        UtilsSQLite.disconnect(conn);
    }
    
}
