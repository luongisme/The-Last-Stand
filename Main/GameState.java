package Main;

public enum GameState {
    PLAYING, MENU, SETTINGS, GAME_OVER;

    public static GameState gameState = MENU;

    public static void SetGameState(GameState state){
        gameState = state;
    }
}
