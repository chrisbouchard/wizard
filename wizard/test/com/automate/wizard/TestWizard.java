package com.automate.wizard;

import java.util.Scanner;

public class TestWizard {
    
    public static void main(final String... args) {
        final Scanner scanner = new Scanner(System.in);
        final Wizard<TestSpellbook> wizard = new Wizard<>(TestSpellbook.class, TestSpellbookController.class);
        wizard.run(scanner);
    }

}
