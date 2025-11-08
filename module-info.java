module com.thelaststand {
    
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    
    
    exports Main;
    
    
    exports Scene;
    exports UI.Button;
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
    opens Scene to javafx.graphics;
}
