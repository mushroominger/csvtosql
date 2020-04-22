import java.util.*;
import java.io.*;

public class CsvToInsert {

    private final static int table_name_row = 0;
    private final static int table_name_col = 1;

    private final static int start_data_row = 3;

    private final static ArrayList<String> col_type = new ArrayList<String>(Arrays.asList(
        "int",
        "integer",
        "smallint",
        "float",
        "real",
        "double",
        "doubleprecision",
        "number",
        "decimal"
    ));

    public static void main(String[] args) {
        ArrayList<String> paths = xx();

        for (String path : paths) {

            try {
                File file = new File(path);

                ArrayList<ArrayList<String>> al1 = new ArrayList<ArrayList<String>>();
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String data = new String();
                while ((data = bufferedReader.readLine()) != null) {
                    // String[] test = data.split(",");
                    ArrayList<String> al2 = new ArrayList<String>(Arrays.asList(data.split(",")));
                    al1.add(al2);
                }

                for(int i=start_data_row; i<al1.size(); i++) {
                    for(int j=1; j<al1.get(i).size(); j++) {
                        String tmp = new String();
                        if(col_type.contains(al1.get(1).get(j).toLowerCase())) {
                            tmp = al1.get(i).get(j);
                        } else {
                            tmp = "'" + al1.get(i).get(j) + "'";
                        }
                        al1.get(i).set(j, tmp);
                    }
                    al1.get(i).remove(0);
                }

                al1.get(2).remove(0);
                String names = String.join(", ", al1.get(2));

                String intro = new String();
                intro = "INSERT INTO " + al1.get(table_name_row).get(table_name_col) + " (" + names + ") VALUES\n";

                ArrayList<String> al3 = new ArrayList<String>(Arrays.asList(intro));

                for(int i=3; i<al1.size(); i++) {
                    String substance = String.join(", ", al1.get(i));
                    substance = "(" + substance + ")";
                    if(al1.size() == i + 1){
                        substance = substance + ";\n";
                        al3.add(substance);
                    } else {
                        substance = substance + ",\n";
                        al3.add(substance);
                    }
                }

                FileWriter fileWriter = new FileWriter(path.replace(".csv", ".sql"));
                for (int i=0; i<al3.size(); i++) {
                    fileWriter.write(al3.get(i));
                }

                fileWriter.close();
                fileReader.close();

                System.out.println("変換に成功しました");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static ArrayList<String> path_getter() {
        System.out.println("変換したいファイルのパスを入力してください \\qで入力終了");
        ArrayList<String> fpal = new ArrayList<>();
        String line = new String();

        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            line = scan.next();
            if (line.equals("\\q")) {
                System.out.println("入力を終了します");
                break;
            } else {
                try {
                    String tmp_path = new File(line).getCanonicalPath();
                    File inFile = new File(tmp_path);
                    if (!inFile.exists()) {
                        System.out.println("ファイルが見つかりません");
                        continue;
                    } else {
                        fpal.add(inFile.toString());
                        System.out.println(fpal);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        scan.close();
        return fpal;
    }

    private static ArrayList<String> xx() {
        System.out.println("test drive");
        ArrayList<String> fpal = new ArrayList<>();
        String line = new String();

        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            line = scan.next();
            if (line.equals("\\q")) {
                System.out.println("入力を終了します");
                break;
            } else if (line.indexOf(".csv") != -1) {
                System.out.println("ファイルかも");
                System.out.println(line);
                try {
                    String tmp_path = new File(line).getCanonicalPath();
                    File inFile = new File(tmp_path);
                    if (!inFile.exists()) {
                        System.out.println("ファイルが見つかりません");
                        continue;
                    } else {
                        fpal.add(inFile.toString());
                        System.out.println(fpal);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("フォルダかも");
                System.out.println(line);
                File dir = new File(line);
                File[] list = dir.listFiles();

                if (list != null) {
                    for (File item : list) {
                        try {
                            String tmp_file = item.getCanonicalPath();
                            if (tmp_file.indexOf(".csv") != -1) {
                                System.out.println(tmp_file);
                                fpal.add(tmp_file);
                            } else {
                                System.out.println("csvファイルではありません");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("空っぽです");
                }
                break;
            }
        }
        scan.close();
        return fpal;
    }
}
