# Hype

A feature-rich Discord bot built with Java and JDA (Java Discord API) for managing server activities, economy system, moderation, and more.

## Features

### 📋 General Commands
- **Ping**: Check the bot's response time
- **Help**: Display available commands
- **Permissions**: Check user permissions
- **Avatar**: Display user's avatar

### 🔧 Configuration
- **Custom Prefixes**: Set custom command prefixes for your server
- **Logging System**: Configure channels for logging server events

### 🎮 Fun & Economy
- **Cash**: View your balance in the economy system
- **GiveCash**: Transfer currency to other users
- **Daily**: Collect daily rewards (with streak tracking)
- **CoinFlip**: Bet your cash on a coin flip
- **Cat/Dog**: View random cat/dog images
- **Meme**: Get random memes

### 📝 Logging
- **Join/Leave Logs**: Track member joins and leaves
- **Log Configuration**: Set up and customize logging options

### 🛡️ Moderation
- **Ban**: Ban users from the server
- **Kick**: Remove users from the server
- **Mute/Unmute**: Temporarily silence users
- **Clean**: Bulk delete messages

## Technology Stack

- **Java 17**: Core programming language
- **JDA (Java Discord API)**: For Discord integration
- **SQLite**: Database for persistent storage of:
  - Server configurations
  - User economy data
  - Custom prefixes
  - Logging settings
- **Maven**: Dependency management
- **dotenv**: Environment variable management for secure token storage
- **Logback**: Advanced logging capabilities

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Discord bot token (from the [Discord Developer Portal](https://discord.com/developers/applications))

### Installation

1. Clone this repository
   ```
   git clone https://github.com/R-Harshith/hype.git
   cd hype
   ```

2. Create a `.env` file in the root directory based on the provided `.env.example`
   ```
   TOKEN=your_discord_bot_token_here
   ```

3. Build the project using Maven
   ```
   mvn clean package
   ```

4. Run the bot
   ```
   java -jar target/hype-1.0-SNAPSHOT.jar
   ```

### Default Configuration

- Default command prefix: `!`
- Type `/ping` to test if the bot is working

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
