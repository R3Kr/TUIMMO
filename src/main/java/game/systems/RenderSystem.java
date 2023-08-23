package game.systems;

import client.CooldownBar;
import client.HpBar;
import client.animations.Animation;
import client.animations.BlockAnimation;
import client.animations.RegenAnimation;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import game.components.NPC;
import game.components.Player;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RenderSystem implements System{
    private final TextGraphics playerGraphics;
    private final TextGraphics redGraphics;
    private final TextGraphics whiteGraphics;
    private final TextGraphics grayGraphics;
    private final TextGraphics greenGraphics;
    private final HpBar hpBar;
    private final CooldownBar cdBar;
    private final TextGraphics terrainGraphics;
    private Supplier<Stream<Player>> streamSupplier;
    private Supplier<Stream<NPC>> npcSupplier;

    private List<Animation> animations;

    private Screen screen;



    public RenderSystem(Screen screen, Supplier<Stream<Player>> positions, Player getPlayer, Supplier<Stream<NPC>> npcSupplier, List<Animation> animations, CooldownBar cdBar) throws IOException {
        this.streamSupplier = positions;
        this.npcSupplier = npcSupplier;
        this.animations = animations;
        this.cdBar = cdBar;


        this.screen = screen;
        this.playerGraphics = screen.newTextGraphics();
        this.redGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.whiteGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        this.grayGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.WHITE);
        this.terrainGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.BLACK).setBackgroundColor(TextColor.ANSI.WHITE);
        this.greenGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.GREEN);
        this.hpBar = new HpBar(getPlayer);

        init();

    }

    @Override
    public void update(){

        try {
            screen.refresh();
        }
        catch (Exception e){
            java.lang.System.out.println(e);
        }
        screen.clear();

        renderAnimations();
        renderPlayer();
        renderUI();
        renderTerrain();

    }

    private void init() throws IOException {
        screen.startScreen();
        screen.clear();
        playerGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        playerGraphics.setBackgroundColor(TextColor.ANSI.BLUE);
    }

    private void renderPlayer(){
        streamSupplier.get().forEach(p -> playerGraphics.putString(p.getX(), p.getY(), p.getName()));
        npcSupplier.get().forEach(n -> {
            if (n.getState().equals("neutral")){
                terrainGraphics.setCharacter(n.getX(), n.getY(), '@');
            }
            else {
                redGraphics.setCharacter(n.getX(), n.getY(), '@');
            }
        });
    }

    private void renderAnimations(){
        for (Animation a : animations){
            if (a instanceof BlockAnimation){
                a.renderWith(whiteGraphics);
            } else if (a instanceof RegenAnimation) {
                a.renderWith(greenGraphics);
            } else {
                a.renderWith(redGraphics);
            }
        }
    }


    private void renderTerrain() {
        terrainGraphics.putString(38, 24, "↓↓");
        terrainGraphics.putString(38, 0, "↑↑");
        terrainGraphics.putString(0, 12, "←");
        terrainGraphics.putString(79, 12, "→");

    }

    private void renderUI() {
        redGraphics.putString(HpBar.POSITION, hpBar.getString());
        cdBar.renderWith(whiteGraphics, grayGraphics);
    }
}
