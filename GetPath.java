package com.company;
import java.io.*;
import java.util.*;


public class GetPath {
    // airport codes of arrival & departure point(s)
    private ArrayList<String> start_airports = new ArrayList<>();
    private ArrayList<String> goal_destination_airports = new ArrayList<>();

    private ArrayList<String> route_information = new ArrayList<>();// destination path
    private Hashtable<String, String> path = new Hashtable<>();


    // validity determinant of destination path
    private boolean validity = true;

    private ArrayList<String> route = new ArrayList<>(); // routes.csv data

    public Scanner input;
    public Scanner input_1;
    public Scanner input_2;

    private int index; // index of destination point in goal_destination_airports
    private File input_info; // input file
    private File output_info; // output file

    GetPath() {
        try {
            // absolute path of airports.csv and routes.csv
            File airports = new File("C:\\Users\\gbiko\\OneDrive\\Desktop\\airports.csv");
            File routes = new File("C:\\Users\\gbiko\\OneDrive\\Desktop\\routes.csv");

            // creating input and output files
            this.input_info = new File("flight_data.txt");
            this.output_info = new File("flight_data_output.txt");

            // reading our database files
            this.input = new Scanner(airports);
            this.input_1 = new Scanner(routes);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // writing input data
    public void set_input_data(String start_information, String end_information) {
        try {
            if(this.input_info.createNewFile() || !this.input_info.createNewFile()) {
                FileWriter writer = new FileWriter(input_info.getName());
                writer.write(start_information + "\n");
                writer.write(end_information);
                writer.close();
            }
            this.input_2 = new Scanner(input_info);
            this.set_start_goal();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    // displaying input data file information
    public void viewStart_information(String start_information, String end_information){
        System.out.println();
        System.out.println("Input file data:");
        System.out.println(start_information);
        System.out.println(end_information);
    }

    // determining possible arrival & departure airport codes
    public void set_start_goal() {
        String start_information = this.input_2.nextLine();
        String end_information = this.input_2.nextLine();
        this.viewStart_information(start_information, end_information);
        String[] departure = start_information.split(",");
        String[] arrival = end_information.split(",");
        while (this.input.hasNextLine()) {
            String line = this.input.nextLine();
            String[] words = line.split(",");
            if ((words[2].equals(departure[0])) & (words[3].equals(departure[1]))) {
                this.start_airports.add(words[4]);
            }
            if ((words[2].equals(arrival[0])) & (words[3].equals(arrival[1]))) {
                this.goal_destination_airports.add(words[4]);
            }
        }
        if (this.start_airports.size() == 0 || this.goal_destination_airports.size() == 0) {
            this.validity = false;
        }
        input.close();
        input_2.close();
    }

    // getting routes.csv data
    public void setRoute() {
        while (this.input_1.hasNextLine()){
            String line = input_1.nextLine();
            this.route.add(line);
        }
        input_1.close();
    }

    // determining destination path
    public void find_goal_path() {
        ArrayList<String> airport = new ArrayList<>(this.start_airports);
        this.setRoute();
        while (airport.size() != 0 & this.validity) {
            int initial_size = airport.size();
            boolean state = false;
            for (String path : this.route) {
                String[] line = path.split(",");
                if (airport.contains(line[2]) & !airport.contains(line[4])) {
                    if (this.goal_destination_airports.contains(line[4])) {
                        this.path.put(line[4], line[2] + "," + line[0] + "," + line[7]);
                        this.index = this.goal_destination_airports.indexOf(line[4]);
                        state = true;
                        this.getRoute_information();
                        break;
                    }
                    airport.add(line[4]);
                    this.path.put(line[4], line[2] + "," + line[0] + "," + line[7]);
                }
            }
            if (state) {
                break;
            }
            if (airport.size() == initial_size){
                this.validity = false;
            }
        }
    }

    // getting destination path
    public void getRoute_information() {
        String destination = this.goal_destination_airports.get(this.index);
        while (!this.start_airports.contains(destination)) {
            for (String key : this.path.keySet()) {
                if (key.equals(destination)) {
                    String source = this.path.get(key).split(",")[0];
                    this.route_information.add(key + "," + path.get(key));
                    destination = source;
                    break;
                }
            }
        }
        Collections.reverse(route_information);
    }

    // displaying output file data
    public void getOutput_info() {
        try {
            Scanner output_file =  new Scanner(output_info);
            System.out.println();
            System.out.println("Output file data:");
            while(output_file.hasNextLine()) {
                System.out.println(output_file.nextLine());
            }
            output_file.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void get_output_data() {
        try {
            int stops = 0;
            if (this.output_info.createNewFile()|| !this.output_info.createNewFile()){
                FileWriter writer = new FileWriter(this.output_info.getName());
                this.find_goal_path();
                if(this.validity){
                    for(String line : this.route_information){
                        String[] array = line.split(",");
                        stops = stops + Integer.parseInt(array[3]);
                        writer.write(array[2] + " from "+ array[1] + " to "+ array[0] + " "+array[3] + " stops"
                                +"\n");
                    }
                    writer.write("Total flights:" + this.route_information.size()+"\n");
                    writer.write("Total additional stops:"+stops+"\n");
                }
                else{
                    writer.write("No valid route available");
                }
                writer.close();
            }
            this.getOutput_info();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String start;
        String end;
        GetPath obj = new GetPath();
        try {
            Scanner user_input = new Scanner(System.in);
            System.out.println("Enter your start city and country separated by comma e.g: Accra,Ghana:");
            start = user_input.nextLine();
            System.out.println("Enter your destination city and country separated by comma e.g: Kigali,Rwanda:");
            end = user_input.nextLine();
            obj.set_input_data(start,end);
            obj.get_output_data();
            user_input.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
        




