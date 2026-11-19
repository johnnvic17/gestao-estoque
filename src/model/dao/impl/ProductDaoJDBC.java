package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private static Connection conn;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product product) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO product (Name, Price, Code, Quantity, CategoryId) "
                    + "VALUES (?, ?, ?, ?, ?)";

            st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());
            st.setString(3, product.getCode());
            st.setInt(4, product.getQuantity());
            st.setInt(5, product.getCategory().getId());

            int row = st.executeUpdate();

            if (row > 0) {
                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    product.setId(id);
                }
                DB.closeResultSet(rs);
            }
            conn.commit();
        } catch (SQLException e) {

            if (conn != null) {
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
    public void update(Product product) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);

            String sql = "UPDATE product "
                    + "SET Name = ?, Price = ?, Code = ?, Quantity = ?, CategoryId = ? "
                    + "WHERE Id = ?";

            st = conn.prepareStatement(sql);

            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());
            st.setString(3, product.getCode());
            st.setInt(4, product.getQuantity());
            st.setInt(5, product.getCategory().getId());
            st.setInt(6, product.getId());

            st.executeUpdate();

            conn.commit();
        } catch (SQLException e){

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
    public void delete(Integer id) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);

            String sql = "DELETE FROM product WHERE Id = ?";

            st = conn.prepareStatement(sql);

            st.setInt(1 , id);

            st.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
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
    public Product findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT product.*, category.Name as CatName "
                    + "FROM product INNER JOIN category "
                    + "ON product.CategoryId = category.Id "
                    + "WHERE product.Id = ?";

            st = conn.prepareStatement(sql);

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                Category cat = instantiateCategory(rs);
                return instantiateProduct(rs, cat);
            }
            return null;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findAll() {

        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();

            String sql = "SELECT product.*, category.Name as CatName "
                    + "FROM product INNER JOIN category "
                    + "ON product.CategoryId = category.Id";

            rs = st.executeQuery(sql);

            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                Category cat = instantiateCategory(rs);
                Product prod = instantiateProduct(rs, cat);
                products.add(prod);
            }
            return products;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<String> getProductCodes() {

        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();

            String sql = "SELECT Code FROM product";

            rs = st.executeQuery(sql);

            List<String> codes = new ArrayList<>();

            while (rs.next()) {
                String code = rs.getString(1);
                codes.add(code);
            }
            return codes;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());

        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findByName(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT product.*, category.Name as CatName "
                    + "FROM product INNER JOIN category ON product.CategoryId = category.Id "
                    + "WHERE product.Name LIKE ?";

            st = conn.prepareStatement(sql);

            st.setString(1, name);

            rs = st.executeQuery();

            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                Category cat = instantiateCategory(rs);
                Product prod = instantiateProduct(rs, cat);
                products.add(prod);
            }
            return products;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());

        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findByPriceGreaterThan(Double price) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT product.*, category.Name as CatName "
                    + "FROM product INNER JOIN category ON product.CategoryId = category.Id "
                    + "WHERE product.Price > ?";

            st = conn.prepareStatement(sql);

            st.setDouble(1, price);

            rs = st.executeQuery();

            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                Category cat = instantiateCategory(rs);
                Product prod = instantiateProduct(rs, cat);
                products.add(prod);
            }
            return products;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findByPriceLessThan(Double price) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT product.*, category.Name as CatName "
                    + "FROM product INNER JOIN category ON product.CategoryId = category.Id "
                    + "WHERE product.Price < ?";

            st = conn.prepareStatement(sql);

            st.setDouble(1, price);

            rs = st.executeQuery();

            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                Category cat = instantiateCategory(rs);
                Product prod = instantiateProduct(rs, cat);
                products.add(prod);
            }
            return products;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private static Product instantiateProduct(ResultSet rs, Category cat) throws SQLException {
        Product product = new Product();

        product.setId(rs.getInt("Id"));
        product.setName(rs.getString("Name"));
        product.setPrice(rs.getDouble("Price"));
        product.setCode(rs.getString("Code"));
        product.setQuantity(rs.getInt("Quantity"));
        product.setCategory(cat);

        return product;
    }

    private static Category instantiateCategory(ResultSet rs) throws SQLException {

        Category category = new Category();

        category.setId(rs.getInt("CategoryId"));
        category.setName(rs.getString("CatName"));

        return category;
    }
}
