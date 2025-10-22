package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Manages user authentication, registration, and secure storage for the desktop app.
 * Stores user data in a local 'users.json' file.
 */
public class AuthService {

    // Regex for password validation (at least 1 uppercase, 1 lowercase, 1 special char, 8-100 chars)
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,100}$");

    private final File USER_FILE = new File("users.json");
    private final ObjectMapper objectMapper = new ObjectMapper();

    // In-memory store for username -> hashed_password
    private Map<String, String> userStore = new ConcurrentHashMap<>();

    public AuthService() {
        loadUsers();
    }

    /**
     * Loads the user map from users.json on startup.
     */
    private void loadUsers() {
        if (!USER_FILE.exists()) {
            System.out.println("No user file found. A new one will be created on registration.");
            return;
        }
        try {
            userStore = objectMapper.readValue(USER_FILE, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            System.err.println("Failed to load user file: " + e.getMessage());
        }
    }

    /**
     * Saves the current user map to users.json.
     */
    private void saveUsers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(USER_FILE, userStore);
        } catch (IOException e) {
            System.err.println("Failed to save user file: " + e.getMessage());
        }
    }

    /**
     * Registers a new user with validation and password hashing.
     */
    public void register(AuthDtos.RegisterDTO dto) throws IllegalArgumentException {
        String username = dto.username;
        String password = dto.password;

        // 1. Validate username
        if (username == null || username.trim().length() < 5 || username.trim().length() > 30) {
            throw new IllegalArgumentException("Username must be between 5 and 30 characters.");
        }

        // 2. Check for unique username (case-insensitive)
        if (userStore.containsKey(username.trim().toLowerCase())) {
            throw new IllegalArgumentException("Username already taken. Please choose another.");
        }

        // 3. Validate password
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must be 8-100 characters and include at least one uppercase, one lowercase, and one special character (@$!%*?&).");
        }

        // 4. Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 5. Save the user (storing username as lowercase for easy lookup)
        userStore.put(username.trim().toLowerCase(), hashedPassword);
        saveUsers();
    }

    /**
     * Attempts to log in a user.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(AuthDtos.LoginDTO dto) {
        String username = dto.username;
        String password = dto.password;

        if (username == null || password == null) {
            return false;
        }

        // 1. Find the user by lowercase username
        String hashedPassword = userStore.get(username.trim().toLowerCase());
        if (hashedPassword == null) {
            return false; // User not found
        }

        // 2. Securely check the password
        return BCrypt.checkpw(password, hashedPassword);
    }
}
