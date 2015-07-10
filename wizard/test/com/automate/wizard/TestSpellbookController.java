package com.automate.wizard;

import java.util.Scanner;

public class TestSpellbookController implements TestSpellbook {
    
    private final TestSpellbook spellbook;
    private final Scanner scanner;

    public TestSpellbookController(final TestSpellbook spellbook, final Scanner scanner) {
        this.spellbook = spellbook;
        this.scanner = scanner;
    }

    @Override
    @Start @Next("age")
    public String name() {
        return scanner.nextLine();
    }

    @Override
    @Next("print")
    public int age() {
        return scanner.nextInt();
    }

    @Override
    public void print() {
        System.out.format("Name: %s, Age: %d%n", spellbook.name(), spellbook.age());
    }

}
