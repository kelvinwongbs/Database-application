import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import static java.lang.Math.floor;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
	
    public static final Scanner scanner = new Scanner(System.in);
    
    public static Connection connect() throws SQLException {
        String dbAddress = "jdbc:mysql://";
        String dbUsername = "";
        String dbPassword = "";
        
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR]: Java MySQL DB Driver not found!");
            System.exit(0);
        }
        
        return con;
    }
    
    public static void createTables() throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;

        sql = "DROP TABLE IF EXISTS driver, vehicle, passenger, request, trip, taxi_stop;";
        stmt.executeUpdate(sql);
        
        //driver
        sql = "CREATE TABLE driver ("
                + "id integer primary key, "
                + "name varchar(30) not null, "
                + "vehicle_id char(6) not null, "
                + "driving_years integer);";
        stmt.executeUpdate(sql);
        //vehicle
        sql = "CREATE TABLE vehicle ("
                + "id char(6) primary key, "
                + "model varchar(30) not null, "
                + "seats integer);";
        stmt.executeUpdate(sql);
        //passenger
        sql = "CREATE TABLE passenger ("
                + "id integer primary key, "
                + "name varchar(30) not null);";
        stmt.executeUpdate(sql);
        //request
        sql = "CREATE TABLE request ("
                + "id integer primary key, "
                + "passenger_id integer, "
                + "start_location varchar(20) not null, "
                + "destination varchar(20) not null, "
                + "model varchar(30) not null, "
                + "passengers integer, "
                + "taken boolean, "
                + "driving_years integer);";
        stmt.executeUpdate(sql);
        //trip
        sql = "CREATE TABLE trip ("
                + "id integer primary key, "
                + "driver_id integer, "
                + "passenger_id integer, "
                + "start_time datetime, "
                + "end_time datetime, "
                + "start_location varchar(20) not null, "
                + "destination varchar(20) not null, "
                + "fee integer);";
        stmt.executeUpdate(sql);
        //taxi_stop
        sql = "CREATE TABLE taxi_stop ("
                + "name varchar(20) primary key, "
                + "location_x integer, "
                + "location_y integer);";
        stmt.executeUpdate(sql);

        
        stmt.close();
        con.close();
    }
    
    public static void deleteTables() throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
        sql = "DROP TABLE IF EXISTS driver, vehicle, passenger, request, trip, taxi_stop;";
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
    }
    
    public static boolean loadData(String path) throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
        
        // drivers
        try {
            Scanner filescanner = new Scanner(new File(path + "/drivers.csv"));
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine();
                String[] field = line.split(",");
                
                sql = String.format("Insert into driver values (%s, '%s', '%s', %s);", field[0], field[1], field[2], field[3]);
                stmt.executeUpdate(sql);
            }
        } catch(FileNotFoundException e) {
            stmt.close();
            con.close();
            return false;
        }
        // vehicles
        try {
            Scanner filescanner = new Scanner(new File(path + "/vehicles.csv"));
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine();
                String[] field = line.split(",");
                
                sql = String.format("Insert into vehicle values ('%s', '%s', %s);", field[0], field[1], field[2]);
                stmt.executeUpdate(sql);
            }
        } catch(FileNotFoundException e) {
            stmt.close();
            con.close();
            return false;
        }
        // passengers
        try {
            Scanner filescanner = new Scanner(new File(path + "/passengers.csv"));
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine();
                String[] field = line.split(",");
                
                sql = String.format("Insert into passenger values (%s, '%s');", field[0], field[1]);
                stmt.executeUpdate(sql);
            }
        } catch(FileNotFoundException e) {
            stmt.close();
            con.close();
            return false;
        }
        // trips
        try {
            Scanner filescanner = new Scanner(new File(path + "/trips.csv"));
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine();
                String[] field = line.split(",");
                
                sql = String.format("Insert into trip values (%s, %s, %s, '%s', '%s', '%s', '%s', %s);", 
                        field[0], field[1], field[2], field[3], field[4], field[5], field[6], field[7]);
                stmt.executeUpdate(sql);
            }
        } catch(FileNotFoundException e) {
            stmt.close();
            con.close();
            return false;
        }
        // taxi_stops
        try {
            Scanner filescanner = new Scanner(new File(path + "/taxi_stops.csv"));
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine();
                String[] field = line.split(",");
                
                sql = String.format("Insert into taxi_stop values ('%s', %s, %s);", field[0], field[1], field[2]);
                stmt.executeUpdate(sql);
            }
        } catch(FileNotFoundException e) {
            stmt.close();
            con.close();
            return false;
        }
        
        stmt.close();
        con.close();
        return true;
    }
    
    public static void checkData() throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = "SELECT COUNT(*) FROM vehicle;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Vehicle: %d\n", resultSet.getInt(1));
	}
        
        sql = "SELECT COUNT(*) FROM passenger;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Passenger: %d\n",resultSet.getInt(1));
	}
        
        sql = "SELECT COUNT(*) FROM driver;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Driver: %d\n",resultSet.getInt(1));
	}
        
        sql = "SELECT COUNT(*) FROM trip;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Trip: %d\n",resultSet.getInt(1));
	}
        
        sql = "SELECT COUNT(*) FROM request;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Request: %d\n",resultSet.getInt(1));
	}
        
        sql = "SELECT COUNT(*) FROM taxi_stop;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    System.out.printf("Taxi_Stop: %d\n",resultSet.getInt(1));
	}

        stmt.close();
        resultSet.close();
        con.close();
    }
    
    public static int requestRide(int passengers, String model, int driving_years) throws SQLException {
        int drivers_n = 0;
        
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = String.format("SELECT COUNT(driver.id) FROM driver, vehicle WHERE vehicle.seats>=%d AND vehicle.model LIKE '%%%s%%' AND driver.vehicle_id=vehicle.id AND driver.driving_years>=%d;",
                passengers, model, driving_years);
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    drivers_n = resultSet.getInt(1);
	}
        
        stmt.close();
        resultSet.close();
        con.close();
        
        return drivers_n;
    }
    
    public static int findLastRequestID() throws SQLException {
        int request_id = 0;
        
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = "SELECT MAX(id) FROM request;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    request_id = resultSet.getInt(1);
	}
        
        stmt.close();
        resultSet.close();
        con.close();
        
        return request_id;
    }
    
    public static boolean checkLocation(String location) throws SQLException {
        boolean check = false;
        
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = String.format("SELECT name FROM taxi_stop WHERE name='%s';", location);
        resultSet = stmt.executeQuery(sql);
        
        check = resultSet.isBeforeFirst();
        
        stmt.close();
        resultSet.close();
        con.close();
        
        return check;
    }
    
    public static void insertRequest(int id, int passenger_id, String start_location, String destination, String model, int passengers, int driving_years) throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
        
        sql = "Insert into request values (%d, %d, '%s', '%s', '%s', %d, 0, %d);";
        sql = String.format(sql, id, passenger_id, start_location, destination, model, passengers, driving_years);
        stmt.executeUpdate(sql);
        
        stmt.close();
        con.close();
    }
    
    public static void checkRecords(int passenger_id, String start_date, String end_date, String destination) throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = "SELECT trip.id, driver.name, vehicle.id, vehicle.model, start_time, end_time, fee, start_location, destination "
                + "FROM trip, driver, vehicle "
                + "WHERE passenger_id=%d AND destination='%s' "
                + "AND DATE(start_time)>'%s' AND DATE(end_time)<'%s' "
                + "AND trip.driver_id=driver.id AND driver.vehicle_id=vehicle.id "
                + "ORDER BY start_time DESC;";
        sql = String.format(sql, passenger_id, destination, start_date, end_date);
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
	    int trip_id = resultSet.getInt(1);
            String driver_name = resultSet.getString(2);
            String vehicle_id = resultSet.getString(3);
            String vehicle_model = resultSet.getString(4);
            String start = resultSet.getTimestamp(5).toString();
            String end = resultSet.getTimestamp(6).toString();
            int fee = resultSet.getInt(7);
            String start_location = resultSet.getString(8);
            String dest = resultSet.getString(9);
            System.out.printf("%d, %s, %s, %s, %s, %s, %d, %s, %s\n", 
                    trip_id, driver_name, vehicle_id, vehicle_model, start, end, fee, start_location, dest);
	}
        
        stmt.close();
        resultSet.close();
        con.close();
    }
    
    public static void searchRequest(int driver_id, int driver_location_x, int driver_location_y, int max_distance) throws SQLException {
	Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
		
	// SQL
	sql = "SELECT R1.id, passenger.name, R1.passengers, R1.start_location, R1.destination "
                        + "FROM request R1, passenger, vehicle, driver "
                        + "WHERE R1.passenger_id = passenger.id AND "
                        + "R1.taken = false AND "
                        + "driver.id = %d AND "
                        + "driver.vehicle_id = vehicle.id AND "
                        + "vehicle.seats >= R1.passengers AND "
                        + "vehicle.model LIKE CONCAT('%%', R1.model, '%%') AND "
                        + "driver.driving_years >= R1.driving_years AND "
                        + "%d >= ABS(%d - (SELECT taxi_stop.location_x "
                                        + "FROM taxi_stop, request R2 "
                                        + "WHERE R2.start_location = taxi_stop.name AND R1.id = R2.id))"
                        + " + ABS(%d - (SELECT taxi_stop.location_y "
                                     + "FROM taxi_stop, request R3 "
                                     + "WHERE R3.start_location = taxi_stop.name AND R1.id = R3.id));";
        sql = String.format(sql, driver_id, max_distance, driver_location_x, driver_location_y);
		
	System.out.println("request ID, passenger name, num of passengers, start location, destination");
	ResultSet rs = stmt.executeQuery(sql);
	while (rs.next()){
		int request_id = rs.getInt("R1.id");
		String passenger_name = rs.getString("passenger.name");
		int request_passengers = rs.getInt("R1.passengers");
		String start_location = rs.getString("R1.start_location");
		String destination = rs.getString("R1.destination");
		System.out.printf("%d, %s, %d, %s, %s\n",request_id, passenger_name, request_passengers, start_location, destination);
	}
		stmt.close();
        rs.close();
        con.close();
    }
    
    public static void takeRequest(int driver_id, int request_id) throws SQLException {
	Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String sql;
		
	// get current time
        String current_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	// SQL for set request be taken
	sql = String.format("UPDATE request SET taken = true WHERE id = %d;", request_id);
	stmt.executeUpdate(sql);
		
	// SQL for find last trip_id
	sql = "SELECT MAX(id) AS max FROM trip;";
	rs = stmt.executeQuery(sql);
	int max = 0;
	while (rs.next()){
            max = rs.getInt("max");
	}
		
	int passenger_id = 0;
        String start_location = null, destination = null;
		
        sql = "SELECT passenger_id, start_location, destination "
                + "FROM request WHERE id = %d";
        sql = String.format(sql, request_id);
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
	    passenger_id = rs.getInt("passenger_id");
	    start_location = rs.getString("start_location");
	    destination = rs.getString("destination");
        }

        // SQL for create trip
	sql = "INSERT INTO trip VALUES (%d, %d, %d, '%s', NULL, '%s', '%s', NULL);";
	sql = String.format(sql, max + 1, driver_id, passenger_id, current_time, start_location, destination);
	stmt.executeUpdate(sql);
		
	// SQL for display info
	sql = "SELECT trip.id, passenger.name, trip.start_time "
		+ "FROM trip, passenger "
		+ "WHERE trip.id = %d AND passenger.id = trip.passenger_id;";
	sql = String.format(sql, max + 1);
	rs = stmt.executeQuery(sql);
	while (rs.next()) {
            int trip_id = rs.getInt("trip.id");
            String passenger_name = rs.getString("passenger.name");
            String start_time = rs.getTimestamp(3).toString();
            System.out.printf("%d, %s, %s\n", trip_id, passenger_name, current_time);
	}
		stmt.close();
        rs.close();
        con.close();
    }
    
    public static void finishTrip(int trip_id) throws SQLException, ParseException {
	Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
		
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // get end time
	Date end_time = new Date();
        String end_time_string = format.format(end_time);
		
	// retrieve the start time
	String start_time_str = null;
	sql = "SELECT trip.start_time "
                + "FROM trip "
		+ "WHERE trip.id = %d;";
	sql = String.format(sql, trip_id);
	ResultSet rs = stmt.executeQuery(sql);
	while (rs.next()){
            start_time_str = rs.getTimestamp(1).toString();
	}
	
	Date start_time = format.parse(start_time_str);
	// calculate duration
	int fee = (int)floor((end_time.getTime() - start_time.getTime()) / (60 * 1000));
		
	// update end time and fee
	sql = "UPDATE trip "
                + "SET end_time = '%s', "
		+ "fee = %d "
		+ "WHERE id = %d;";
	sql = String.format(sql, end_time_string, fee, trip_id);
	stmt.executeUpdate(sql);
		
	// display the trip
	System.out.println("Trip ID, Passenger name, Start, End, Fee");
	sql = "SELECT trip.id, passenger.name, trip.start_time, trip.end_time, trip.fee "
                + "FROM trip, passenger "
		+ "WHERE trip.id = %d AND "
		+ "passenger.id = trip.passenger_id";
	sql = String.format(sql, trip_id);
	rs = stmt.executeQuery(sql);
	while (rs.next()) {
            int print_trip_id = rs.getInt("trip.id");
            String print_passenger_name = rs.getString("passenger.name");
            String print_start_time = rs.getTimestamp(3).toString();
            String print_end_time = rs.getTimestamp(4).toString();
            int print_fee = rs.getInt("trip.fee");
            System.out.printf("%d, %s, %s, %s, %d\n", print_trip_id, print_passenger_name, print_start_time, print_end_time, print_fee);
        }
		
        stmt.close();
        rs.close();
        con.close();
    }
    
    public static int findUnfinishedTrips(int driver_id) throws SQLException {
	int trip_id = 0;
        
        Connection con = connect();
        Statement stmt = con.createStatement();
        String sql;
		
	// print unfinished trip
	sql = "SELECT trip.id, trip.passenger_id, trip.start_time "
                + "FROM trip "
                + "WHERE trip.driver_id = %d AND "
		+ "trip.end_time is NULL;";
	sql = String.format(sql, driver_id);
	ResultSet rs = stmt.executeQuery(sql);
	while (rs.next()){
		trip_id = rs.getInt("trip.id");
		int passenger_id = rs.getInt("trip.passenger_id");
		String start_time = rs.getTimestamp(3).toString();
		System.out.printf("%d, %d, %s\n", trip_id, passenger_id, start_time);
	}
        stmt.close();
        rs.close();
        con.close();
	
        return trip_id;
    }
    
    public static void findFinishedTrips(int min_dist, int max_dist) throws SQLException {
        Connection con = connect();
        Statement stmt = con.createStatement();
        ResultSet resultSet;
        String sql;
        
        sql = "SELECT DISTINCT trip.id, driver.name, passenger.name, trip.start_location, trip.destination, trip.fee "
                + "FROM trip, driver, passenger, (Select * From taxi_stop) AS start, (SELECT * From taxi_stop) AS dest "
                + "WHERE trip.driver_id = driver.id AND trip.passenger_id = passenger.id AND "
                + "trip.start_location = start.name AND trip.destination = dest.name AND "
                + "(ABS(start.location_x - dest.location_x) + ABS(start.location_y - dest.location_y)) BETWEEN %d AND %d;";
        sql = String.format(sql, min_dist, max_dist);
	resultSet = stmt.executeQuery(sql);
        System.out.println("trip id, driver name, passenger name, start location, destination, duration");
        while(resultSet.next()){
            int trip_id = resultSet.getInt(1);
            String driver_name = resultSet.getString(2);
            String passenger_name = resultSet.getString(3);
            String start_location = resultSet.getString(4);
            String destination = resultSet.getString(5);
            int duration = resultSet.getInt(6);
            
            System.out.printf("%d, %s, %s, %s, %s, %d\n", trip_id, driver_name, passenger_name, start_location, destination, duration);
        }
        
        stmt.close();
        resultSet.close();
        con.close();
    }
    
    public static void Administrator() throws SQLException {
        while (true) {
            System.out.println("Administrator, what would you like to do?");
            System.out.println("1. Create tables");
            System.out.println("2. Delete tables");
            System.out.println("3. Load data");
            System.out.println("4. Check data");
            System.out.println("5. Go back");
            System.out.println("Please enter [1-4]");
            
            String op = scanner.nextLine();
            
            if (op.equals("1")) {
                System.out.print("Processing...");
                createTables();
                System.out.println("Done! Tables are created!");
            } else if (op.equals("2")) {
                System.out.print("Processing...");
                deleteTables();
                System.out.println("Done! Tables are deleted!");
            } else if (op.equals("3")) {
                System.out.println("Please enter the folder path");
                String path = scanner.nextLine();
            
                System.out.print("Processing...");
                if (loadData(path)) {
                    System.out.println("Done! Data is loaded!");
                } else {
                    System.out.println("[ERROR] Data not found!");
                }
            } else if (op.equals("4")) {
                System.out.println("Number of records in each table:");
                checkData();
            } else if (op.equals("5")){
                break;
            } else {
                System.out.println("[ERROR]: Invalid input");
            }
        }
    }
    
    public static void Passenger() throws SQLException {
        while (true) {
            System.out.println("Passenger, what would you like to do?");
            System.out.println("1. Request a ride");
            System.out.println("2. Check trip records");
            System.out.println("3. Go back");
            System.out.println("Please enter [1-3]");
        
            String op = scanner.nextLine();
        
            if (op.equals("1")) {
                System.out.println("Please enter your ID.");
                int id = Integer.parseInt(scanner.nextLine());
                
                System.out.println("Please enter the number of passengers");
                int passengers = Integer.parseInt(scanner.nextLine());
                
                while (passengers<1 || passengers>8) {
                    System.out.println("[ERROR] Invalid number of passenegers.");
                    System.out.println("Please enter the number of passengers");
                    passengers = Integer.parseInt(scanner.nextLine());
                }
                
                System.out.println("Please enter the start location");
                String start_location = scanner.nextLine();
                
                while (!checkLocation(start_location)) {
                    System.out.println("[ERROR] Start location not found.");
                    System.out.println("Please enter the start location");
                    start_location = scanner.nextLine();
                }
                
                System.out.println("Please enter the destination");
                String destination = scanner.nextLine();
                
                while (destination.equals(start_location)) {
                    System.out.println("[ERROR] Destination and start location should be different.");
                    System.out.println("Please enter the destination");
                    destination = scanner.nextLine();
                }
                while (!checkLocation(destination)) {
                    System.out.println("[ERROR] Destination not found.");
                    System.out.println("Please enter the destination");
                    start_location = scanner.nextLine();
                }
                
                System.out.println("Please enter the model. (Press enter to skip)");
                String model = scanner.nextLine();
                
                System.out.println("Please enter the minimum driving years. (Press enter to skip)");
                String tmp = scanner.nextLine();
                int driving_years = tmp.equals("") ? 0 : Integer.parseInt(tmp);
            
                int drivers_n = requestRide(passengers, model, driving_years);
                if (drivers_n == 0) {
                    System.out.println("No driver satisfies the criteria! Please adjust criteria!");
                } else {
                    int request_id = findLastRequestID() + 1;
                    insertRequest(request_id, id, start_location, destination, model, passengers, driving_years);
                    System.out.printf("Your request is placed. %d drivers are able to take the request.\n", drivers_n);
                }
            } else if (op.equals("2")) {
                System.out.println("Please enter your ID.");
                int id = Integer.parseInt(scanner.nextLine());
                
                System.out.println("Please enter the start date.");
                String start_date = scanner.nextLine();
                
                System.out.println("Please enter the end date.");
                String end_date = scanner.nextLine();
                
                System.out.println("Please enter the destination.");
		String destination = scanner.nextLine();
                
                System.out.println("Trip_id, Driver Name, Vehicle ID, Vehicle Model, Start, End, Fee, Start Location, Destination");
                checkRecords(id, start_date, end_date, destination);
            } else if (op.equals("3")) {
                break;
            } else {
                System.out.println("[ERROR]: Invalid input");
            }
        }
    }
    
    public static void Driver() throws SQLException, ParseException {
        while (true) {
            System.out.println("Driver, what would you like to do?");
            System.out.println("1. Search requests");
            System.out.println("2. Take a request");
            System.out.println("3. Finish a trip");
            System.out.println("4. Go back");	
        
            String op = scanner.nextLine();
            
            if (op.equals("1")){
		System.out.println("Please enter your ID.");
		int driver_id = Integer.parseInt(scanner.nextLine());
                
		System.out.println("Please enter the coordinates of your location.");
                String[] coord = scanner.nextLine().split(" ");
		int driver_location_x = Integer.parseInt(coord[0]);
		int driver_location_y = Integer.parseInt(coord[1]);
                
		System.out.println("Please enter the maximum distance from you to the passenger.");
		int max_distance = Integer.parseInt(scanner.nextLine());
                
                searchRequest(driver_id, driver_location_x, driver_location_y, max_distance);
            } else if (op.equals("2")) {
                System.out.println("Please enter your ID.");
		int driver_id = Integer.parseInt(scanner.nextLine());
                
                System.out.println("Please enter the request ID.");
		int request_id = Integer.parseInt(scanner.nextLine());
                
                System.out.println("Trip ID, Passenger name, Start");
                takeRequest(driver_id, request_id);
            } else if (op.equals("3")) {
                System.out.println("Please enter your ID.");
		int driver_id = Integer.parseInt(scanner.nextLine());
                
                System.out.println("Trip ID, Passenger ID, Start");
                int trip_id = findUnfinishedTrips(driver_id);
                System.out.println("Do you wish to finish the trip? [y/n]");
                String ans = scanner.nextLine();
                
                if (ans.equals("y")) {
                    finishTrip(trip_id);
                } else if (!ans.equals("n")) {
                    System.out.println("[ERROR]: Invalid input");
                }
            } else if (op.equals("4")) {
                break;
            } else {
                System.out.println("[ERROR] Invalid input.");
            }
        }
    }
    
    public static void Manager() throws SQLException {
        while (true) {
            System.out.println("Manager, what would you like to do?");
            System.out.println("1. Find trips");
            System.out.println("2. Go back");
            System.out.println("Please enter[1-2]");
            
            String op = scanner.nextLine();
            
            if (op.equals("1")) {
                System.out.println("Please enter the mimimun traveling distance.");
                int min_dist = Integer.parseInt(scanner.nextLine());
                System.out.println("Please enter the maximum traveling distance.");
                int max_dist = Integer.parseInt(scanner.nextLine());
                
                findFinishedTrips(min_dist, max_dist);
            } else if (op.equals("2")) {
                break;
            } else {
                System.out.println("[ERROR]: Invalid input");
            }
        }
    }

    public static void main(String[] args) throws SQLException, ParseException {
        
        while (true) {
            System.out.println("Welcome! Who are you?");
            System.out.println("1. An adminstrator");
            System.out.println("2. A passenger");
            System.out.println("3. A driver");
            System.out.println("4. A manager");
            System.out.println("5. None of the above");
            System.out.println("Please enter [1-4]");
            
            String role = scanner.nextLine();
        
            if (role.equals("1")) {
                Administrator();
            } else if (role.equals("2")) {
                Passenger();
            } else if (role.equals("3")) {
                Driver();
            } else if (role.equals("4")) {
                Manager();
            } else if (role.equals("5")) {
                break;
            } else{
                System.out.println("[ERROR]: Invalid input");
            }
        }
    }
    
}
