package game.systems;

import client.HpBar;
import client.animations.Animation;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
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
    private final TextGraphics uiGraphics;
    private final HpBar hpBar;
    private final TextGraphics terrainGraphics;
    private Supplier<Stream<Player>> streamSupplier;
    private Supplier<Stream<NPC>> npcSupplier;

    private List<Animation> animations;

    private Screen screen;



    public RenderSystem(Screen screen, Supplier<Stream<Player>> positions, Player getPlayer, Supplier<Stream<NPC>> npcSupplier, List<Animation> animations) throws IOException {
        this.streamSupplier = positions;
        this.npcSupplier = npcSupplier;
        this.animations = animations;


        this.screen = screen;
        this.playerGraphics = screen.newTextGraphics();
        this.uiGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.terrainGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.BLACK).setBackgroundColor(TextColor.ANSI.WHITE);
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
        npcSupplier.get().forEach(n -> terrainGraphics.setCharacter(n.getX(), n.getY(), '@'));
    }

    private void renderAnimations(){
        for (Animation a : animations){
            a.renderWith(uiGraphics);
        }
    }


    private void renderTerrain() {
        terrainGraphics.drawLine(0, 0, 0, 23, 'E');
        terrainGraphics.drawLine(0, 23, 40, 23, '@');
    }

    private void renderUI() {
        uiGraphics.putString(HpBar.POSITION, hpBar.getString());
    }
}
