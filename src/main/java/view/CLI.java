package view;

import controller.PlayersController;
import model.Constant;
import model.Game;
import model.Player;
import model.bets.Bet;
import model.bets.ListBet;
import model.bets.NormalBet;
import model.bets.RangeBet;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);
    private final PlayersController playersController;

    public CLI(PlayersController playersController) {
        this.playersController = playersController;
    }

    public void start() {
        String input = "a";
        while (true) {
            if (!input.isBlank()) {
                System.out.println("'Login' or 'Sign up' or 'exit'");
            }
            input = scanner.nextLine();
            if (input.equals("Login")) {
                login();
            } else if (input.equals("Sign up")) {
                signUp();
            } else if (input.equals("exit")) {
                return;
            } else if (!input.isBlank()){
                System.err.println("Invalid input");
            }
        }
    }

    private void signUp() {
        System.out.println("Enter your name : ");
        String name = scanner.next();
        while (playersController.getPlayer(name) != null) {
            System.err.println("This name has taken plz try another one");
            name = scanner.next();
        }
        System.out.println("Enter your cash");
        String in = scanner.next();
        while (!isValidDouble(in)) {
            System.err.println("Invalid input plz enter a number");
            in = scanner.next();
        }
        double cash = Double.parseDouble(in);
        Player player = new Player(name, cash);
        if (Game.canStart(player)) {
            playersController.addPlayer(player);
            menus(player);
        } else {
            System.err.println("You don't have enough cash to enter");
        }
    }

    public void login() {
        while (true) {
            System.out.println("Enter your name : (or back)");
            String name = scanner.next();
            Player player = playersController.getPlayer(name);
            while (!name.equals("back") && player == null) {
                System.out.println("'" + name + "' not found\nTry another name : ");
                name = scanner.next();
                player = playersController.getPlayer(name);
            }
            if (name.equals("back")) return;
            if (Game.canStart(player)) {
                menus(player);
            } else {
                System.err.println("You don't have enough cash to enter");
            }
        }
    }

    public void menus(Player player) {
        Game game = new Game(player);
        While:
        while (true) {
            if (!game.getBets().isEmpty() || Game.canStart(player)) {
                System.out.println(game.mainMenu());
                int input = getInput(3);
                switch (input) {
                    case 1: //roll
                        processRoll(game);
                        playersController.save();
                        break;
                    case 2: //add bet
                        processAddBet(game);
                        break;
                    case 3: //log out
                        break While;
                }
            } else {
                System.out.println("Game over");
                return;
            }
        }
    }

    private void processRoll(Game game) {
        System.out.println(game.roll());
    }

    private void processAddBet(Game game) {
        if (!Game.canStart(game.getPlayer())) {
            System.err.println("You don't have enough cash to bet");
            return;
        }
        While:
        while (true) {
            System.out.println(game.betsMenu());
            int input = getInput(4);
            switch (input) {
                case 1://normal
                    processAddNormalBet(game, game.getBets(), false);
                    playersController.save();
                    break;
                case 2://range
                    processAddRangeBet(game, game.getBets(), false);
                    playersController.save();
                    break;
                case 3://list
                    processAddListBet(game);
                    playersController.save();
                    break;
                case 4://back
                    break While;
            }
        }
    }

    private void processAddListBet(Game game) {
        LinkedList<Bet> bets = new LinkedList<>();
        While:
        while (true) {
            System.out.println(game.listMenu());
            int input = getInput(4);
            switch (input) {
                case 1://normal
                    processAddNormalBet(game, bets, true);
                    break;
                case 2://range
                    processAddRangeBet(game, bets, true);
                    break;
                case 3://done
                    processAddListBet(game, bets);
                    break While;
                case 4://cancel
                    break While;
            }
        }
    }

    private void processAddListBet(Game game, LinkedList<Bet> bets) {
        if (bets.isEmpty()) {
            System.err.println("You should add some bet to continue");
            return;
        }
        System.out.println("Enter your cost : ");
        double cost = getCost(game);
        ListBet listBet = game.createListBet(cost, bets);
        showMsg(game, listBet);
    }

    private void processAddRangeBet(Game game, LinkedList<Bet> bets, boolean isForList) {
        double cost = 0;
        if (!isForList) {
            System.out.println("Enter your cost : ");
            cost = getCost(game);
        }
        System.out.println("Enter your starter guess : ");
        int start = getGuess();
        System.out.println("Enter your finisher guess : ");
        int end = getGuess();
        RangeBet rangeBet = game.createRangeBet(cost, start, end, bets, isForList);
        showMsg(game, rangeBet);
    }

    private void processAddNormalBet(Game game, LinkedList<Bet> bets, boolean isForList) {
        double cost = 0;
        if (!isForList) {
            System.out.println("Enter your cost : ");
            cost = getCost(game);
        }
        System.out.println("Enter your guess : ");
        int guess = getGuess();
        NormalBet normalBet = game.createNormalBet(cost, guess, bets, isForList);
        showMsg(game, normalBet);
    }

    private void showMsg(Game game, Bet bet) {
        if (bet == null)
            System.err.println("This bet failed to add\nYou don't have enough cash to add this bet");
        else
            System.out.println(bet + " added to your list successfully");
        if (bet instanceof NormalBet) {
            if (((NormalBet) bet).isForList()) {
                return;
            }
        } else if (bet instanceof RangeBet) {
            if (((RangeBet) bet).isForList()) {
                return;
            }
        }
        System.out.println("Your cash is : " + game.getPlayer().getCash());
    }

    private int getInput(int size) {
        String in = scanner.next();
        while (!isValidInput(in, size)) {
            System.err.println("Invalid input plz try again");
            in = scanner.next();
        }
        return Integer.parseInt(in);
    }

    private boolean isValidInput(String input, int size) {
        String regex = String.format("[1-%d]", size);
        return Pattern.matches(regex, input);
    }

    private boolean isValidInt(String input) {
        String regex = "[0-9]+";
        return Pattern.matches(regex, input) && input.charAt(0) != '0';
    }

    private boolean isValidDouble(String input) {
        String regex1 = "[0-9]+\\.[0-9]+";
        String regex2 = "[0-9]+";
        return Pattern.matches(regex1, input) || Pattern.matches(regex2, input);
    }

    private int getGuess() {
        int guess;
        do {
            System.out.println("Plz just enter a number between 1 to " + Constant.RANDOM_SIZE);
            String in = scanner.next();
            while (!isValidInt(in)) {
                System.err.println("Invalid input\nPlz just enter a number between 1 to " + Constant.RANDOM_SIZE);
                in = scanner.next();
            }
            guess = Integer.parseInt(in);
        } while (guess < 1 || guess > Constant.RANDOM_SIZE);
        return guess;
    }

    private double getCost(Game game) {
        double cost = 0;
        while (!game.isValidCost(cost)) {
            System.out.println("Plz enter a number more than 0 and less than " +
                    game.getPlayer().getCash());
            String in = scanner.next();
            while (!isValidDouble(in)) {
                System.err.println("Invalid input\nPlz enter a number more than 0 and less than " +
                        game.getPlayer().getCash());
                in = scanner.next();
            }
            cost = Double.parseDouble(in);
        }
        return cost;
    }
}
