module com.thelaststand {
    
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires java.logging;

    
    exports Main;
    
    
    exports Scenes;
    exports Button;
    exports Interfaces;
    exports Managers;
    exports Map;
    exports Player;
    exports Constant;
    exports Entities.Enemy;
    exports Entities.Tower;
    exports Input;
    exports Sound;
    
    
    opens Main to javafx.graphics;
    opens Scenes to javafx.graphics;
    exports Player.Skill;
}
