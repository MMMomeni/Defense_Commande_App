package com.example.defensecommanderapp;

import android.view.View;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GetScoresRunnable implements Runnable {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final ScoreBoardActivity context;
    private static String dbURL = "jdbc:mysql://christopherhield.com:3306/chri5558_missile_defense";
    ;
    private Connection conn;
    private static final String STUDENT_TABLE = "AppScores";
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
    private boolean insert;
    private String initials;
    private int score;
    private int level;
    private boolean inTop10 = false;
    private boolean dialog = true;


    GetScoresRunnable(ScoreBoardActivity ctx, String initials, int score, int level, boolean bln) {
        this.context = ctx;
        this.insert = bln;
        this.initials = initials;
        this.score = score;
        this.level = level;
    }

    public void run() {

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(dbURL, "chri5558_student", "ABC.123");

            StringBuilder sb = new StringBuilder();

            if (insert) {

                Statement stmt = conn.createStatement();

                String sql = "INSERT INTO " + STUDENT_TABLE + " VALUES (" +
                        System.currentTimeMillis() + ", '" + initials + "', " + score + ", " + level
                        +
                        ")";

                stmt.executeUpdate(sql);

                stmt.close();
                inTop10 = false;
                dialog = false;

            }


            String response = "#   " + "Init   " + "Level   " + "Score   " + "Date/Time\n";

            sb.append(response);
            sb.append(getAll());
            context.setResults(sb.toString());
            conn.close();
            if (inTop10 && dialog){
                context.runOnUiThread(() ->
                        context.infoDialog(null));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getAll() throws SQLException {
        Statement stmt = conn.createStatement();

        String sql = "SELECT * FROM " + STUDENT_TABLE + " ORDER BY Score DESC LIMIT 10";

        StringBuilder sb = new StringBuilder();

        ResultSet rs = stmt.executeQuery(sql);
        int counter = 0;
        while (rs.next()) {
            counter++;
            long millisT = rs.getLong(1);
            String initsT = rs.getString(2);
            int scoreT = rs.getInt(3);
            int levelT = rs.getInt(4);
            if (score > scoreT)
                inTop10 = true;


            sb.append(String.format(Locale.getDefault(),
                    "%-5s %-6s %-5s %-9s %12s%n", counter, initsT, levelT, scoreT, sdf.format(new Date(millisT))));
        }
        rs.close();
        stmt.close();


        return sb.toString();
    }




}
