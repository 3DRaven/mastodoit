package com.anyqn.mastodon.mastosync.repostitories;

import java.util.Scanner;

public class UserInputRepository {
    private static final Scanner in = new Scanner(System.in);

    public String getNextLine() {
        return in.nextLine();
    }
}
