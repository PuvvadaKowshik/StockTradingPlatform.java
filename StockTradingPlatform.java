import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + " - " + name + ": $" + price;
    }
}

class PortfolioItem {
    Stock stock;
    int quantity;

    public PortfolioItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    double getValue() {
        return quantity * stock.price;
    }
}

class User {
    String username;
    double balance;
    Map<String, PortfolioItem> portfolio = new HashMap<>();

    public User(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > balance) {
            System.out.println("❌ Not enough balance to complete this purchase.");
            return;
        }

        balance -= cost;
        portfolio.putIfAbsent(stock.symbol, new PortfolioItem(stock, 0));
        portfolio.get(stock.symbol).quantity += quantity;

        System.out.println("✅ Bought " + quantity + " shares of " + stock.symbol);
    }

    void sellStock(Stock stock, int quantity) {
        if (!portfolio.containsKey(stock.symbol) || portfolio.get(stock.symbol).quantity < quantity) {
            System.out.println("❌ Not enough shares to sell.");
            return;
        }

        portfolio.get(stock.symbol).quantity -= quantity;
        balance += stock.price * quantity;

        if (portfolio.get(stock.symbol).quantity == 0) {
            portfolio.remove(stock.symbol);
        }

        System.out.println("✅ Sold " + quantity + " shares of " + stock.symbol);
    }

    void viewPortfolio() {
        System.out.println("\n📊 Portfolio for " + username + ":");
        if (portfolio.isEmpty()) {
            System.out.println("You don’t own any stocks.");
            return;
        }

        double totalValue = 0;
        for (PortfolioItem item : portfolio.values()) {
            System.out.println(item.stock.symbol + " - " + item.quantity + " shares | Value: $" + item.getValue());
            totalValue += item.getValue();
        }
        System.out.println("💰 Total Portfolio Value: $" + totalValue);
        System.out.println("💵 Available Balance: $" + balance);
    }
}

public class StockTradingPlatform {
    static Scanner sc = new Scanner(System.in);
    static List<Stock> marketStocks = new ArrayList<>();
    static User user;

    public static void main(String[] args) {
        initStocks();
        user = new User("guest", 10000.0); // default balance

        int choice;
        do {
            System.out.println("\n=== 🏦 Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> showMarket();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> user.viewPortfolio();
                case 0 -> System.out.println("Exiting platform. Thank you!");
                default -> System.out.println("❌ Invalid choice.");
            }
        } while (choice != 0);
    }

    static void initStocks() {
        marketStocks.add(new Stock("AAPL", "Apple Inc.", 195.35));
        marketStocks.add(new Stock("GOOG", "Alphabet Inc.", 2745.79));
        marketStocks.add(new Stock("TSLA", "Tesla Motors", 711.20));
        marketStocks.add(new Stock("AMZN", "Amazon.com", 3342.88));
        marketStocks.add(new Stock("INFY", "Infosys Ltd", 1470.00));
    }

    static void showMarket() {
        System.out.println("\n📈 Available Stocks:");
        for (int i = 0; i < marketStocks.size(); i++) {
            System.out.println((i + 1) + ". " + marketStocks.get(i));
        }
    }

    static void buyStock() {
        showMarket();
        System.out.print("Enter stock number to buy: ");
        int index = sc.nextInt() - 1;
        if (index < 0 || index >= marketStocks.size()) {
            System.out.println("❌ Invalid stock choice.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int qty = sc.nextInt();
        if (qty <= 0) {
            System.out.println("❌ Quantity must be positive.");
            return;
        }

        user.buyStock(marketStocks.get(index), qty);
    }

    static void sellStock() {
        showMarket();
        System.out.print("Enter stock number to sell: ");
        int index = sc.nextInt() - 1;
        if (index < 0 || index >= marketStocks.size()) {
            System.out.println("❌ Invalid stock choice.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int qty = sc.nextInt();
        if (qty <= 0) {
            System.out.println("❌ Quantity must be positive.");
            return;
        }

        user.sellStock(marketStocks.get(index), qty);
    }
}
