package com.master.dao;

import com.master.db.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TaskPlotterDao {

    private final DbConnection conn;

    public TaskPlotterDao(DbConnection conn) {
        this.conn = conn;
    }

}
