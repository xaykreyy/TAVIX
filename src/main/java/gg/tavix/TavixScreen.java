package gg.tavix;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.*;

public final class TavixScreen extends Screen {
    private static final int BG = 0xF20B0C16;
    private static final int PANEL = 0xFF111322;
    private static final int PANEL_HOVER = 0xFF1B1D32;
    private static final int LINE = 0xFF292B47;
    private static final int PURPLE = 0xFF9B5CFF;
    private static final int BLUE = 0xFF5A7CFF;
    private static final int WHITE = 0xFFF2F3FF;
    private static final int MUTED = 0xFF969BB9;

    private final List<String> categories = List.of("Combat", "Movement", "Render", "Player", "Misc");
    private final Map<String, List<ModuleEntry>> modules = new LinkedHashMap<>();
    private final Set<String> enabled = new HashSet<>();
    private String category = "Render";
    private String search = "";
    private long openedAt = System.currentTimeMillis();

    public TavixScreen() {
        super(Text.literal("TAVIX"));
        modules.put("Combat", List.of(
            new ModuleEntry("Aim Assist", "Configuration slot / test UI", "COMBAT"),
            new ModuleEntry("Auto Clicker", "Configuration slot / test UI", "COMBAT"),
            new ModuleEntry("Reach", "Configuration slot / test UI", "COMBAT")
        ));
        modules.put("Movement", List.of(
            new ModuleEntry("Sprint", "Client-side sprint helper", "MOVE"),
            new ModuleEntry("Step", "Configuration slot / test UI", "MOVE"),
            new ModuleEntry("Velocity", "Configuration slot / test UI", "MOVE")
        ));
        modules.put("Render", List.of(
            new ModuleEntry("Fullbright", "Visual settings slot", "VISUAL"),
            new ModuleEntry("HUD Editor", "Customize the on-screen interface", "VISUAL"),
            new ModuleEntry("Notifications", "Show module state notifications", "VISUAL")
        ));
        modules.put("Player", List.of(
            new ModuleEntry("Auto Respawn", "Convenience setting slot", "PLAYER"),
            new ModuleEntry("Coordinates", "Show coordinates in HUD", "PLAYER")
        ));
        modules.put("Misc", List.of(
            new ModuleEntry("Config Manager", "Preset and settings slot", "MISC"),
            new ModuleEntry("Sounds", "UI click sounds toggle", "MISC")
        ));
        enabled.add("HUD Editor");
        enabled.add("Notifications");
        enabled.add("Sounds");
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0x99000000);
        int w = Math.min(760, width - 28);
        int h = Math.min(450, height - 28);
        int x = (width - w) / 2, y = (height - h) / 2;
        ctx.fill(x, y, x + w, y + h, BG);
        ctx.fill(x, y, x + w, y + 52, PANEL);
        ctx.fill(x, y + 51, x + w, y + 52, LINE);
        ctx.fill(x + 17, y + 12, x + 46, y + 41, PURPLE);
        ctx.fill(x + 20, y + 15, x + 43, y + 38, BLUE);
        ctx.drawTextWithShadow(textRenderer, "T", x + 27, y + 20, WHITE);
        ctx.drawTextWithShadow(textRenderer, "TAVIX", x + 57, y + 12, WHITE);
        ctx.drawTextWithShadow(textRenderer, "CLIENT  /  1.21.4", x + 58, y + 29, MUTED);
        ctx.drawTextWithShadow(textRenderer, "BETA", x + w - 49, y + 21, PURPLE);

        int sideW = 142;
        ctx.fill(x, y + 52, x + sideW, y + h, 0xFF0D0E1A);
        ctx.fill(x + sideW, y + 52, x + sideW + 1, y + h, LINE);
        ctx.drawText(textRenderer, "CATEGORIES", x + 15, y + 69, MUTED, false);
        int ny = y + 91;
        for (String c : categories) {
            boolean active = c.equals(category);
            int bx = x + 9, by = ny - 5, bw = sideW - 18, bh = 27;
            if (active) {
                ctx.fill(bx, by, bx + bw, by + bh, 0xFF211832);
                ctx.fill(bx, by, bx + 2, by + bh, PURPLE);
            } else if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) {
                ctx.fill(bx, by, bx + bw, by + bh, PANEL_HOVER);
            }
            ctx.drawText(textRenderer, c, x + 20, ny + 4, active ? WHITE : MUTED, false);
            ny += 34;
        }

        int cx = x + sideW + 20;
        ctx.drawTextWithShadow(textRenderer, category, cx, y + 70, WHITE);
        ctx.drawText(textRenderer, "Toggle modules · Right Shift to open/close", cx, y + 86, MUTED, false);
        int cardW = (w - sideW - 52) / 2;
        int cardY = y + 112;
        List<ModuleEntry> list = modules.getOrDefault(category, List.of());
        int shown = 0;
        for (ModuleEntry m : list) {
            if (!m.name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) continue;
            int col = shown % 2, row = shown / 2;
            int bx = cx + col * (cardW + 10), by = cardY + row * 66;
            boolean hover = mouseX >= bx && mouseX <= bx + cardW && mouseY >= by && mouseY <= by + 56;
            ctx.fill(bx, by, bx + cardW, by + 56, hover ? PANEL_HOVER : PANEL);
            ctx.fill(bx, by, bx + cardW, by + 1, enabled.contains(m.name) ? PURPLE : LINE);
            ctx.drawText(textRenderer, m.name, bx + 10, by + 9, WHITE, false);
            ctx.drawText(textRenderer, m.tag, bx + 10, by + 24, MUTED, false);
            boolean on = enabled.contains(m.name);
            int sx = bx + cardW - 36, sy = by + 12;
            ctx.fill(sx, sy, sx + 25, sy + 13, on ? PURPLE : 0xFF383B53);
            ctx.fill(on ? sx + 14 : sx + 2, sy + 2, on ? sx + 23 : sx + 11, sy + 11, WHITE);
            shown++;
        }
        if (shown == 0) ctx.drawText(textRenderer, "No modules found", cx, cardY + 12, MUTED, false);
        ctx.fill(x, y + h - 27, x + w, y + h, PANEL);
        ctx.drawText(textRenderer, "TAVIX  •  CONFIG SAVING COMING SOON", x + 14, y + h - 17, MUTED, false);
        ctx.drawText(textRenderer, "ESC  CLOSE", x + w - 82, y + h - 17, PURPLE, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int w = Math.min(760, width - 28), h = Math.min(450, height - 28);
        int x = (width - w) / 2, y = (height - h) / 2, sideW = 142;
        int ny = y + 91;
        for (String c : categories) {
            int by = ny - 5;
            if (mouseX >= x + 9 && mouseX <= x + sideW - 9 && mouseY >= by && mouseY <= by + 27) {
                category = c; return true;
            }
            ny += 34;
        }
        int cx = x + sideW + 20, cardW = (w - sideW - 52) / 2, cardY = y + 112, shown = 0;
        for (ModuleEntry m : modules.getOrDefault(category, List.of())) {
            if (!m.name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) continue;
            int bx = cx + (shown % 2) * (cardW + 10), by = cardY + (shown / 2) * 66;
            if (mouseX >= bx && mouseX <= bx + cardW && mouseY >= by && mouseY <= by + 56) {
                if (!enabled.add(m.name)) enabled.remove(m.name);
                return true;
            }
            shown++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private record ModuleEntry(String name, String description, String tag) {}
}
