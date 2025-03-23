package controller;

import gui.HomePage;

public class Controller {
    private HomePage homePage;

    public Controller() {
        homePage = new HomePage();
    }

    public void start() {
        homePage.setVisible(true);
    }
}
