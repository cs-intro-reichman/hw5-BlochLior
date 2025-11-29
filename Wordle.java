public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
		In in = new In("dictionary.txt");
        return in.readAllStrings();
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
		int randomInt = (int)(Math.random() * dict.length);
        return dict[randomInt];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
		return (secret.indexOf(c) != -1);
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
		String processedGuess = preProcessString(guess);
        for (int i = 0; i < processedGuess.length(); i++) {
            char ch = processedGuess.charAt(i);
            if (containsChar(secret, ch)) {
                if (secret.charAt(i) == ch) {
                    resultRow[i] = 'G';
                } else {
                    resultRow[i] = 'Y';
                }
                
            } else {
                resultRow[i] = '_';
            }
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
		String processedGuess = preProcessString(guess);
        
        for (int i = 0; i < guesses[row].length; i++) {
            char ch = processedGuess.charAt(i);
            guesses[row][i] = ch;
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
		for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true;
    }

    public static String preProcessString(String guess) {
        String result = "";
        for (int i = 0; i < guess.length(); i++) {
            char ch = guess.charAt(i);
            result += preProcessChar(ch);
        }
        return result;
    }

    public static char preProcessChar(char ch) {
        char newCh = ch;
        if (ch >= 'a' && ch <= 'z') {
            newCh = (char)(ch - 'a' + 'A');
        }
        return newCh;
    }
    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[6][WORD_LENGTH];
        char[][] results = new char[6][WORD_LENGTH];

        // Prepare to read from the standart input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString();
                String processedGuess = preProcessString(guess);
                
                if (guess.length() != 5) {
                    System.out.println("Invalid word. Please try again." + " processed guess is:" + processedGuess);
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret ward was: " + secret);
        }

        inp.close();
    }
}
