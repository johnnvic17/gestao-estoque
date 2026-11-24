package application;

import db.DB;
import model.dao.CategoryDao;
import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;
import services.CategoryService;
import services.ProductService;
import utils.GenerateProductCode;
import utils.GenerateRandomNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        ProductDao productDao = DaoFactory.createProductDao();

        ProductService productService = new ProductService(productDao);

        System.out.println("------- Gestão de produtos -------");

        inputMenu(productService, sc);

        sc.close();
        DB.closeConnection();
    }

    public static void inputMenu(ProductService productService, Scanner sc) {
        System.out.println("\n1 - Cadastrar produto");
        System.out.println("2 - Buscar produto pelo ID");
        System.out.println("3 - Atualizar produto");
        System.out.println("4 - Filtrar produtos (Nome, Preço e etc.)");
        System.out.println("5 - Listar produtos");
        System.out.println("6 - Remover produto");
        System.out.println("0 - Sair");

        int op;
        int id;
        Product product;
        List<Product> products;

        CategoryDao categoryDao = DaoFactory.createCategoryDao();
        CategoryService categoryService = new CategoryService(categoryDao);

        do {
            System.out.print("\nEscolha uma opção: ");
            op = sc.nextInt();

            try {
                switch (op) {
                    case 1:
                        product = createProduct(productService, categoryService, new Product(), sc);
                        productService.insertProduct(product);

                        System.out.println("Produto cadastrado!");
                        break;
                    case 2:
                        System.out.print("ID do produto: ");
                        id = sc.nextInt();

                        product = productService.findById(id);

                        if (product == null) {
                            System.out.println("Produto não encontrado!");

                        } else {
                            System.out.println(product);

                            if (product.getQuantity() <= 10) {
                                System.out.println("AVISO: Pouca quantidade em estoque!");
                            }
                        }
                        break;
                    case 3:
                        System.out.print("ID do produto a ser atualizado: ");
                        id = sc.nextInt();

                        product = productService.findById(id);

                        if (product == null) {
                            System.out.println("Produto não encontrado!");

                        } else {
                            Product updatedProduct = createProduct(productService, categoryService, product, sc);
                            productService.updateProduct(updatedProduct);

                            System.out.println("Produto atualizado!");
                        }
                        break;
                    case 4:
                        products = inputFilterMenu(productService, sc);

                        if (products.isEmpty()){
                            System.out.println("Nenhum produto encontrado!");
                        } else {
                            for (Product p : products) {
                                System.out.println(p);
                            }
                        }
                        break;
                    case 5:
                        products = productService.findAll();

                        if (products.isEmpty()) {
                            System.out.println("Nenhum produto encontrado!");
                        } else {
                            for (Product p : products) {
                                System.out.println(p);
                            }
                        }
                        break;
                    case 6:
                        System.out.print("ID do produto a ser removido: ");
                        id = sc.nextInt();
                        productService.deleteProduct(id);

                        System.out.println("Produto removido!");
                        break;
                    case 0:
                        System.out.println("Programa encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida...");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    public static List<Product> inputFilterMenu(ProductService productService, Scanner sc) {
        System.out.println("\n1 - Filtrar produtos por nome");
        System.out.println("2 - Filtrar produtos maior que x");
        System.out.println("3 - Filtrar produtos que seja menor que x");

        System.out.print("\nOpção de filtro: ");
        int op;
        double price;
        List<Product> products = new ArrayList<>();

        op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                System.out.print("Nome dos produtos: ");
                String name = sc.nextLine();

                products = productService.findByName("%" + name + "%");
                break;
            case 2:
                System.out.print("Filtrar produtos com preço maior que: ");
                price = sc.nextInt();

                products = productService.findByPriceGraterThan(price);
                break;
            case 3:
                System.out.println("Filtrar produtos com preço menor que: ");
                price = sc.nextInt();

                products = productService.findByPriceLessThan(price);
                break;
            default:
                System.out.println("Opção inválida...");
        }
        return products;
    }

    public static Product createProduct(ProductService productService, CategoryService categoryService, Product product, Scanner sc) {
        sc.nextLine();
        System.out.print("Nome do produto: ");
        String name = sc.nextLine();
        product.setName(name);

        System.out.print("Preço: ");
        Double price = sc.nextDouble();
        product.setPrice(price);

        System.out.print("Quantidade: ");
        Integer quantity = sc.nextInt();
        product.setQuantity(quantity);


        if (product.getCategory() == null) {
            sc.nextLine();

            System.out.print("Categoria: ");
            String categoryName = sc.nextLine().trim();

            Category category = categoryService.findByName(categoryName);

            if (category == null) {
                categoryService.insertCategory(new Category(null, categoryName));

                category = categoryService.findByName(categoryName);
            }
            product.setCategory(category);


            if (product.getCode() == null) {

                String code = generateUniqueCode(categoryName, productService);
                product.setCode(code);
            }
        }
        return product;
    }

    public static String generateUniqueCode(String categoryName, ProductService productService) {
        int number = GenerateRandomNumber.generateNumber();

        String code = GenerateProductCode.generateCode(categoryName, number);

        List<String> codes = productService.getProductCodes();

        while (codes.contains(code)) {
            number = GenerateRandomNumber.generateNumber();
            code = GenerateProductCode.generateCode(categoryName, number);
        }
        return code;
    }
}