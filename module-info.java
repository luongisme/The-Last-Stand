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
    exports Entities.Enemies;
    exports Entities.Towers;
    exports Input;
    exports Sound;
    exports Logic.Effects;
    exports Logic.Strategies;

    opens Main to javafx.graphics;
    opens Scenes to javafx.graphics;
    exports Player.Skill;
    exports Managers.Tower;
    exports Helper;
    exports Helper.LoadImages;
    exports Entities.Projectiles;
    exports Entities.AnimationEffects;
}
