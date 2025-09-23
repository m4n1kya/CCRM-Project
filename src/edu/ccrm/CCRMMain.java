package edu.ccrm;

import edu.ccrm.cli.CLIInterface;
import edu.ccrm.config.AppConfig;

public class CCRMMain {
    public static void main(String[] args) {
        System.out.println("=== Campus Course & Records Manager ===");
        
        // Singleton instance
        AppConfig config = AppConfig.getInstance();
        config.loadConfiguration();
        
        CLIInterface cli = new CLIInterface();
        cli.start();
    }
}