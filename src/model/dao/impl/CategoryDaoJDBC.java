package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.CategoryDao;
import model.entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoJDBC implements CategoryDao {

    private static Connection conn;

    public CategoryDaoJDBC (Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Category category) {
        PreparedStatement st = null;

        try{
            conn.setAutoCommit(false);

            String sql = "INSERT INTO category (Name) VALUES (?)";

            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, category.getName());

            int row = st.executeUpdate();

            if (row > 0){
                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()){
                    int id = rs.getInt(1);
                    category.setId(id);
                }
                DB.closeResultSet(rs);
            }
            conn.commit();

        }catch (SQLException e){

            if (conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DBException(ex.getMessage());
                }
            }

            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Category findByName(String name) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM category WHERE category.Name LIKE ?";

            st = conn.prepareStatement(sql);

            st.setString(1, name);

            rs = st.executeQuery();

            if (rs.next()){
                Category category = new Category();
                category.setId(rs.getInt("Id"));
                category.setName(rs.getString("Name"));
                return category;
            }
            return null;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
